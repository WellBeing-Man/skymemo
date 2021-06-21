package com.ldg.skymemo.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding


//단일 뷰 모델을 사용하는 짜여질 UI 컴포넌트에 사용할 인터페이스입니다.
@Deprecated("unused after di")
interface UseSingleViewModel<V: ViewModel,B:ViewBinding>{
    var binding: B
    var viewModel: V

    //ViewModel을 생성하는 로직이 들어가야합니다.
    fun createViewModel(modelClass: Class<V>, viewModelStoreOwner: ViewModelStoreOwner)

    //ViewBinding을 생성하는 로직이 들어가야합니다.
    fun bindingView(inflater: LayoutInflater, redId:Int, container: ViewGroup?,viewLifecycleOwner: LifecycleOwner)
}
