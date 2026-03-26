package com.afterlight.feature.auth.di

import com.afterlight.core.network.TokenProvider
import com.afterlight.feature.auth.data.SecureTokenProvider
import com.afterlight.feature.auth.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for feature-auth dependency injection.
 * Stage 13: TokenProvider and AuthRepository singletons.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    
    @Binds
    @Singleton
    abstract fun bindTokenProvider(
        impl: SecureTokenProvider
    ): TokenProvider
}
