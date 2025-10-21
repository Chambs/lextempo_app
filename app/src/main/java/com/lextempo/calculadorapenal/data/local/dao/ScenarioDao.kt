package com.lextempo.calculadorapenal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lextempo.calculadorapenal.data.local.entity.ScenarioEntity

@Dao
interface ScenarioDao {
    @Insert
    suspend fun insert(e: ScenarioEntity): Long

    @Query("SELECT * FROM scenarios ORDER BY createdAt DESC")
    suspend fun list(): List<ScenarioEntity>

    @Query("DELETE FROM scenarios WHERE id = :id")
    suspend fun delete(id: Long)
}
