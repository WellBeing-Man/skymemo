package com.ldg.skymemo.repository

import android.util.Log
import javax.inject.Inject

class MemoReadRepositoryImpl @Inject constructor(): ReadRepository {
    override fun read() {
        Log.d("MemoReadRepositoryImpl","Read")
    }
}