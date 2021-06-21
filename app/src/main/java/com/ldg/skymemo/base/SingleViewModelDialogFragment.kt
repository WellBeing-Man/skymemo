package com.ldg.skymemo.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.ldg.skymemo.factory.ViewModelFactory

//ViewModel 사용하는 Dialog Fragment입니다.
@Deprecated("unused after di")
open class SingleViewModelDialogFragment<V: ViewModel,B: ViewDataBinding> : DialogFragment() ,
    UseSingleViewModel<V, B> {

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
