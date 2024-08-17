package com.ahmetocak.network.datasource.messages

import com.ahmetocak.common.Response
import com.ahmetocak.network.api.KtorChatApi
import com.ahmetocak.network.helper.apiCall
import com.ahmetocak.network.model.NetworkMessage
import javax.inject.Inject

class MessagesRemoteDataSourceImpl @Inject constructor(
    private val api: KtorChatApi
): MessagesRemoteDataSource {
    override suspend fun getAllMediaMessages(messageBoxId: Int): Response<List<NetworkMessage>> {
        return apiCall { api.getAllMediaMessages(messageBoxId) }
    }
}