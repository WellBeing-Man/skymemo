package com.ldg.skymemo.data

import java.io.Serializable

// fileName 은 id값을 기준으로 만들어짐 id+.wbm
class MemoFile(
    val fileName:String,
    val id: Long,
    val content: String?,
    val photos: List<ByteArray?>?,
    val drawing: ByteArray?
) :Serializable{




}