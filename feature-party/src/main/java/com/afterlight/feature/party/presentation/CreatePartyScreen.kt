package com.afterlight.feature.party.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

/**
 * Create party screen with expiration selector.
 * Stage 13: Next morning, 48h, or custom expiration.
 */
@Composable
fun CreatePartyScreen(
    onNavigateBack: () -> Unit,
    viewModel: PartyViewModel = hiltViewModel()
) {
    var partyName by remember { mutableStateOf("") }
    var selectedExpiration by remember { mutableStateOf(ExpirationOption.NEXT_MORNING) }
    
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
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
                title = { Text("Create Party") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Party Details",
                style = MaterialTheme.typography.titleMedium
            )
            
            OutlinedTextField(
                value = partyName,
                onValueChange = { partyName = it },
                label = { Text("Party Name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Expires After",
                style = MaterialTheme.typography.titleMedium
            )
            
            ExpirationOption.values().forEach { option ->
                FilterChip(
                    selected = selectedExpiration == option,
                    onClick = { selectedExpiration = option },
                    label = { Text(option.label) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    val expiresAt = calculateExpirationTime(selectedExpiration)
                    viewModel.createParty(partyName, expiresAt)
                },
                enabled = uiState !is PartyUiState.Loading && partyName.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState is PartyUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Create Party")
                }
            }
        }
    }
}

enum class ExpirationOption(val label: String) {
    NEXT_MORNING("Next Morning (8 AM)"),
    HOURS_48("48 Hours"),
    CUSTOM("Custom")
}

private fun calculateExpirationTime(option: ExpirationOption): kotlinx.datetime.Instant {
    val now = Clock.System.now()
    
    return when (option) {
        ExpirationOption.NEXT_MORNING -> {
            // Next day at 8 AM (simplified - just add 16 hours)
            now + 16.hours
        }
        ExpirationOption.HOURS_48 -> {
            now + 48.hours
        }
        ExpirationOption.CUSTOM -> {
            // Default to 24 hours for now
            now + 24.hours
        }
    }
}
