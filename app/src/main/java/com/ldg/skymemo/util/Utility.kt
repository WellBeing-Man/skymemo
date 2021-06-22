package com.ldg.skymemo.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import java.text.SimpleDateFormat


// View에서 background를 비트맵형태로 저장
fun loadBitmapFromView(view: View): Bitmap? {
    val specWidth = View.MeasureSpec.makeMeasureSpec(1800, View.MeasureSpec.EXACTLY)
    view.measure(specWidth, specWidth)
    val questionWidth = view.measuredWidth
    val questionHeight= view.measuredHeight
    val bitmap = Bitmap.createBitmap(questionWidth, questionHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.WHITE)
    view.layout(view.left, view.top, view.right, view.bottom)
    view.draw(canvas)
    return bitmap
}

// timestamp를 파일시스템에 저장
@SuppressLint("SimpleDateFormat")
fun timeToFileName(time:Long):String{
    val simpleDateFormat=SimpleDateFormat("yyyyMMddHHmmssSSSS")
    return simpleDateFormat.format(time).toString()+".wbm"
}