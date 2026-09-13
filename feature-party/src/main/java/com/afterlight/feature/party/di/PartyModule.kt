package com.afterlight.feature.party.di

import com.afterlight.feature.party.data.FirebasePartyRepository
import com.afterlight.feature.party.domain.PartyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for feature-party dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class PartyModule {
    
    @Binds
    @Singleton
    abstract fun bindPartyRepository(
        firebasePartyRepository: FirebasePartyRepository
    ): PartyRepository
}
