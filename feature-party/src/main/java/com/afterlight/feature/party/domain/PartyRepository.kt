package com.afterlight.feature.party.domain

import com.afterlight.data.local.model.PartyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

/**
 * Interface for Party repository operations.
 */
interface PartyRepository {
    suspend fun createParty(name: String, expiresAt: Instant): Result<PartyEntity>
    suspend fun joinParty(partyId: String): Result<PartyEntity>
    fun fetchUserParties(): Flow<List<PartyEntity>>
    suspend fun deleteParty(partyId: String): Result<Unit>
    fun getPartyById(partyId: String): Flow<PartyEntity?>
}
