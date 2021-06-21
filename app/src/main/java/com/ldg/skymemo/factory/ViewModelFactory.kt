package com.ldg.skymemo.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ldg.skymemo.logo.LogoViewModel
import com.ldg.skymemo.memo.HandleViewModel
import com.ldg.skymemo.repository.MemoRepositoryImpl
import java.lang.IllegalArgumentException


//ViewModel Factory 구현체
@Deprecated("unused after di")
class ViewModelFactory(val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
//            modelClass.isAssignableFrom(ListViewModel::class.java) ->
//                ListViewModel(RepositoryFactory(application).create(MemoRepository::class.java)) as T
            modelClass.isAssignableFrom(LogoViewModel::class.java)->
                LogoViewModel() as T
//            modelClass.isAssignableFrom(HandleViewModel::class.java)->
//                HandleViewModel(RepositoryFactory(application).create(MemoRepositoryImpl::class.java)) as T
            else ->
                throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
