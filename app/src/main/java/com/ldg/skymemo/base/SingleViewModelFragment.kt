package com.ldg.skymemo.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.ldg.skymemo.factory.ViewModelFactory

//MVVM구조를 사용하는 Fragment입니다.
@Deprecated("unused after di")
abstract class SingleViewModelFragment<V: ViewModel,B: ViewDataBinding> : Fragment() , UseSingleViewModel<V, B> {

    override lateinit var binding: B
    override lateinit var viewModel: V


    //connecting view model method
    override fun createViewModel(modelClass: Class<V>, viewModelStoreOwner: ViewModelStoreOwner) {
        viewModel=
            ViewModelProvider(viewModelStoreOwner,
                ViewModelFactory(application = requireActivity().application)
            ).get(modelClass)
    }
    //binding view method
    override fun bindingView(inflater: LayoutInflater, redId:Int, container: ViewGroup?,viewLifecycleOwner: LifecycleOwner) {
        binding= DataBindingUtil.inflate(inflater,redId,container,false)
        binding.apply { lifecycleOwner=viewLifecycleOwner }
    }
}
