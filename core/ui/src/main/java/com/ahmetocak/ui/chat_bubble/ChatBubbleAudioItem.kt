package com.ahmetocak.ui.chat_bubble

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.ahmetocak.designsystem.components.ChatAppAnimatedIconButton
import com.ahmetocak.designsystem.components.LightDarkPreview
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.ui.R
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.ahmetocak.designsystem.R.dimen as ChatAppDimens

@Composable
fun ChatBubbleAudioItem(
    author: String,
    time: String,
    audioUrl: Uri,
    authorImgUrl: String?,
    isAudioPlaying: Boolean,
    onPlayClick: (Uri) -> Unit,
    isComingFromMe: Boolean
) {
    if (isComingFromMe) {
        OngoingChatBubbleAudioItem(
            author = author,
            time = time,
            audioUrl = audioUrl,
            isAudioPlaying = isAudioPlaying,
            onPlayClick = onPlayClick
        )
    } else {
        ComingChatBubbleAudioItem(
            author = author,
            time = time,
            audioUrl = audioUrl,
            authorImgUrl = authorImgUrl,
            isAudioPlaying = isAudioPlaying,
            onPlayClick = onPlayClick
        )
    }
}

@Composable
private fun ComingChatBubbleAudioItem(
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
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.author_img_height)),
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
private fun OngoingChatBubbleAudioItem(
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
    val padding2 = dimensionResource(id = ChatAppDimens.padding_2)
    val padding8 = dimensionResource(id = ChatAppDimens.padding_8)
    val padding32 = dimensionResource(id = ChatAppDimens.padding_32)

    ConstraintLayout(modifier = Modifier.width(IntrinsicSize.Max)) {
        val (authorText, dateText, playBtn, lottieAnim) = createRefs()

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

        ChatAppAnimatedIconButton(
            modifier = Modifier.constrainAs(playBtn) {
                top.linkTo(authorText.bottom, margin = padding8)
                start.linkTo(parent.start, margin = padding8)
            },
            onClick = onPlayClick,
            imageVector = ChatAppIcons.Filled.play,
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
            imageVector2 = ChatAppIcons.Filled.stop,
            animationCondition = !isAudioPlaying
        )

        LottieAudioWaveAnimation(
            modifier = Modifier
                .constrainAs(lottieAnim) {
                    top.linkTo(authorText.bottom, margin = padding8)
                    bottom.linkTo(dateText.top, margin = padding8)
                    start.linkTo(playBtn.end, margin = padding32)
                    end.linkTo(parent.end, margin = padding32)
                }
                .requiredHeight(padding32)
                .padding(horizontal = padding32),
            isAudioPlaying = isAudioPlaying
        )

        Text(
            modifier = Modifier.constrainAs(dateText) {
                top.linkTo(playBtn.bottom, margin = padding2)
                end.linkTo(parent.end, margin = padding8)
            },
            text = messageDate,
            style = dateTextStyle
        )
    }
}

@Composable
private fun LottieAudioWaveAnimation(modifier: Modifier, isAudioPlaying: Boolean) {
    val disabledDynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color.Gray.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf("**")
        )
    )
    val enabledDynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                MaterialTheme.colorScheme.primary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf("**")
        )
    )

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.audio_wave_anim))

    LottieAnimation(
        modifier = modifier,
        composition = composition,
        dynamicProperties = if (isAudioPlaying) enabledDynamicProperties else disabledDynamicProperties,
        iterations = LottieConstants.IterateForever,
        isPlaying = isAudioPlaying,
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth
    )
}

@LightDarkPreview
@Composable
private fun PreviewComingChatBubbleAudioItem() {
    ChatAppTheme {
        Surface {
            ComingChatBubbleAudioItem(
                author = "Author",
                time = "12:00",
                audioUrl = Uri.parse(""),
                authorImgUrl = null,
                isAudioPlaying = false,
                onPlayClick = {}
            )
        }
    }
}