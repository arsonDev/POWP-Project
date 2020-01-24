package pl.heng.database.repository

import pl.heng.database.model.IModel

interface Repository {
    suspend fun insert(model : IModel)
}