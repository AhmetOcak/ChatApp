package com.ahmetocak.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ahmetocak.designsystem.components.NetworkImage
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.designsystem.R.drawable as AppResources

private val AuthorImgHeight = 16.dp

private val imgModifier = Modifier
    .height(AuthorImgHeight)
    .clip(CircleShape)
    .aspectRatio(1f)

@Composable
fun ComingChatBubbleItem(
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
fun OngoingChatBubbleItem(
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
            modifier = Modifier.constrainAs(authorText) {
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(parent.start, margin = 8.dp)
            },
            text = author,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            modifier = Modifier.constrainAs(messageText) {
                top.linkTo(authorText.bottom, margin = 4.dp)
                start.linkTo(parent.start)
            }.padding(horizontal = 8.dp),
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )

        Text(
            modifier = Modifier.constrainAs(dateText) {
                top.linkTo(messageText.bottom, margin = 2.dp)
                end.linkTo(parent.end, margin = 8.dp)
            },
            text = messageDate,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun AuthorImage(authorImgUrl: String?) {
    authorImgUrl?.let {
        NetworkImage(
            imageUrl = it,
            modifier = imgModifier
        )
    } ?: run {
        Icon(
            painter = painterResource(id = AppResources.blank_profile),
            contentDescription = null,
            modifier = imgModifier
        )
    }
}

@Preview
@Composable
fun PreviewComingChatBalloonItem() {
    Surface {
        ChatAppTheme {
            ComingChatBubbleItem(
                author = "Darth Vader",
                message = "This is a test messsdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaage.",
                time = "13:18",
                authorImgUrl = null
            )
        }
    }
}

@Preview
@Composable
fun PreviewOngoingChatBalloonItem() {
    Surface {
        ChatAppTheme {
            OngoingChatBubbleItem(
                author = "Ahmet Ocak",
                message = "This is a test message.",
                time = "13:17",
            )
        }
    }
}