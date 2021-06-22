package com.ldg.skymemo.logo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ldg.skymemo.R
import com.ldg.skymemo.base.BindingFragment
import com.ldg.skymemo.databinding.FragmentLogoBinding
import dagger.hilt.android.AndroidEntryPoint



// 로고를 보여주는 프래그먼트
@AndroidEntryPoint
class LogoFragment : BindingFragment<FragmentLogoBinding>() {

    private val logoViewModel:LogoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView(inflater,R.layout.fragment_logo,container,viewLifecycleOwner)
        binding.logoViewModel=logoViewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnMovePage()
    }

    //2초간 대기후 리스트 프래그먼트로 이동
    private fun setOnMovePage() {
        logoViewModel.onMovePageStart()

        logoViewModel.onMovePage.observe(viewLifecycleOwner, { start ->
            if (start) {
                findNavController().navigate(LogoFragmentDirections.actionLogoFragmentToMemoListFragment())
                logoViewModel.onMovePageDone()
            }
        })
    }

}