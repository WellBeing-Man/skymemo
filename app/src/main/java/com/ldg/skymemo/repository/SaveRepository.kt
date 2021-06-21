package com.ldg.skymemo.repository

import android.app.Activity
import com.ldg.skymemo.data.Memo

interface SaveRepository {
    suspend fun saveMemo(data: Memo, activity: Activity):String?
}