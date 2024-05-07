package com.usm.bluetube.videolist.di

import com.usm.bluetube.videolist.repository.VideoListRepository
import com.usm.bluetube.videolist.repository.VideoListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ModuleVideoList {

    @Binds
    @Singleton
    abstract fun bindVideoRepository(videoRepository: VideoListRepositoryImpl): VideoListRepository
}