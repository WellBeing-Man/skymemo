package com.ldg.skymemo.data

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.ldg.skymemo.util.timeToFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton


@Suppress("BlockingMethodInNonBlockingContext")
@Singleton
class MemoFileSaverImpl @Inject constructor() : FileSaver<Memo> {

    override suspend fun saveOnInternalStorage(obj: Memo, activity: Activity):String? {

        val fileName= timeToFileName(obj.id)
        MemoFile(fileName,obj.id,obj.content,convertBitmapListToByteArrayList(obj.photos),convertBitmapToByteArray(obj.drawing)).let {
            moveFile->
            try {
                withContext(Dispatchers.IO) {
                    (activity as Context).openFileOutput(fileName, Context.MODE_PRIVATE)
                            .use { fileOutputStream ->
                                fileOutputStream.write(convertSerializableToByteArray(moveFile))
                                fileOutputStream.close()
                            }
                }
                Log.d("MemoFileSaver",fileName)
                return fileName
            }catch (f:FileNotFoundException){
                f.printStackTrace()
                return null
            }catch (i:IOException){
                i.printStackTrace()
                return null
            }
            return null
        }
    }



    private fun convertBitmapListToByteArrayList(photos: List<Bitmap>?): List<ByteArray>? {

        photos?:return null

        val bytes:MutableList<ByteArray> = mutableListOf()

        photos.let {
                list->
            for( i in list.indices){
                val byteOutputStream= ByteArrayOutputStream()
                list[i].compress(Bitmap.CompressFormat.JPEG,100,byteOutputStream)
                bytes.add(byteOutputStream.toByteArray())
            }
        }

        return bytes
    }


    private fun convertBitmapToByteArray(bitmap: Bitmap?): ByteArray? {

        bitmap?:return null

        var bytes: ByteArray?
        bitmap.let {
            val byteOutputStream=ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG,100,byteOutputStream)
            bytes=byteOutputStream.toByteArray()
        }
        return bytes?:throw NullPointerException()
    }


    private fun convertSerializableToByteArray(obj: Serializable?): ByteArray? {
            obj?:return null
            var bytes:ByteArray?=null
        obj.let {
            try {
                val bos=ByteArrayOutputStream()
                val out=ObjectOutputStream(bos)
                out.writeObject(obj)
                out.flush()
                bytes=bos.toByteArray()
                out.close()
                bos.close()
            }catch (n:NullPointerException){
                n.printStackTrace()
            }
            return bytes?:throw  NullPointerException()
        }
    }


}