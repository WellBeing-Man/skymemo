package com.ldg.skymemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ldg.skymemo.data.Memo
import com.ldg.skymemo.databinding.ItemMemoListBinding
import com.ldg.skymemo.viewholder.MemoListViewHolder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



class MemoListAdapter (private val onDelete: (memo:Memo)->Unit) : ListAdapter<Memo, MemoListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoListViewHolder {
        return MemoListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MemoListViewHolder, position: Int) {

        holder.bind(currentList[position],onDelete)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem==newItem
            }

        }
    }

}
