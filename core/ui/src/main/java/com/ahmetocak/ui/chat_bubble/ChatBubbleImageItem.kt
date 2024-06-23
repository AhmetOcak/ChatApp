package com.ahmetocak.ui.chat_bubble

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ahmetocak.designsystem.components.NetworkImage

@Composable
fun ChatBubbleImageItem(
    author: String,
    imageUrl: String,
    time: String,
    authorImgUrl: String?,
    isComingFromMe: Boolean,
    onClick: (String) -> Unit
) {
    if (isComingFromMe) {
        OngoingChatBubbleImageItem(
            author = author,
            imageUrl = imageUrl,
            time = time,
            onClick = remember { { onClick(imageUrl) } })
    } else {
        ComingChatBubbleImageItem(
            author = author,
            imageUrl = imageUrl,
            time = time,
            authorImgUrl = authorImgUrl,
            onClick = remember { { onClick(imageUrl) } }
        )
    }
}

@Composable
private fun ComingChatBubbleImageItem(
    author: String,
    imageUrl: String,
    time: String,
    authorImgUrl: String?,
    onClick: () -> Unit
) {
    Column {
        AuthorImage(authorImgUrl = authorImgUrl)
        Card(
            modifier = Modifier.padding(start = AuthorImgHeight, end = 32.dp),
            shape = RoundedCornerShape(
                topStart = 0f,
                topEnd = 48f,
                bottomStart = 48f,
                bottomEnd = 48f
            ),
            onClick = onClick
        ) {
            BubbleSkeleton(
                author = author,
                imageUrl = imageUrl,
                messageDate = time
            )
        }
    }
}

@Composable
private fun OngoingChatBubbleImageItem(
    author: String,
    imageUrl: String,
    time: String,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        ConstraintLayout {
            val (chatBubble) = createRefs()

            Card(
                modifier = Modifier
                    .padding(start = 32.dp)
                    .constrainAs(chatBubble) {},
                shape = RoundedCornerShape(
                    topStart = 48f,
                    topEnd = 0f,
                    bottomStart = 48f,
                    bottomEnd = 48f
                ),
                onClick = onClick
            ) {
                BubbleSkeleton(
                    author = author,
                    imageUrl = imageUrl,
                    messageDate = time
                )
            }
        }
    }
}

@Composable
private fun BubbleSkeleton(
    author: String,
    imageUrl: String,
    messageDate: String
) {
    Column(modifier = Modifier.width(IntrinsicSize.Max)) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp, end = 32.dp)
                .padding(vertical = 8.dp),
            text = author,
            style = authorTextStyle
        )
        NetworkImage(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(0.dp, LocalConfiguration.current.screenHeightDp.dp / 2)
                .padding(horizontal = 2.dp)
                .clip(RoundedCornerShape(5)),
            imageUrl = imageUrl
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .padding(start = 32.dp, end = 8.dp),
            text = messageDate,
            style = dateTextStyle,
            textAlign = TextAlign.End
        )
    }
}