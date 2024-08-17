package com.ahmetocak.network.datasource.messages

import com.ahmetocak.common.Response
import com.ahmetocak.network.model.NetworkMessage

interface MessagesRemoteDataSource {
    suspend fun getAllMediaMessages(messageBoxId: Int, ): Response<List<NetworkMessage>>
}