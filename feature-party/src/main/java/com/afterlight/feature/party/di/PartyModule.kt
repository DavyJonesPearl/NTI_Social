package com.afterlight.feature.party.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for feature-party dependency injection.
 * Stage 13: PartyRepository and PartyExpirationScheduler are @Singleton constructor-injected.
 */
@Module
@InstallIn(SingletonComponent::class)
object PartyModule {
    // No provides needed - PartyRepository and PartyExpirationScheduler use @Inject constructor
}
