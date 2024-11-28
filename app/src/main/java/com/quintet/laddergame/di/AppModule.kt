package com.quintet.laddergame.di

import com.quintet.laddergame.data.AppContainer
import com.quintet.laddergame.data.AppContainerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppContainer(): AppContainer {
        return AppContainerImpl()
    }
}