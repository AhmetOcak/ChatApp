package com.ahmetocak.network.datasource.ktor_message

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahmetocak.network.api.KtorChatApi
import com.ahmetocak.network.model.NetworkMessage

class MessagesPagingSource(
    private val api: KtorChatApi,
    private val userEmail: String,
    private val friendEmail: String
) : PagingSource<Int, NetworkMessage>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkMessage> {
        return try {
            val currentPageNumber = params.key ?: 0
            val response = api.getMessages(
                page = currentPageNumber,
                userEmail = userEmail,
                friendEmail = friendEmail
            )
            LoadResult.Page(
                data = response.messageList,
                prevKey = null,
                nextKey = if (currentPageNumber < response.totalPages) currentPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkMessage>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}