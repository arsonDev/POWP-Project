package pl.heng.database.repository

import pl.heng.database.model.IModel

class Repo(val repository: Repository) {

    suspend fun insert(model : IModel){
        repository.insert(model)
    }
}