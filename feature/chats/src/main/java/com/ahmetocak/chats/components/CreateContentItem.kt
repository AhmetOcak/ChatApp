package com.ahmetocak.chats.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.ahmetocak.designsystem.R.dimen as ChatAppDimens

@Composable
internal fun CreateContentItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = ChatAppDimens.padding_8),
                    horizontal = dimensionResource(id = ChatAppDimens.padding_16)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimens.padding_16))
        ) {
            IconButton(
                onClick = onClick,
                enabled = false,
                colors = IconButtonDefaults.iconButtonColors(
                    disabledContainerColor = FloatingActionButtonDefaults.containerColor,
                    disabledContentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(imageVector = icon, contentDescription = null)
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}