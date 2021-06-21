package com.ldg.skymemo.factory

import android.app.Application
import com.ldg.skymemo.base.BaseRepository
import com.ldg.skymemo.data.MemoFileSaverImpl
import com.ldg.skymemo.repository.MemoRepositoryImpl
import java.lang.IllegalArgumentException


//ViewModel Factory 구현체
@Deprecated("unused after di")
class RepositoryFactory(private val application: Application) {

    fun <T : BaseRepository?> create(modelClass: Class<T>): T {
        return when {
//            modelClass.isAssignableFrom(MemoRepositoryImpl::class.java)->
//                MemoRepositoryImpl(MemoFileSaverImpl()) as T
            else ->
                throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
