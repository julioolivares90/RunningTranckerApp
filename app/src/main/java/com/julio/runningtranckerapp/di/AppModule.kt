package com.julio.runningtranckerapp.di

import android.content.Context
import androidx.room.Room
import com.julio.runningtranckerapp.db.RunningDataBase
import com.julio.runningtranckerapp.other.Constants.RUNNING_DATABSE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,RunningDataBase::class.java,
        RUNNING_DATABSE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDao(db: RunningDataBase) = db.getRunDao()
}