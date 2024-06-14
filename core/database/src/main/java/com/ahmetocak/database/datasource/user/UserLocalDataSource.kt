package com.ahmetocak.database.datasource.user

import com.ahmetocak.common.Response
import com.ahmetocak.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {

    suspend fun addUser(userEntity: UserEntity): Response<Unit>
    suspend fun observeUser(): Response<Flow<UserEntity?>>
    suspend fun deleteUser(userEntity: UserEntity): Response<Unit>
    suspend fun updateUser(userEntity: UserEntity): Response<Unit>
    suspend fun clearAllTable()
    suspend fun getUserEmail(): Response<String>
}