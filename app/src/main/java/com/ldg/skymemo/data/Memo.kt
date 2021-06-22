package com.ldg.skymemo.data

import android.graphics.Bitmap

//id 값을 currentTime값으로 가져오고 있음
//todo 다른 id 할당 알고리즘 필요
data class Memo(
    val id:Long,
    val content:String?,
    val photos:List<Bitmap>?,
    val drawing:Bitmap?
)
