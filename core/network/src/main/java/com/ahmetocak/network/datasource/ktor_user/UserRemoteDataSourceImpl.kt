package com.ahmetocak.network.datasource.ktor_user

import com.ahmetocak.common.Response
import com.ahmetocak.network.api.KtorChatApi
import com.ahmetocak.network.helper.apiCall
import com.ahmetocak.network.model.NetworkUser
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(private val api: KtorChatApi): UserRemoteDataSource {
    override suspend fun create(
        email: String,
        username: String,
        profilePicUrl: String?
    ): Response<NetworkUser> {
        return apiCall {
            api.createUser(
                email = email,
                username = username,
                profilePicUrl = profilePicUrl
            )
        }
    }

    override suspend fun getById(userEmail: String): Response<NetworkUser> {
        return apiCall { api.getUser(userEmail) }
    }

    override suspend fun deleteUser(userEmail: String): Response<Unit> {
        return apiCall { api.deleteUser(userEmail) }
    }

    override suspend fun updateUser(
        userEmail: String,
        username: String?,
        profilePicUrl: String?
    ): Response<Unit> {
        return apiCall {
            api.updateUser(
                userEmail = userEmail,
                username = username,
                profilePicUrl = profilePicUrl
            )
        }
    }
}