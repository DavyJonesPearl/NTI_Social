package com.afterlight.core.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import kotlinx.datetime.Instant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Factory for creating Retrofit instances with custom configuration.
 * Stage 13: Gson converter with kotlinx.datetime.Instant adapter.
 */
@Singleton
class RetrofitFactory @Inject constructor() {
    
    /**
     * Creates a Retrofit instance with Gson converter and kotlinx.datetime support.
     * 
     * @param okHttpClient Configured OkHttpClient
     * @param baseUrl API base URL
     */
    fun createRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        val gson = createGson()
        
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    /**
     * Creates Gson instance with custom adapters for kotlinx.datetime types.
     */
    private fun createGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Instant::class.java, InstantSerializer())
            .registerTypeAdapter(Instant::class.java, InstantDeserializer())
            .create()
    }
    
    /**
     * Serializes kotlinx.datetime.Instant to ISO-8601 string.
     */
    private class InstantSerializer : JsonSerializer<Instant> {
        override fun serialize(
            src: Instant?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonPrimitive? {
            return src?.let { JsonPrimitive(it.toString()) }
        }
    }
    
    /**
     * Deserializes ISO-8601 string to kotlinx.datetime.Instant.
     */
    private class InstantDeserializer : JsonDeserializer<Instant> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Instant? {
            return json?.asString?.let { Instant.parse(it) }
        }
    }
}
