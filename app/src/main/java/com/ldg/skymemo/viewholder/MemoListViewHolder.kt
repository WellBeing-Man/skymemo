package com.ldg.skymemo.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ldg.skymemo.R
import com.ldg.skymemo.adapter.ThumbNailAdapter
import com.ldg.skymemo.data.Memo
import com.ldg.skymemo.databinding.ItemMemoListBinding


//메모 리스트에 대한 뷰 홀더
class MemoListViewHolder(private val binding: ItemMemoListBinding) : RecyclerView.ViewHolder(binding.root) {

    private val thumbNailAdapter =ThumbNailAdapter()

    fun bind(item: Memo, onDelete: (memo:Memo)->Unit)= with(binding){

        resetMotion()

        // 데이터 바인딩
        memoContentTextView.text=item.content

        Glide.with(backgroundImageView)
                .load(item.drawing)
                .into(backgroundImageView)

        photoRecyclerView.layoutManager=GridLayoutManager(binding.root.context,3)
        photoRecyclerView.adapter=thumbNailAdapter

        thumbNailAdapter.list.clear()
        item.photos?.forEach {
            thumbNailAdapter.list.add(it)
        }

        binding.deleteTextButton.setOnClickListener {
            onDelete(item)
        }
        thumbNailAdapter.notifyDataSetChanged()

    }

    // motion상태를 초기화 하기 위한 메서드
    private fun resetMotion(){
        this.binding.memoContainerLayout.transitionToStart()
    }

    companion object {
        fun from(parent: ViewGroup): MemoListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMemoListBinding.inflate(layoutInflater, parent, false)
            return MemoListViewHolder(binding)
        }
    }
}