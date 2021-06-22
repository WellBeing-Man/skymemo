package com.ldg.skymemo.viewholder

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ldg.skymemo.databinding.ItemThumnailImageBinding
import javax.inject.Inject

//메모 리스트의 썸네일을 위한 뷰홀더
class ThumbnailViewHolder(private val binding: ItemThumnailImageBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Bitmap){
        Glide.with(binding.root.context)
                .load(item)
                .into(binding.thumbNailImageView)
    }

    companion object {
        fun from(parent: ViewGroup): ThumbnailViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemThumnailImageBinding.inflate(layoutInflater, parent, false)
            return ThumbnailViewHolder(binding)
        }
    }
}