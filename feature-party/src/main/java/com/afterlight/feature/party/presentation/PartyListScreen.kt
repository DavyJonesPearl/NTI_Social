package com.afterlight.feature.party.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.afterlight.data.local.model.PartyEntity
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.milliseconds

/**
 * Party list screen with countdown badges.
 * Stage 13: Active parties with expiration countdown.
 */
@Composable
fun PartyListScreen(
    onNavigateToCreateParty: () -> Unit,
    onNavigateToJoinParty: () -> Unit,
    onNavigateToPartyDetail: (String) -> Unit,
    viewModel: PartyViewModel = hiltViewModel()
) {
    val parties by viewModel.parties.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(uiState) {
        if (uiState is PartyUiState.Error) {
            snackbarHostState.showSnackbar((uiState as PartyUiState.Error).message)
            viewModel.clearUiState()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Parties") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreateParty) {
                Icon(Icons.Default.Add, contentDescription = "Create Party")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (parties.isEmpty()) {
            EmptyPartyState(
                onCreateParty = onNavigateToCreateParty,
                onJoinParty = onNavigateToJoinParty,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Button(
                        onClick = onNavigateToJoinParty,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Join Party")
                    }
                }
                
                items(parties, key = { it.id }) { party ->
                    PartyCard(
                        party = party,
                        onClick = { onNavigateToPartyDetail(party.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PartyCard(
    party: PartyEntity,
    onClick: () -> Unit
) {
    var timeRemaining by remember { mutableStateOf(calculateTimeRemaining(party.expiresAt)) }
    
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            timeRemaining = calculateTimeRemaining(party.expiresAt)
        }
    }
    
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = party.name,
                    style = MaterialTheme.typography.titleLarge
                )
                
                AssistChip(
                    onClick = { },
                    label = { Text(timeRemaining) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when {
                            timeRemaining.contains("Expired") -> MaterialTheme.colorScheme.errorContainer
                            timeRemaining.contains("h") -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.tertiaryContainer
                        }
                    )
                )
            }
        }
    }
}

@Composable
fun EmptyPartyState(
    onCreateParty: () -> Unit,
    onJoinParty: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No Active Parties",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Create a party or join one to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Button(
            onClick = onCreateParty,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Party")
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedButton(
            onClick = onJoinParty,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Join Party")
        }
    }
}

private fun calculateTimeRemaining(expiresAt: kotlinx.datetime.Instant): String {
    val now = Clock.System.now()
    val remaining = (expiresAt - now).inWholeMilliseconds.milliseconds
    
    if (remaining.isNegative()) {
        return "Expired"
    }
    
    val hours = remaining.inWholeHours
    val minutes = (remaining.inWholeMinutes % 60)
    
    return when {
        hours > 24 -> "${hours / 24}d ${hours % 24}h"
        hours > 0 -> "${hours}h ${minutes}m"
        else -> "${minutes}m"
    }
}
