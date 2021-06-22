package com.ldg.skymemo.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ldg.skymemo.R
import com.ldg.skymemo.adapter.MemoListAdapter
import com.ldg.skymemo.base.BindingFragment
import com.ldg.skymemo.databinding.FragmentMemoListBinding
import com.ldg.skymemo.memo.MemoActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@AndroidEntryPoint
class ListFragment : BindingFragment<FragmentMemoListBinding>() {

    private val DEBUG_TAG="ListFragment"

    private val listViewModel:ListViewModel by viewModels()

    //todo adapter 의존성 줄이기
    private lateinit var memoListAdapter: MemoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView(inflater,R.layout.fragment_memo_list,container,viewLifecycleOwner)
        binding.memoListViewModel=listViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObserver()
        initRecycler()
     }


    private fun bindObserver() {
        // 새로운 메모 추가
        listViewModel.addButtonClicked.observe(viewLifecycleOwner, { clicked ->
            if (clicked) {
                Intent(requireActivity(), MemoActivity::class.java).also {
                    startActivity(it)
                }
                listViewModel.onClickAddButtonDone()
            }
        })
        //메모 리스트 리프레쉬
        listViewModel.refreshList.observe(viewLifecycleOwner, { refresh ->
            if (refresh) {
                readMemoFile()
                listViewModel.onRefreshListDone()
            }
        })
        //메모 리스트 새로 추가
        listViewModel.memoList.observe(viewLifecycleOwner, { list ->
            memoListAdapter.submitList(list)
            memoListAdapter.notifyDataSetChanged()
        })
    }


    private fun initRecycler() = with(binding){
        memoListAdapter= MemoListAdapter { memo ->
            listViewModel.remove(memo)
        }
        memoRecyclerView.adapter=memoListAdapter
    }


    override fun onResume() {
        super.onResume()
        Log.d(DEBUG_TAG,"OnResume Called")
        listViewModel.onRefreshList()
    }

    //파일시스템에서 메모 파일 읽어오기
    private fun readMemoFile() {
        val files= File(requireContext().filesDir.toURI())
        listViewModel.refreshMemo(files.listFiles())
    }

}