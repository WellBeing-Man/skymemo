package com.ldg.skymemo.data

import java.io.Serializable

class MemoFile(
    val fileName:String,
    val id: Long,
    val content: String?,
    val photos: List<ByteArray?>?,
    val drawing: ByteArray?
) :Serializable{




}