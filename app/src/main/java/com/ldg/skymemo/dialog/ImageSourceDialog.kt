package com.ldg.skymemo.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.FileProvider
import com.ldg.skymemo.R
import com.ldg.skymemo.base.SingleViewModelDialogFragment
import com.ldg.skymemo.databinding.ImageSourceDialogBinding
import com.ldg.skymemo.memo.HandleViewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ImageSourceDialog: SingleViewModelDialogFragment<HandleViewModel, ImageSourceDialogBinding>() {


      private var tempFilePath:String=""

    private  val camRequestActivityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
              StartActivityForResult()
      ) { result ->
          try {
              if(result.resultCode==Activity.RESULT_OK) {
                  val bitmap = BitmapFactory.decodeFile(tempFilePath)
                  bitmap ?: throw NullPointerException()
                  viewModel.addPicture(bitmap)
              }
          }catch (n: NullPointerException){
              n.printStackTrace()
          }

    }

    private  val albumRequestActivityLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            StartActivityForResult()
    ) { result ->
            result.data?.data?.let {
                    try {
                        val inputStream=requireContext().contentResolver.openInputStream(it)
                        val bitmap=BitmapFactory.decodeStream(inputStream)
                        viewModel.addPicture(bitmap)
                    }catch (f: FileNotFoundException){
                        f.printStackTrace()
                    }
            }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bindingView(inflater = inflater, container = container, redId = R.layout.image_source_dialog, viewLifecycleOwner = viewLifecycleOwner)
        createViewModel(HandleViewModel::class.java, requireActivity())
        binding.handleViewModel=viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.chooseCamImageView).setOnClickListener {
            dispatchTakePictureIntent()
        }

        view.findViewById<ImageView>(R.id.chooseAlbumImageView).setOnClickListener {
            albumRequest()
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
    private fun dispatchTakePictureIntent() {
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

}