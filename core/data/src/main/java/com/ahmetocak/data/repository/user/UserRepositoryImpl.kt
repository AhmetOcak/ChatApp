package com.ahmetocak.data.repository.user

import com.ahmetocak.common.Response
import com.ahmetocak.common.mapResponse
import com.ahmetocak.data.mapper.toUser
import com.ahmetocak.data.mapper.toUserEntity
import com.ahmetocak.database.datasource.user.UserLocalDataSource
import com.ahmetocak.model.User
import com.ahmetocak.network.datasource.ktor_user.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun create(
        email: String,
        username: String,
        profilePicUrl: String?
    ): Response<User> {
        return userRemoteDataSource.create(
            email = email,
            username = username,
            profilePicUrl = profilePicUrl
        ).mapResponse { it.toUser() }
    }

    override suspend fun getUser(userEmail: String): Response<User> {
        return userRemoteDataSource.getById(userEmail).mapResponse { it.toUser() }
    }

    override suspend fun deleteUser(email: String): Response<Unit> {
        return userRemoteDataSource.deleteUser(email)
    }

    override suspend fun updateUser(
        email: String,
        username: String?,
        profilePicUrl: String?
    ): Response<Unit> {
        return userRemoteDataSource.updateUser(
            userEmail = email,
            username = username,
            profilePicUrl = profilePicUrl
        )
    }

    override suspend fun updateUserInCache(user: User): Response<Unit> {
        return userLocalDataSource.updateUser(user.toUserEntity())
    }

    override suspend fun clearAllUserData() = userLocalDataSource.clearAllTable()

    override suspend fun addUserToCache(user: User): Response<Unit> {
        return userLocalDataSource.addUser(user.toUserEntity())
    }

    override suspend fun observeUserInCache(): Response<Flow<User?>> {
        return userLocalDataSource.observeUser()
            .mapResponse { it.map { entity -> entity?.toUser() } }
    }

    override suspend fun deleteUserFromCache(user: User): Response<Unit> {
        return userLocalDataSource.deleteUser(user.toUserEntity())
    }
}