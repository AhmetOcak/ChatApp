package com.ahmetocak.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex

@Composable
fun ChatAppGreyProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(2f)
            .background(Color(0x80000000)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ChatAppProgressIndicator(modifier: Modifier = Modifier, visible: Boolean) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(2f),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = visible) {
            CircularProgressIndicator()
        }
    }
}