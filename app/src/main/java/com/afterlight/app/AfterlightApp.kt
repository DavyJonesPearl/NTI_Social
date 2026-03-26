package com.afterlight.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.afterlight.feature.auth.domain.AuthState
import com.afterlight.feature.auth.navigation.AUTH_GRAPH
import com.afterlight.feature.auth.navigation.authNavGraph
import com.afterlight.feature.auth.presentation.AuthViewModel
import com.afterlight.feature.camera.presentation.CameraScreen
import com.afterlight.feature.gallery.presentation.GalleryScreen
import com.afterlight.feature.party.navigation.PARTY_GRAPH
import com.afterlight.feature.party.navigation.partyNavGraph

/**
 * Stage 13: Root Navigation Integration - AfterlightApp orchestrator.
 */
@Composable
fun AfterlightApp(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    // Observe authentication state
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    
    // Determine start destination based on auth state
    val startDestination = when (authState) {
        is AuthState.Authenticated -> PARTY_GRAPH
        else -> AUTH_GRAPH
    }
    
    // Handle authentication state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(PARTY_GRAPH) {
                    popUpTo(AUTH_GRAPH) { inclusive = true }
                    launchSingleTop = true
                }
            }
            is AuthState.Unauthenticated -> {
                if (navController.currentDestination?.route != AUTH_GRAPH) {
                    navController.navigate(AUTH_GRAPH) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            else -> {}
        }
    }
    
    // Root NavHost with auth gate
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authNavGraph(navController)
        partyNavGraph(navController)
        
        // Camera route (app-level)
        composable(
            route = "camera/{partyId}",
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
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
        
        // Gallery route (app-level)
        composable(
            route = "gallery/{partyId}",
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getString("partyId") ?: return@composable
            GalleryScreen(
                partyId = partyId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
