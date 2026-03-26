package com.afterlight.feature.camera.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.afterlight.feature.camera.presentation.CameraScreen

/**
 * Camera navigation graph.
 * Stage 13: camera/{partyId} route.
 */
const val CAMERA_ROUTE = "camera/{partyId}"

fun NavGraphBuilder.cameraNavGraph(navController: NavHostController) {
    composable(
        route = CAMERA_ROUTE,
        arguments = listOf(
            navArgument("partyId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val partyId = backStackEntry.arguments?.getString("partyId") ?: return@composable
        
        CameraScreen(
            partyId = partyId,
            onNavigateBack = { navController.popBackStack() },
            onNavigateToGallery = {
                navController.navigate("gallery/$partyId") {
                    popUpTo("party_detail/$partyId") { inclusive = false }
                }
            }
        )
    }
}
