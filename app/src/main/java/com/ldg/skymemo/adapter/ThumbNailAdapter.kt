package com.ldg.skymemo.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ldg.skymemo.databinding.ItemThumnailImageBinding
import com.ldg.skymemo.viewholder.ThumbnailViewHolder
import javax.inject.Inject

class ThumbNailAdapter: RecyclerView.Adapter<ThumbnailViewHolder>() {

    val list= mutableListOf<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailViewHolder {
        return ThumbnailViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ThumbnailViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
