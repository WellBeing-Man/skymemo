package com.ldg.skymemo.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ldg.skymemo.data.Memo
import com.ldg.skymemo.data.MemoFile
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream


// file을 memofile로 맵
fun File.mapToMemoFile():MemoFile? {

    return try {
        val fis = FileInputStream(this)
        val ois= ObjectInputStream(fis)

        (ois.readObject()) as MemoFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// memofile을 memo로 맵핑
fun MemoFile.mapToMemo(): Memo {
    var background: Bitmap? = null
    val photoList= mutableListOf<Bitmap>()
    if(drawing != null)
         background=BitmapFactory.decodeByteArray(this.drawing,0, drawing.size,BitmapFactory.Options())
    if(photos!!.isNotEmpty()){
        photos.forEach {
            bytes: ByteArray? ->
            bytes?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size, BitmapFactory.Options())
                photoList.add(bitmap)
            }
        }
    }

    return Memo(id,content,photoList,background)

}