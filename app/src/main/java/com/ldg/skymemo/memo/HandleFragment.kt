package com.ldg.skymemo.memo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ldg.skymemo.R
import com.ldg.skymemo.adapter.HandlePictureAdapter
import com.ldg.skymemo.base.BindingFragment
import com.ldg.skymemo.databinding.FragmentMemoHandleBinding
import com.ldg.skymemo.dialog.PhotoDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HandleFragment : BindingFragment<FragmentMemoHandleBinding>() {

    private val handleViewModel : HandleViewModel by viewModels()

    //todo adapter에 대한 의존성 줄이
    private lateinit var handlePictureAdapter : HandlePictureAdapter

    //메모 저장 콜백
    private lateinit var onBackPressedSaveMemoCallback: OnBackPressedCallback


    //비트맵 파일을 위한 temp variable
    private var tempFilePath:String=""

    // 카메라 이벤트 요청
    private  val camRequestActivityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ) { result ->
        addBitmapFromCamToViewModel(result)
    }
    //앨범에 이벤트 요청
    private  val albumRequestActivityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ) { result ->
        addBitmapFromAlbumToViewModel(result)
    }

    //뷰모델에 카메라 이벤트 result 전달
    private fun addBitmapFromCamToViewModel(result: ActivityResult) {
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = BitmapFactory.decodeFile(tempFilePath)
                bitmap ?: throw NullPointerException()
                addBitmapToViewModel(bitmap)


            }
        } catch (n: NullPointerException) {
            n.printStackTrace()
        }
    }
    //뷰모델에 앨범 이벤트 result 전달
    private fun addBitmapFromAlbumToViewModel(result: ActivityResult) {
        result.data?.data?.let {
            try {
                val inputStream = requireContext().contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                addBitmapToViewModel(bitmap)
                inputStream?:throw NullPointerException()
                inputStream.close()

            } catch (f: FileNotFoundException) {
                f.printStackTrace()
            } catch (n:NullPointerException){
                n.printStackTrace()
            }
        }
    }




    private fun initObserver() = with(handleViewModel){
        saveButtonClick.observe(viewLifecycleOwner,{
            click->
            if(click){
                saveMemo(binding.memoEditText.text.toString(),binding.drawView,requireActivity())
                onSaveButtonClickDone()
                requireActivity().finish()
            }
        })
        pictures.observe(viewLifecycleOwner, { list ->
            handlePictureAdapter.submitList(list.mapIndexed { index, bitmap ->
                HandlePictureAdapter.BitmapItem(index,bitmap)
            })
        })
    }


    private fun initRecycler()= with(binding) {
        //TODO 어떻게 하면 의존성을 줄일까?
        handlePictureAdapter= HandlePictureAdapter(onClickCallBack =
        {
                bitmap->
            PhotoDialog(requireContext(),bitmap).show()
        },
            onDeleteCallback = {
                index->
                handleViewModel.removePicture(index)
            }
        )
        pictureRecycler.adapter=handlePictureAdapter
        pictureRecycler.layoutManager=LinearLayoutManager(context).apply { orientation=LinearLayoutManager.HORIZONTAL }
    }


    // 각 라디오 버튼 클릭 리스너
    private fun initClick()= with(binding) {
        radioGroup.setOnCheckedChangeListener { rG, id ->
            when (id) {
                R.id.writeRadioButton -> {
                    memoEditText.isEnabled=true
                    memoEditText.requestFocus()
                    drawingGroup.visibility = View.GONE
                    pictureSelectGroup.visibility=View.GONE
                    drawView.lockDrawMode()
                }
                R.id.drawRadioButton -> {
                    memoEditText.isEnabled=false
                    drawView.unlockDrawMode()
                    drawingGroup.visibility = View.VISIBLE
                    pictureSelectGroup.visibility=View.GONE
                }
                R.id.pictureRadioButton -> {
                    if (handleViewModel.checkLimit()) {
                        pictureSelectGroup.visibility = View.VISIBLE
                    }else{
                        showLimited()
                    }
                }
            }
        }

        undoDrawImageView.setOnClickListener {
            drawView.onClickUndo()
        }

        chooseAlbumImageView.setOnClickListener {
            if (handleViewModel.checkLimit()) {
                albumRequest()
                }else{
                    showLimited()
            }
        }

        chooseCamImageView.setOnClickListener {
            if (handleViewModel.checkLimit()) {
                camRequest()
            }else{
                showLimited()
            }
        }

    }

    private fun showLimited() {
        Toast.makeText(requireContext(),"사진을 6장까지 추가 가능합니다.",Toast.LENGTH_LONG).show()
    }


    //앨범 리퀘스트
    private fun albumRequest() {
        Intent(Intent.ACTION_PICK).also { intent->
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            albumRequestActivityLauncher.launch(intent)
        }
    }

    // 카메라 리쿼스트
    @SuppressLint("QueryPermissionsNeeded")
    private fun camRequest() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            "com.ldg.skymemo.picture_provider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
                photoFile?.delete()
            }
            camRequestActivityLauncher.launch(takePictureIntent)
        }
    }

    //카메라 리퀘스트에서 원본 파일 사이즈로 비트맵을 받아오기 위한 임시 파일 생성 메서드
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
        ).apply { tempFilePath=absolutePath }
    }

    private fun addBitmapToViewModel(bitmap: Bitmap?) {
        handleViewModel.addPicture(bitmap)
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        bindingView(inflater = inflater,container = container,redId = R.layout.fragment_memo_handle,viewLifecycleOwner = viewLifecycleOwner)
        binding.memoHandleViewModel=handleViewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initClick()
        initRecycler()
    }

    // pressback일때 메모 파일 자동으로 저장 콜백 바인딩
    override fun onAttach(context: Context) {
        super.onAttach(context)

        onBackPressedSaveMemoCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleViewModel.onSaveButtonClick()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedSaveMemoCallback)
    }
    // pressback 콜백 제거
    override fun onDetach() {
        super.onDetach()
        onBackPressedSaveMemoCallback.remove()
    }

}