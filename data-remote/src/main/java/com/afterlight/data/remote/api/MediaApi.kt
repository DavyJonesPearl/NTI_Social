package com.afterlight.data.remote.api

import com.afterlight.data.remote.dto.MediaResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming

/**
 * Media API interface.
 * Stage 13: Encrypted media upload/download endpoints.
 */
interface MediaApi {
    
    @Multipart
    @POST("parties/{partyId}/media")
    suspend fun uploadMedia(
        @Path("partyId") partyId: String,
        @Part file: MultipartBody.Part
    ): Response<MediaResponse>
    
    @Streaming
    @GET("media/{mediaId}")
    suspend fun downloadMedia(
        @Path("mediaId") mediaId: String
    ): Response<ResponseBody>
    
    @GET("parties/{partyId}/media")
    suspend fun listMediaForParty(
        @Path("partyId") partyId: String
    ): Response<List<MediaResponse>>
    
    @DELETE("media/{mediaId}")
    suspend fun deleteMedia(
        @Path("mediaId") mediaId: String
    ): Response<Unit>
}
