package com.ldg.skymemo.repository

import android.app.Activity
import com.ldg.skymemo.data.Memo
import com.ldg.skymemo.data.MemoFileSaverImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(private val memoFileSaver: MemoFileSaverImpl) : SaveRepository {

     override suspend fun saveMemo(data:Memo, activity: Activity) : String?{
         return withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
             memoFileSaver.saveOnInternalStorage(data, activity)
         }
     }
}