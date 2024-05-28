package com.ahmetocak.data.mapper

import com.ahmetocak.database.entity.FriendEntity
import com.ahmetocak.model.Friend
import com.ahmetocak.network.model.NetworkFriend

internal fun NetworkFriend.toFriend(): Friend {
    return Friend(
        id = id,
        currentUserEmail = userEmail,
        friendEmail = friendEmail,
        friendProfilePicUrl = friendProfilePicUrl
    )
}

internal fun List<NetworkFriend>.toFriend(): List<Friend> {
    return this.map { it.toFriend() }
}

internal fun Friend.toFriendEntity(): FriendEntity {
    return FriendEntity(
        id = id,
        userEmail = currentUserEmail,
        friendEmail = friendEmail,
        friendProfilePicUrl = friendProfilePicUrl
    )
}

internal fun FriendEntity.toFriend(): Friend {
    return Friend(
        id = id,
        currentUserEmail = userEmail,
        friendEmail = friendEmail,
        friendProfilePicUrl = friendProfilePicUrl
    )
}

internal fun List<FriendEntity>.toListFriend(): List<Friend> {
    return this.map { it.toFriend() }
}