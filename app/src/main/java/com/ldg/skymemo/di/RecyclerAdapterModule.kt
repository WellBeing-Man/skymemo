package com.ldg.skymemo.di


import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

// todo adapter들의 의존성 줄이기
@Module
@InstallIn(SingletonComponent::class)
object RecyclerAdapterModule {



    @Qualifier
    annotation class AdapterMemoList

}