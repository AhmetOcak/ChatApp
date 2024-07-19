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
import androidx.compose.ui.unit.dp
import com.ahmetocak.chat_box.ChatBoxUiEvent
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.ui.CHAT_ITEM_HEIGHT

@Composable
internal fun AddParticipantItem(onEvent: (ChatBoxUiEvent) -> Unit) {
    Surface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onEvent(ChatBoxUiEvent.ShowAddParticipantView) })
                .padding(16.dp)
                .height(CHAT_ITEM_HEIGHT),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = ChatAppIcons.Filled.addPerson,
                contentDescription = null
            )
            Text(text = "Add Participant")
        }
    }
}