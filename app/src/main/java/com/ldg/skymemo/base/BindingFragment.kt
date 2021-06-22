package com.ldg.skymemo.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

// ViewDataBinding을 사용하는 프래그먼트의 보일러 플레이트 줄이기
open class BindingFragment<V:ViewDataBinding> : Fragment() {

    protected lateinit var binding:V

    protected fun bindingView(inflater: LayoutInflater, redId:Int, container: ViewGroup?, viewLifecycleOwner: LifecycleOwner) {
        binding= DataBindingUtil.inflate(inflater,redId,container,false)
        binding.apply { lifecycleOwner=viewLifecycleOwner }
    }
}