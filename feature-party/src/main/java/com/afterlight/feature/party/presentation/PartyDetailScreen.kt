package com.afterlight.feature.party.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.milliseconds

/**
 * Party detail screen with camera/gallery actions.
 * Stage 13: Party info, take photo, view gallery, delete confirmation.
 */
@Composable
fun PartyDetailScreen(
    partyId: String,
    onNavigateBack: () -> Unit,
    onNavigateToCamera: (String) -> Unit,
    onNavigateToGallery: (String) -> Unit,
    viewModel: PartyViewModel = hiltViewModel()
) {
    val partyRepository = viewModel // Access via DI if needed
    var party by remember { mutableStateOf<com.afterlight.data.local.model.PartyEntity?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(partyId) {
        // Note: Would fetch party from repository here
        // For now, simplified
    }
    
    LaunchedEffect(uiState) {
        when (uiState) {
            is PartyUiState.Success -> onNavigateBack()
            is PartyUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as PartyUiState.Error).message)
                viewModel.clearUiState()
            }
            else -> {}
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(party?.name ?: "Party") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, "Delete Party")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Time Remaining",
                        style = MaterialTheme.typography.labelLarge
                    )
                    
                    Text(
                        text = timeRemaining.ifBlank { "Loading..." },
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = { onNavigateToCamera(partyId) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Take Photo")
            }
            
            OutlinedButton(
                onClick = { onNavigateToGallery(partyId) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Gallery")
            }
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Party?") },
            text = { Text("This will delete the party and all its photos. This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteParty(partyId)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
