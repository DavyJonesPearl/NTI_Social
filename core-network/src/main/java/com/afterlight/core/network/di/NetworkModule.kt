package com.afterlight.core.network.di

import android.content.Context
import android.content.pm.ApplicationInfo
import com.afterlight.core.network.NetworkClientFactory
import com.afterlight.core.network.RetrofitFactory
import com.afterlight.core.network.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt module for core-network dependency injection.
 * Stage 13: Singleton Retrofit client.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    private const val BASE_URL = "https://api.afterlight.app/" // Placeholder
    
    /**
     * Provides singleton OkHttpClient.
     * TokenProvider is optional (null until feature-auth provides implementation).
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        networkClientFactory: NetworkClientFactory,
        tokenProvider: TokenProvider?
    ): OkHttpClient {
        val isDebuggable = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        return networkClientFactory.createOkHttpClient(
            tokenProvider = tokenProvider,
            enableLogging = isDebuggable
        )
    }
    
    /**
     * Provides singleton Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        retrofitFactory: RetrofitFactory
    ): Retrofit {
        return retrofitFactory.createRetrofit(okHttpClient, BASE_URL)
    }
}
