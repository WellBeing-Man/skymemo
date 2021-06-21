package com.ldg.skymemo.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

open class BindingFragment<V:ViewDataBinding> : Fragment() {

    protected lateinit var binding:V

    protected fun bindingView(inflater: LayoutInflater, redId:Int, container: ViewGroup?, viewLifecycleOwner: LifecycleOwner) {
        binding= DataBindingUtil.inflate(inflater,redId,container,false)
        binding.apply { lifecycleOwner=viewLifecycleOwner }
    }
}