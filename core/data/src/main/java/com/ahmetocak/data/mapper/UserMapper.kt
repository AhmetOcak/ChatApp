package com.ahmetocak.data.mapper

import com.ahmetocak.database.entity.UserEntity
import com.ahmetocak.model.User
import com.ahmetocak.network.model.NetworkUser

internal fun NetworkUser.toUser(): User {
    return User(
        id = id,
        username = username,
        email = email,
        profilePicUrl = profilePicUrl
    )
}

internal fun UserEntity.toUser(): User {
    return User(
        id = id,
        username = username,
        email = email,
        profilePicUrl = profilePicUrl
    )
}

internal fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        email = email,
        profilePicUrl = profilePicUrl
    )
}