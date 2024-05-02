package com.ahmetocak.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    authorImgUrl: String?,
    isAuthorSame: Boolean
) {
    if (isAuthorSame) {
        Card(
            modifier = Modifier.padding(start = AuthorImgHeight),
            shape = RoundedCornerShape(size = 48f)
        ) {
            BubbleSkeleton(
                author = author,
                message = message,
                messageDate = time,
                isAuthorSame = true
            )
        }
    } else {
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
                    messageDate = time,
                    isAuthorSame = false
                )
            }
        }
    }
}

@Composable
fun OngoingChatBubbleItem(
    author: String,
    message: String,
    time: String,
    seenByList: List<String?>,
    isAuthorSame: Boolean
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        ConstraintLayout {
            val (chatBubble, seenByImages) = createRefs()

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
                    messageDate = time,
                    isAuthorSame = isAuthorSame
                )
            }
            Row(
                modifier = Modifier.constrainAs(seenByImages) {
                    top.linkTo(chatBubble.bottom, margin = 1.dp)
                    end.linkTo(parent.end)
                }
            ) {
                seenByList.forEach { imgUrl ->
                    AuthorImage(authorImgUrl = imgUrl)
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
        }
    }
}

@Composable
private fun BubbleSkeleton(
    author: String,
    message: String,
    messageDate: String,
    isAuthorSame: Boolean
) {
    ConstraintLayout {
        val (authorText, messageText, dateText) = createRefs()

        if (!isAuthorSame) {
            Text(
                modifier = Modifier.constrainAs(authorText) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                },
                text = author,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            modifier = Modifier.constrainAs(messageText) {
                top.linkTo(if (!isAuthorSame) authorText.bottom else parent.top, margin = 4.dp)
                start.linkTo(parent.start, margin = 8.dp)
            },
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.constrainAs(dateText) {
                top.linkTo(messageText.bottom, margin = 2.dp)
                start.linkTo(messageText.end, margin = 16.dp)
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
                message = "This is a test message.",
                time = "13:18",
                authorImgUrl = null,
                isAuthorSame = false
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
                seenByList = listOf(null, null),
                isAuthorSame = false
            )
        }
    }
}