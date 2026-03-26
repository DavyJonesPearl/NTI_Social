package com.afterlight.feature.gallery.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.afterlight.feature.gallery.presentation.GalleryScreen

/**
 * Gallery navigation graph.
 * Stage 13: gallery/{partyId} route.
 */
const val GALLERY_ROUTE = "gallery/{partyId}"

fun NavGraphBuilder.galleryNavGraph(navController: NavHostController) {
    composable(
        route = GALLERY_ROUTE,
        arguments = listOf(
            navArgument("partyId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val partyId = backStackEntry.arguments?.getString("partyId") ?: return@composable
        
        GalleryScreen(
            partyId = partyId,
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
