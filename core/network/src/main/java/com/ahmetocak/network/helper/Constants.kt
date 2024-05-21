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