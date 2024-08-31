package com.ahmetocak.chat_box.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import com.ahmetocak.chat_box.R
import com.ahmetocak.designsystem.components.FilledChatAppIconButton
import com.ahmetocak.designsystem.R.dimen as ChatAppDimens

@Composable
internal fun AttachItem(onClick: () -> Unit, imageVector: ImageVector, title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimens.padding_4))
    ) {
        FilledChatAppIconButton(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.attach_item_btn_size))
                .aspectRatio(1f),
            onClick = onClick,
            imageVector = imageVector
        )
        Text(text = title)
    }
}