package com.ldg.skymemo.logo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoViewModel  @Inject constructor(): ViewModel() {

    private val _onMovePage = MutableLiveData<Boolean>().apply { value=false }
    val onMovePage:LiveData<Boolean>
        get() = _onMovePage

    fun onMovePageStart(){
        viewModelScope.launch {
            delay(2000)
            _onMovePage.value=true
        }
    }
    fun onMovePageDone(){
            _onMovePage.value=false
    }
}
