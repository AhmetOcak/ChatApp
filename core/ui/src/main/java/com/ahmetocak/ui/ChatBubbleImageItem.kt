package com.ahmetocak.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ahmetocak.designsystem.components.NetworkImage

@Composable
fun ComingChatBubbleImageItem(
    author: String,
    imageUrl: String,
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
                imageUrl = imageUrl,
                messageDate = time
            )
        }
    }
}

@Composable
fun OngoingChatBubbleImageItem(
    author: String,
    imageUrl: String,
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
            style = MaterialTheme.typography.titleMedium
        )
        NetworkImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
                .clip(RoundedCornerShape(5)),
            imageUrl = imageUrl
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp),
            text = messageDate,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End
        )
    }
}