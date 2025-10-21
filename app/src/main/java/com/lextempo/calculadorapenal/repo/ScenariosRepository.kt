package com.lextempo.calculadorapenal.repo

import com.lextempo.calculadorapenal.data.local.dao.ScenarioDao
import com.lextempo.calculadorapenal.data.local.entity.ScenarioEntity
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ScenariosRepository {
    suspend fun saveScenario(title: String, payloadJson: String): Long
    suspend fun listScenarios(): List<ScenarioEntity>
    suspend fun deleteScenario(id: Long)
}

class ScenariosRepositoryImpl @Inject constructor(
    private val dao: ScenarioDao
) : ScenariosRepository {
    override suspend fun saveScenario(title: String, payloadJson: String): Long =
        withContext(Dispatchers.IO) {
            dao.insert(ScenarioEntity(title = title, payloadJson = payloadJson))
        }

    override suspend fun listScenarios(): List<ScenarioEntity> =
        withContext(Dispatchers.IO) { dao.list() }

    override suspend fun deleteScenario(id: Long) = withContext(Dispatchers.IO) { dao.delete(id) }
}
