package com.lextempo.calculadorapenal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lextempo.calculadorapenal.data.local.dao.ScenarioDao
import com.lextempo.calculadorapenal.data.local.entity.ScenarioEntity

@Database(entities = [ScenarioEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scenarioDao(): ScenarioDao
}
