package com.ahmetocak.ui.chat_bubble

import android.net.Uri
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ahmetocak.designsystem.components.ChatAppAnimatedIconButton
import com.ahmetocak.designsystem.icons.ChatAppIcons

@Composable
fun ComingChatBubbleAudioItem(
    author: String,
    time: String,
    audioUrl: Uri,
    authorImgUrl: String?,
    isAudioPlaying: Boolean,
    onPlayClick: (Uri) -> Unit
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
            AudioBubbleSkeleton(
                author = author,
                messageDate = time,
                isAudioPlaying = isAudioPlaying,
                onPlayClick = remember { { onPlayClick(audioUrl) } }
            )
        }
    }
}

@Composable
fun OngoingChatBubbleAudioItem(
    author: String,
    time: String,
    audioUrl: Uri,
    isAudioPlaying: Boolean,
    onPlayClick: (Uri) -> Unit
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
                AudioBubbleSkeleton(
                    author = author,
                    messageDate = time,
                    isAudioPlaying = isAudioPlaying,
                    onPlayClick = remember { { onPlayClick(audioUrl) } }
                )
            }
        }
    }
}

@Composable
private fun AudioBubbleSkeleton(
    author: String,
    messageDate: String,
    isAudioPlaying: Boolean,
    onPlayClick: () -> Unit
) {
    ConstraintLayout {
        val (authorText, dateText, playBtn, lineAnim) = createRefs()

        Text(
            modifier = Modifier.constrainAs(authorText) {
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(parent.start, margin = 8.dp)
            }.padding(end = 32.dp),
            text = author,
            style = MaterialTheme.typography.titleMedium
        )

        ChatAppAnimatedIconButton(
            modifier = Modifier.constrainAs(playBtn) {
                top.linkTo(authorText.bottom, margin = 8.dp)
                start.linkTo(parent.start, margin = 8.dp)
            },
            onClick = onPlayClick,
            imageVector = ChatAppIcons.Filled.play,
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
            imageVector2 = ChatAppIcons.Filled.stop,
            animationCondition = !isAudioPlaying
        )

        if (isAudioPlaying) {
            ElectricLine(
                modifier = Modifier
                    .width(96.dp)
                    .constrainAs(lineAnim) {
                        top.linkTo(playBtn.top)
                        bottom.linkTo(playBtn.bottom)
                        start.linkTo(playBtn.end, margin = 4.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                    }
            )
        } else {
            HorizontalDivider(modifier = Modifier.width(96.dp))
        }

        Text(
            modifier = Modifier.constrainAs(dateText) {
                top.linkTo(playBtn.bottom, margin = 2.dp)
                start.linkTo(authorText.end, margin = 32.dp)
                end.linkTo(parent.end, margin = 8.dp)
            },
            text = messageDate,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ElectricLine(modifier: Modifier) {
    val transition = rememberInfiniteTransition(label = "playing")
    val color by transition.animateColor(
        initialValue = Color(0xFF04D9FF),
        targetValue = Color(0xFF02788D),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "colorAnimation"
    )

    HorizontalDivider(
        modifier = modifier,
        color = color,
        thickness = 2.dp
    )
}