package com.ahmetocak.stories.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.stories.StoriesRoute

const val STORIES_ROUTE = "stories_route"

fun NavController.navigateToStories(navOptions: NavOptions) = navigate(STORIES_ROUTE, navOptions)

fun NavGraphBuilder.storiesScreen(onNavigateToUserDetail: (String) -> Unit) {
    composable(STORIES_ROUTE) {
        StoriesRoute(onNavigateToUserDetail = onNavigateToUserDetail)
    }
}