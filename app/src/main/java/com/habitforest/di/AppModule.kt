package com.habitforest.di

import android.content.Context
import androidx.room.Room
import com.habitforest.data.local.HabitDao
import com.habitforest.data.local.HabitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext ctx: Context): HabitDatabase =
        Room.databaseBuilder(ctx, HabitDatabase::class.java, "habit_forest.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideHabitDao(db: HabitDatabase): HabitDao = db.habitDao()
}
