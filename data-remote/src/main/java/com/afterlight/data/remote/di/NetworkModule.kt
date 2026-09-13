package com.afterlight.data.remote.di

import com.afterlight.data.remote.api.AuthApi
import com.afterlight.data.remote.api.MediaApi
import com.afterlight.data.remote.api.PartyApi
import com.afterlight.data.remote.firebase.FirebaseAuthService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt module for data-remote dependency injection.
 * Stage 13: API interface providers using Retrofit from core-network + Firebase
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    /**
     * Provides Firebase Authentication instance
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    
    /**
     * Provides Firebase Firestore instance
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    
    /**
     * Provides Firebase Functions instance
     */
    @Provides
    @Singleton
    fun provideFirebaseFunctions(): com.google.firebase.functions.FirebaseFunctions = 
        com.google.firebase.functions.FirebaseFunctions.getInstance()
        
    /**
     * Provides Firebase Storage instance
     */
    @Provides
    @Singleton
    fun provideFirebaseStorage(): com.google.firebase.storage.FirebaseStorage = 
        com.google.firebase.storage.FirebaseStorage.getInstance()
    
    /**
     * Provides singleton AuthApi instance.
     */
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
    
    /**
     * Provides singleton PartyApi instance.
     */
    @Provides
    @Singleton
    fun providePartyApi(retrofit: Retrofit): PartyApi {
        return retrofit.create(PartyApi::class.java)
    }
    
    /**
     * Provides singleton MediaApi instance.
     */
    @Provides
    @Singleton
    fun provideMediaApi(retrofit: Retrofit): MediaApi {
        return retrofit.create(MediaApi::class.java)
    }
}
