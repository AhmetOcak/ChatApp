package com.ahmetocak.data.repository.user

import com.ahmetocak.common.Response
import com.ahmetocak.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun create(
        email: String,
        username: String,
        profilePicUrl: String?
    ): Response<User>

    suspend fun getUser(userEmail: String): Response<User>
    suspend fun deleteUser(email: String): Response<Unit>
    suspend fun updateUser(
        email: String,
        username: String?,
        profilePicUrl: String?
    ): Response<Unit>
    suspend fun addUserToCache(user: User): Response<Unit>
    suspend fun observeUserInCache(): Response<Flow<User?>>
    suspend fun deleteUserFromCache(user: User): Response<Unit>
    suspend fun updateUserInCache(user: User): Response<Unit>
    suspend fun clearAllUserData()
    suspend fun getUserEmail(): Response<String>
    suspend fun uploadUserFcmToken(email: String, token: String): Response<Unit>
}