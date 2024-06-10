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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

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
            modifier = Modifier.padding(start = AuthorImgHeight),
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
    ConstraintLayout {
        val (authorText, messageText, dateText) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(authorText) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                }
                .padding(end = 32.dp),
            text = author,
            style = authorTextStyle
        )

        Text(
            modifier = Modifier
                .constrainAs(messageText) {
                    top.linkTo(authorText.bottom, margin = 4.dp)
                    start.linkTo(parent.start)
                }
                .padding(horizontal = 8.dp),
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )

        Text(
            modifier = Modifier.constrainAs(dateText) {
                top.linkTo(messageText.bottom, margin = 2.dp)
                end.linkTo(parent.end, margin = 8.dp)
            }.padding(start = 32.dp),
            text = messageDate,
            style = dateTextStyle
        )
    }
}