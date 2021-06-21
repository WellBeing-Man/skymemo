package com.ldg.skymemo.di

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ldg.skymemo.adapter.MemoListAdapter
import com.ldg.skymemo.adapter.ThumbNailAdapter
import com.ldg.skymemo.data.Memo
import com.ldg.skymemo.viewholder.MemoListViewHolder
import com.ldg.skymemo.viewholder.ThumbnailViewHolder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecyclerAdapterModule {



    @Qualifier
    annotation class AdapterMemoList

}