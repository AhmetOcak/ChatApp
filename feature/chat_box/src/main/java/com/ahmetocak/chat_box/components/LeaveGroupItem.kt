package com.ahmetocak.chat_box.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ahmetocak.chat_box.ChatBoxUiEvent
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.ui.CHAT_ITEM_HEIGHT
import com.ahmetocak.designsystem.R.dimen as ChatAppDimens

@Composable
internal fun LeaveGroupItem(onEvent: (ChatBoxUiEvent) -> Unit) {
    Surface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {/*TODO*/ })
                .padding(dimensionResource(id = ChatAppDimens.padding_16))
                .height(CHAT_ITEM_HEIGHT),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimens.padding_16)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ChatAppIcons.Default.exitToApp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = "Leave the group",
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.error)
            )
        }
    }
}