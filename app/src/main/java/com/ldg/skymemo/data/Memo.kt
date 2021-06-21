package com.ldg.skymemo.data

import android.graphics.Bitmap

data class Memo(
    val id:Long,
    val content:String?,
    val photos:List<Bitmap>?,
    val drawing:Bitmap?
)
