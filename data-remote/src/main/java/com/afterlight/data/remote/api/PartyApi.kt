package com.afterlight.data.remote.api

import com.afterlight.data.remote.dto.CreatePartyRequest
import com.afterlight.data.remote.dto.JoinPartyResponse
import com.afterlight.data.remote.dto.PartyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Party API interface.
 * Stage 13: Party lifecycle management endpoints.
 */
interface PartyApi {
    
    @POST("parties")
    suspend fun createParty(
        @Body request: CreatePartyRequest
    ): Response<PartyResponse>
    
    @POST("parties/{partyId}/join")
    suspend fun joinParty(
        @Path("partyId") partyId: String
    ): Response<JoinPartyResponse>
    
    @GET("parties")
    suspend fun listParties(): Response<List<PartyResponse>>
    
    @GET("parties/{partyId}")
    suspend fun getParty(
        @Path("partyId") partyId: String
    ): Response<PartyResponse>
    
    @DELETE("parties/{partyId}")
    suspend fun deleteParty(
        @Path("partyId") partyId: String
    ): Response<Unit>
}
