package com.lextempo.calculadorapenal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scenarios")
data class ScenarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val payloadJson: String,
    val createdAt: Long = System.currentTimeMillis()
)
