package com.ldg.skymemo.di

import com.ldg.skymemo.data.FileSaver
import com.ldg.skymemo.data.Memo
import com.ldg.skymemo.data.MemoFileSaverImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent


// file saver module
@InstallIn(SingletonComponent::class)
@Module
object FileSaverModule{

    @Singleton
    @Provides
    fun fileSaver() : FileSaver<Memo>{
        return MemoFileSaverImpl()
    }
}