package com.ldg.skymemo.data

import android.app.Activity

//파일을 로컬 파일 시스템에 저장하기 위한 인터페이스
interface FileSaver<T> {
    suspend fun saveOnInternalStorage(obj:T, activity: Activity):String?
}