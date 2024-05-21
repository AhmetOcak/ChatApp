package com.ahmetocak.network.datasource.ktor_user

import com.ahmetocak.common.Response
import com.ahmetocak.network.model.NetworkUser

interface UserRemoteDataSource {

    suspend fun create(
        email: String,
        username: String,
        profilePicUrl: String?
    ): Response<NetworkUser>

    suspend fun getById(userEmail: String): Response<NetworkUser>
    suspend fun deleteUser(userEmail: String): Response<Unit>
    suspend fun updateUser(
        userEmail: String,
        username: String?,
        profilePicUrl: String?
    ): Response<Unit>
}