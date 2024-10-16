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

internal object KtorChatGroupEndPoints {
    const val CREATE_PRIVATE_GROUP = "/chatGroup/createPrivateGroup"
    const val CREATE_GROUP = "/chatGroup/createGroup"
    const val ADD_PARTICIPANT = "/chatGroup/addParticipant"
    const val GET = "/chatGroup/getGroups/{${Path.USER_EMAIL}}"
    const val UPDATE_GROUP_IMG = "/chatGroup/updateGroupImage"

    internal object Path {
        const val USER_EMAIL = "userEmail"
    }
}

internal object KtorMessagesEndPoints {
    const val GET = "/messages/getMessages/{${Paths.MESSAGE_BOX_ID}}/{${Paths.PAGE}}"
    const val POST = "/messages/sendMessage"
    const val GET_MEDIA_MESS = "/messages/getAllMediaMessages/{${Paths.MESSAGE_BOX_ID}}"

    internal object Paths {
        const val MESSAGE_BOX_ID = "messageBoxId"
        const val PAGE = "page"
    }
}

internal object KtorFcmTokenEndPoints {
    const val POST = "/token/add"
}