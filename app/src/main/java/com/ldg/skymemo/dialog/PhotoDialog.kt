package com.ldg.skymemo.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import com.ldg.skymemo.R

class PhotoDialog(context: Context,private val bitmap: Bitmap) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_photo)

        findViewById<ImageView>(R.id.selectedPhotoView).apply {
            setImageBitmap(bitmap)
        }
        findViewById<ImageView>(R.id.exitImageView).apply {
            setOnClickListener {
                dismiss()
            }
        }
    }
}