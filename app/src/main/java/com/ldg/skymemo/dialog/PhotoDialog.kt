package com.ldg.skymemo.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import com.ldg.skymemo.R

//파일시스템에서 가져온 사진 파일 상세보기 다이얼로그
//todo dialog 의존성 줄이기
class PhotoDialog(context: Context,private val bitmap: Bitmap) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_photo)

        //bitmap 할당
        findViewById<ImageView>(R.id.selectedPhotoView).apply {
            setImageBitmap(bitmap)
        }

        //나가기 버튼 클릭 리스너, 가장 앞으로 가져오기
        findViewById<ImageView>(R.id.exitImageView).apply {
            bringToFront()
            setOnClickListener {
                dismiss()
            }
        }
    }
}