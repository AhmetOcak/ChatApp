package com.ahmetocak.chat_box.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ahmetocak.chat_box.ChatBoxUiEvent
import com.ahmetocak.chat_box.R
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.ui.CHAT_ITEM_HEIGHT
import com.ahmetocak.designsystem.R.dimen as ChatAppDimens

@Composable
internal fun AddParticipantItem(onEvent: (ChatBoxUiEvent) -> Unit) {
    Surface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onEvent(ChatBoxUiEvent.ShowAddParticipantView) })
                .padding(dimensionResource(id = ChatAppDimens.padding_16))
                .height(CHAT_ITEM_HEIGHT),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimens.padding_16))
        ) {
            Icon(
                imageVector = ChatAppIcons.Filled.addPerson,
                contentDescription = null
            )
            Text(text = stringResource(id = R.string.add_participant))
        }
    }
}