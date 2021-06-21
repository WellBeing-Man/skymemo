package com.ldg.skymemo.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.ldg.skymemo.R
import com.ldg.skymemo.adapter.MemoListAdapter
import com.ldg.skymemo.base.BindingFragment
import com.ldg.skymemo.data.Memo
import com.ldg.skymemo.databinding.FragmentMemoListBinding
import com.ldg.skymemo.di.RecyclerAdapterModule
import com.ldg.skymemo.memo.MemoActivity
import com.ldg.skymemo.viewholder.MemoListViewHolder
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@AndroidEntryPoint
class ListFragment : BindingFragment<FragmentMemoListBinding>() {

    private val DEBUG_TAG="ListFragment"

    private val listViewModel:ListViewModel by viewModels()


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

        listViewModel.addButtonClicked.observe(viewLifecycleOwner,{
            clicked->
            if(clicked){
                Intent(requireActivity(),MemoActivity::class.java).also {
                    startActivity(it)
                }
                listViewModel.onClickAddButtonDone()
            }
        })

        listViewModel.refreshList.observe(viewLifecycleOwner, { refresh ->
            if (refresh) {
                readMemoFile()
                listViewModel.onRefreshListDone()
            }
        })

        listViewModel.memoList.observe(viewLifecycleOwner,{
            list->
               memoListAdapter.submitList(list)
              memoListAdapter.notifyDataSetChanged()
            Log.d(DEBUG_TAG,"adapter notified")

        })

        initRecycler()


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


    private fun readMemoFile() {
        val files= File(requireContext().filesDir.toURI())
        listViewModel.addMemo(files.listFiles())
    }

}