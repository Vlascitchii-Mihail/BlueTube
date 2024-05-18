package com.usm.bluetube.shortslist.di

import com.usm.bluetube.shortslist.repository.ShortsRepository
import com.usm.bluetube.shortslist.repository.ShortsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ModuleShorts {

    @Binds
    @Singleton
    abstract fun bindShortsRepository(shortsRepository: ShortsRepositoryImpl): ShortsRepository
}