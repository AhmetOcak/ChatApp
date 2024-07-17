package com.ahmetocak.network.api

import com.ahmetocak.network.helper.KtorChatGroupEndPoints
import com.ahmetocak.network.helper.KtorFcmTokenEndPoints
import com.ahmetocak.network.helper.KtorMessagesEndPoints
import com.ahmetocak.network.helper.KtorUserEndpoints
import com.ahmetocak.network.model.NetworkChatGroup
import com.ahmetocak.network.model.NetworkMessage
import com.ahmetocak.network.model.NetworkUser
import com.ahmetocak.network.model.PaginatedNetworkMessage
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface KtorChatApi {

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

    @FormUrlEncoded
    @POST(KtorChatGroupEndPoints.CREATE_PRIVATE_GROUP)
    suspend fun createPrivateGroup(
        @Field("currentUserEmail") currentUserEmail: String,
        @Field("friendEmail") friendEmail: String
    ): NetworkChatGroup

    @FormUrlEncoded
    @POST(KtorChatGroupEndPoints.CREATE_GROUP)
    suspend fun createGroup(
        @Field("creatorEmail") creatorEmail: String,
        @Field("name") groupName: String,
        @Field("creatorUsername") creatorUsername: String,
        @Field("creatorProfilePicUrl") creatorProfilePicUrl: String?,
        @Field("imageUrl") groupImageUrl: String?,
    ): NetworkChatGroup

    @GET(KtorChatGroupEndPoints.GET)
    suspend fun getGroups(
        @Path(KtorChatGroupEndPoints.Path.USER_EMAIL) userEmail: String
    ): List<NetworkChatGroup>

    @FormUrlEncoded
    @PUT(KtorChatGroupEndPoints.ADD_PARTICIPANT)
    suspend fun addParticipantToChatGroup(
        @Field("groupId") groupId: Int,
        @Field("participantEmail") participantEmail: String,
        @Field("participantUsername") participantUsername: String,
        @Field("participantProfilePicUrl") participantProfilePicUrl: String?
    )

    @GET(KtorMessagesEndPoints.GET)
    suspend fun getMessages(
        @Path(KtorMessagesEndPoints.Paths.FRIENDSHIP_ID) friendShipId: Int,
        @Path(KtorMessagesEndPoints.Paths.PAGE) page: Int
    ): PaginatedNetworkMessage

    @FormUrlEncoded
    @POST(KtorFcmTokenEndPoints.POST)
    suspend fun uploadToken(
        @Field("email") email: String,
        @Field("token") token: String
    )

    @POST(KtorMessagesEndPoints.POST)
    suspend fun sendMessage(
        @Body message: NetworkMessage
    ): NetworkMessage
}