package com.ahmetocak.ui.chat_bubble

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import com.ahmetocak.ui.R
import com.ahmetocak.designsystem.R.dimen as ChatAppDimens

@Composable
fun ChatBubbleTextItem(
    author: String,
    message: String,
    time: String,
    authorImgUrl: String?,
    isComingFromMe: Boolean
) {
    if (isComingFromMe) {
        OngoingChatBubbleTextItem(author = author, message = message, time = time)
    } else {
        ComingChatBubbleTextItem(
            author = author,
            message = message,
            time = time,
            authorImgUrl = authorImgUrl
        )
    }
}

@Composable
private fun ComingChatBubbleTextItem(
    author: String,
    message: String,
    time: String,
    authorImgUrl: String?
) {
    Column {
        AuthorImage(authorImgUrl = authorImgUrl)
        Card(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.author_img_height)),
            shape = RoundedCornerShape(
                topStart = 0f,
                topEnd = 48f,
                bottomStart = 48f,
                bottomEnd = 48f
            )
        ) {
            BubbleSkeleton(
                author = author,
                message = message,
                messageDate = time
            )
        }
    }
}

@Composable
private fun OngoingChatBubbleTextItem(
    author: String,
    message: String,
    time: String
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        ConstraintLayout {
            val (chatBubble) = createRefs()

            Card(
                modifier = Modifier.constrainAs(chatBubble) {},
                shape = RoundedCornerShape(
                    topStart = 48f,
                    topEnd = 0f,
                    bottomStart = 48f,
                    bottomEnd = 48f
                )
            ) {
                BubbleSkeleton(
                    author = author,
                    message = message,
                    messageDate = time
                )
            }
        }
    }
}

@Composable
private fun BubbleSkeleton(
    author: String,
    message: String,
    messageDate: String
) {
    val padding2 = dimensionResource(id = ChatAppDimens.padding_2)
    val padding4 = dimensionResource(id = ChatAppDimens.padding_4)
    val padding8 = dimensionResource(id = ChatAppDimens.padding_8)
    val padding32 = dimensionResource(id = ChatAppDimens.padding_32)


    ConstraintLayout {
        val (authorText, messageText, dateText) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(authorText) {
                    top.linkTo(parent.top, margin = padding8)
                    start.linkTo(parent.start, margin = padding8)
                }
                .padding(end = padding32),
            text = author,
            style = authorTextStyle
        )

        Text(
            modifier = Modifier
                .constrainAs(messageText) {
                    top.linkTo(authorText.bottom, margin = padding4)
                    start.linkTo(parent.start)
                }
                .padding(horizontal = padding8),
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )

        Text(
            modifier = Modifier.constrainAs(dateText) {
                top.linkTo(messageText.bottom, margin = padding2)
                end.linkTo(parent.end, margin = padding8)
            }.padding(start = padding32),
            text = messageDate,
            style = dateTextStyle
        )
    }
}