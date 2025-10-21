package com.lextempo.calculadorapenal.di

import android.content.Context
import androidx.room.Room
import com.lextempo.calculadorapenal.data.local.AppDatabase
import com.lextempo.calculadorapenal.data.local.dao.ScenarioDao
import com.lextempo.calculadorapenal.repo.ScenariosRepository
import com.lextempo.calculadorapenal.repo.ScenariosRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides @Singleton
    fun db(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun scenarioDao(db: AppDatabase): ScenarioDao = db.scenarioDao()

    @Provides @Singleton
    fun scenariosRepo(dao: ScenarioDao): ScenariosRepository =
        ScenariosRepositoryImpl(dao)
}
