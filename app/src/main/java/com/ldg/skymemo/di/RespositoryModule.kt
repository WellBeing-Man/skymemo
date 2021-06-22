package com.ldg.skymemo.di

import com.ldg.skymemo.repository.MemoReadRepositoryImpl
import com.ldg.skymemo.repository.MemoRepositoryImpl
import com.ldg.skymemo.repository.ReadRepository
import com.ldg.skymemo.repository.SaveRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

// todo db와 network 추가시 리포추가
@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule{

    @Binds
    abstract fun memoReadRepository(memoReadRepositoryImpl: MemoReadRepositoryImpl) : ReadRepository

    @Binds
    abstract fun memoSaveRepository(memoRepositoryImpl: MemoRepositoryImpl):SaveRepository

}


@Qualifier
annotation class FileSystemReadRepo
