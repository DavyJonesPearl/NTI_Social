package com.afterlight.core.security.di

import com.afterlight.core.security.SecurityManager
import com.afterlight.core.security.SecurityManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for core-security dependency injection.
 * Stage 13: SecurityManager as singleton.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {
    
    @Binds
    @Singleton
    abstract fun bindSecurityManager(
        impl: SecurityManagerImpl
    ): SecurityManager
}
