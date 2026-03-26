package com.afterlight.feature.party.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.afterlight.feature.party.presentation.CreatePartyScreen
import com.afterlight.feature.party.presentation.JoinPartyScreen
import com.afterlight.feature.party.presentation.PartyDetailScreen
import com.afterlight.feature.party.presentation.PartyListScreen

/**
 * Party navigation graph.
 * Stage 13: Party list, create, join, detail routes.
 * Camera/gallery routes handled at app level to break circular dependency.
 */
const val PARTY_GRAPH = "party_graph"
const val PARTY_LIST_ROUTE = "party_list"
const val CREATE_PARTY_ROUTE = "create_party"
const val JOIN_PARTY_ROUTE = "join_party"
const val PARTY_DETAIL_ROUTE = "party_detail/{partyId}"

fun NavGraphBuilder.partyNavGraph(navController: NavHostController) {
    navigation(
        startDestination = PARTY_LIST_ROUTE,
        route = PARTY_GRAPH
    ) {
        composable(PARTY_LIST_ROUTE) {
            PartyListScreen(
                onNavigateToCreateParty = {
                    navController.navigate(CREATE_PARTY_ROUTE)
                },
                onNavigateToJoinParty = {
                    navController.navigate(JOIN_PARTY_ROUTE)
                },
                onNavigateToPartyDetail = { partyId ->
                    navController.navigate("party_detail/$partyId")
                }
            )
        }
        
        composable(CREATE_PARTY_ROUTE) {
            CreatePartyScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(JOIN_PARTY_ROUTE) {
            JoinPartyScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = PARTY_DETAIL_ROUTE,
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getString("partyId") ?: return@composable
            
            PartyDetailScreen(
                partyId = partyId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCamera = { partyId ->
                    // Navigated from app level
                    navController.navigate("camera/$partyId")
                },
                onNavigateToGallery = { partyId ->
                    // Navigated from app level
                    navController.navigate("gallery/$partyId")
                }
            )
        }
    }
}
