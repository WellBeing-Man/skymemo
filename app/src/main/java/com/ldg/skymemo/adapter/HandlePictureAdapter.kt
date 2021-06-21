package com.ldg.skymemo.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ldg.skymemo.R
import com.ldg.skymemo.adapter.HandlePictureAdapter.*
import com.ldg.skymemo.databinding.ItemHandleImageBinding

class HandlePictureAdapter(private val onClickCallBack:(bitmap:Bitmap)->Unit,private val onDeleteCallback:(index:Int)->Unit) : ListAdapter<BitmapItem, ViewHolder>(diffUtil)  {

    private val DEBUG_TAG: String="HandlePictureAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position].bitmap,onClickCallBack)
        holder.binding.deletePictureImageVIew.setOnClickListener {
            onDeleteCallback(position)
        }
    }

    class ViewHolder(val binding: ItemHandleImageBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: Bitmap, onClick: (bitmap:Bitmap)->Unit){
                Glide.with(binding.root.context)
                    .load(item)
                    .into(binding.itemPicture)

                binding.itemPicture.setOnClickListener {
                    onClick(item)
                }

                binding.deletePictureImageVIew.bringToFront()
            }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHandleImageBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<BitmapItem>() {
            override fun areItemsTheSame(oldItem: BitmapItem, newItem: BitmapItem): Boolean {
              return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: BitmapItem, newItem: BitmapItem): Boolean {
                return oldItem.bitmap.sameAs(newItem.bitmap)
            }

        }
    }

    data class BitmapItem(
            val id:Int,
            val bitmap: Bitmap
    )

}