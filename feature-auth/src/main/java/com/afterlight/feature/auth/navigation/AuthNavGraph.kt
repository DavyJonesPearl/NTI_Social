package com.afterlight.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.afterlight.feature.auth.presentation.LoginScreen
import com.afterlight.feature.auth.presentation.RegisterScreen

/**
 * Authentication navigation graph.
 * Stage 13: Login and register routes.
 */
const val AUTH_GRAPH = "auth_graph"
const val LOGIN_ROUTE = "login"
const val REGISTER_ROUTE = "register"

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        startDestination = LOGIN_ROUTE,
        route = AUTH_GRAPH
    ) {
        composable(LOGIN_ROUTE) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(REGISTER_ROUTE)
                }
            )
        }
        
        composable(REGISTER_ROUTE) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}
