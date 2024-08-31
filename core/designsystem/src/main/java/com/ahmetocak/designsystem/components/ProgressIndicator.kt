package com.ahmetocak.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.zIndex
import com.ahmetocak.designsystem.R

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
fun ChatAppProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(2f),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ChatAppPlaceableProgressIndicator(modifier: Modifier, alignment: Alignment = Alignment.Center) {
    Box(
        modifier = modifier,
        contentAlignment = alignment
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ButtonCircularProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(dimensionResource(id = R.dimen.circular_prog_ind_size)),
        strokeWidth = dimensionResource(id = R.dimen.padding_2)
    )
}