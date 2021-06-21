package com.ldg.skymemo.memo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var handlePictureAdapter : HandlePictureAdapter

    private lateinit var onBackPressedCallback: OnBackPressedCallback


    private var tempFilePath:String=""

    private  val camRequestActivityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ) { result ->
        addBitmapFromCamToViewModel(result)
    }

    private  val albumRequestActivityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ) { result ->
        addBitmapFromAlbumToViewModel(result)
    }

    private fun addBitmapFromCamToViewModel(result: ActivityResult) {
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = BitmapFactory.decodeFile(tempFilePath)
                bitmap ?: throw NullPointerException()
                handleViewModel.addPicture(bitmap)
            }
        } catch (n: NullPointerException) {
            n.printStackTrace()
        }
    }

    private fun addBitmapFromAlbumToViewModel(result: ActivityResult) {
        result.data?.data?.let {
            try {
                val inputStream = requireContext().contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                handleViewModel.addPicture(bitmap)
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
                R.id.pictureRadioButton->{
                    pictureSelectGroup.visibility=View.VISIBLE
                }
            }
        }

        undoDrawImageView.setOnClickListener {
            drawView.onClickUndo()
        }

        chooseAlbumImageView.setOnClickListener {
            albumRequest()
        }

        chooseCamImageView.setOnClickListener {
            camRequest()
        }

    }



    private fun albumRequest() {
        Intent(Intent.ACTION_PICK).also { intent->
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            albumRequestActivityLauncher.launch(intent)
        }
    }


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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleViewModel.onSaveButtonClick()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

}