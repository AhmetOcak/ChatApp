package com.ahmetocak.database.datasource.user

import com.ahmetocak.common.Response
import com.ahmetocak.database.dao.UserDao
import com.ahmetocak.database.db.UserDatabase
import com.ahmetocak.database.entity.UserEntity
import com.ahmetocak.database.utils.dbCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
    private val db: UserDatabase
): UserLocalDataSource {
    override suspend fun addUser(userEntity: UserEntity): Response<Unit> {
        return dbCall { userDao.addUser(userEntity) }
    }

    override suspend fun observeUser(): Response<Flow<UserEntity?>> {
        return dbCall { userDao.observeUser() }
    }

    override suspend fun deleteUser(userEntity: UserEntity): Response<Unit> {
        return dbCall { userDao.deleteUser(userEntity) }
    }

    override suspend fun updateUser(userEntity: UserEntity): Response<Unit> {
        return dbCall { userDao.updateUser(userEntity) }
    }

    override suspend fun clearAllTable() = db.clearAllTables()
}