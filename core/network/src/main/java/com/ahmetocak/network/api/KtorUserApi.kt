package com.ahmetocak.network.api

import com.ahmetocak.network.helper.KtorUserEndpoints
import com.ahmetocak.network.model.NetworkUser
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface KtorUserApi {

    @FormUrlEncoded
    @POST(KtorUserEndpoints.CREATE)
    suspend fun createUser(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("profilePicUrl") profilePicUrl: String?
    ): NetworkUser

    @GET(KtorUserEndpoints.GET)
    suspend fun getUser(
        @Path(KtorUserEndpoints.Paths.USER_EMAIL) userEmail: String
    ): NetworkUser

    @DELETE(KtorUserEndpoints.DELETE)
    suspend fun deleteUser(
        @Path(KtorUserEndpoints.Paths.USER_EMAIL) userEmail: String
    )

    @FormUrlEncoded
    @PUT(KtorUserEndpoints.UPDATE)
    suspend fun updateUser(
        @Path(KtorUserEndpoints.Paths.USER_EMAIL) userEmail: String,
        @Field("username") username: String?,
        @Field("profilePicUrl") profilePicUrl: String?
    )
}