package com.ldg.skymemo.data

import android.app.Activity

interface FileSaver<T> {
    suspend fun saveOnInternalStorage(obj:T, activity: Activity):String?
}