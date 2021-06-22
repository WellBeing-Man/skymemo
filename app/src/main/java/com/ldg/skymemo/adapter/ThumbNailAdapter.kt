package com.ldg.skymemo.adapter

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ldg.skymemo.viewholder.ThumbnailViewHolder


// 메모 리스트에서 메모가 가지고 있는 비트맵 이미지의 썸네일 어댑터
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
