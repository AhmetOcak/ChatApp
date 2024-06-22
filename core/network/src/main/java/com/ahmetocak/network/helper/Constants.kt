package com.ahmetocak.network.helper

internal const val BASE_URL = "http://10.0.2.2:8080"

internal object KtorUserEndpoints {
    const val GET = "/user/getUser/{${Paths.USER_EMAIL}}"
    const val CREATE = "/user/createUser"
    const val DELETE = "/user/deleteUser/{${Paths.USER_EMAIL}}"
    const val UPDATE = "/user/updateUser/{${Paths.USER_EMAIL}}"

    internal object Paths {
        const val USER_EMAIL = "userEmail"
    }
}

internal object KtorFriendEndPoints {
    const val GET = "/friend/getFriends/{${Paths.USER_EMAIL}}"
    const val POST = "/friend/create"

    internal object Paths {
        const val USER_EMAIL = "userEmail"
    }
}

internal object KtorMessagesEndPoints {
    const val GET = "/messages/getMessages/{${Paths.FRIENDSHIP_ID}}/{${Paths.PAGE}}"
    const val POST = "/messages/sendMessage"

    internal object Paths {
        const val FRIENDSHIP_ID = "friendshipId"
        const val PAGE = "page"
    }
}

internal object KtorFcmTokenEndPoints {
    const val POST = "/token/add"
}