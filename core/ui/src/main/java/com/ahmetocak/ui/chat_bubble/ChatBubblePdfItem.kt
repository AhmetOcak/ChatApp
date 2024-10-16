package com.ahmetocak.ui.chat_bubble

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.FileProvider
import com.ahmetocak.ui.R
import com.ahmetocak.ui.chat_bubble.doc_manager.downloadDocumentToPermanentStorage
import com.ahmetocak.ui.chat_bubble.doc_manager.renderPdfToBitmap
import java.io.File
import com.ahmetocak.designsystem.R.dimen as ChatAppDimens

@Composable
fun ChatBubblePdfItem(
    author: String,
    pdfUrl: String,
    time: String,
    authorImgUrl: String?,
    onClick: (Uri) -> Unit,
    isComingFromMe: Boolean
) {
    if (isComingFromMe) {
        OngoingChatBubblePdfItem(
            author = author,
            pdfUrl = pdfUrl,
            time = time,
            onClick = onClick
        )
    } else {
        ComingChatBubblePdfItem(
            author = author,
            pdfUrl = pdfUrl,
            time = time,
            authorImgUrl = authorImgUrl,
            onClick = onClick
        )
    }
}

@Composable
private fun ComingChatBubblePdfItem(
    author: String,
    pdfUrl: String,
    time: String,
    authorImgUrl: String?,
    onClick: (Uri) -> Unit
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
                pdfUrl = pdfUrl,
                messageDate = time,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun OngoingChatBubblePdfItem(
    author: String,
    pdfUrl: String,
    time: String,
    onClick: (Uri) -> Unit
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
                    pdfUrl = pdfUrl,
                    messageDate = time,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
private fun BubbleSkeleton(
    author: String,
    pdfUrl: String,
    messageDate: String,
    onClick: (Uri) -> Unit
) {
    val context = LocalContext.current
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(key1 = pdfUri) {
        val fileName = "$author-$messageDate"
        val file = File(context.cacheDir, "$fileName.pdf")
        pdfUri = if (file.exists()) FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        ) else downloadDocumentToPermanentStorage(context, pdfUrl, fileName)
        pdfUri?.let {
            bitmap = renderPdfToBitmap(context, it)
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = ChatAppDimens.padding_2))
            .width(IntrinsicSize.Max)
            .clickable(
                enabled = pdfUri != null,
                onClick = remember { { pdfUri?.let { onClick(it) } } })
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = dimensionResource(id = ChatAppDimens.padding_8),
                    end = dimensionResource(id = ChatAppDimens.padding_32)
                )
                .padding(vertical = dimensionResource(id = ChatAppDimens.padding_8)),
            text = author,
            style = authorTextStyle
        )
        bitmap?.let {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5))
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.background(Color.White)
                )
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = ChatAppDimens.padding_4))
                .padding(
                    start = dimensionResource(id = ChatAppDimens.padding_32),
                    end = dimensionResource(id = ChatAppDimens.padding_8)
                ),
            text = messageDate,
            style = dateTextStyle,
            textAlign = TextAlign.End
        )
    }
}