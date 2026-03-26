package com.afterlight.feature.auth.domain;

import com.afterlight.data.local.dao.FaceDao;
import com.afterlight.data.local.dao.MediaDao;
import com.afterlight.data.local.dao.PartyDao;
import com.afterlight.data.local.dao.SyncStateDao;
import com.afterlight.data.local.dao.UserDao;
import com.afterlight.data.remote.firebase.FirebaseAuthService;
import com.afterlight.feature.auth.data.SecureTokenProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<FirebaseAuthService> firebaseAuthServiceProvider;

  private final Provider<SecureTokenProvider> tokenProvider;

  private final Provider<UserDao> userDaoProvider;

  private final Provider<PartyDao> partyDaoProvider;

  private final Provider<MediaDao> mediaDaoProvider;

  private final Provider<FaceDao> faceDaoProvider;

  private final Provider<SyncStateDao> syncStateDaoProvider;

  public AuthRepository_Factory(Provider<FirebaseAuthService> firebaseAuthServiceProvider,
      Provider<SecureTokenProvider> tokenProvider, Provider<UserDao> userDaoProvider,
      Provider<PartyDao> partyDaoProvider, Provider<MediaDao> mediaDaoProvider,
      Provider<FaceDao> faceDaoProvider, Provider<SyncStateDao> syncStateDaoProvider) {
    this.firebaseAuthServiceProvider = firebaseAuthServiceProvider;
    this.tokenProvider = tokenProvider;
    this.userDaoProvider = userDaoProvider;
    this.partyDaoProvider = partyDaoProvider;
    this.mediaDaoProvider = mediaDaoProvider;
    this.faceDaoProvider = faceDaoProvider;
    this.syncStateDaoProvider = syncStateDaoProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(firebaseAuthServiceProvider.get(), tokenProvider.get(), userDaoProvider.get(), partyDaoProvider.get(), mediaDaoProvider.get(), faceDaoProvider.get(), syncStateDaoProvider.get());
  }

  public static AuthRepository_Factory create(
      javax.inject.Provider<FirebaseAuthService> firebaseAuthServiceProvider,
      javax.inject.Provider<SecureTokenProvider> tokenProvider,
      javax.inject.Provider<UserDao> userDaoProvider,
      javax.inject.Provider<PartyDao> partyDaoProvider,
      javax.inject.Provider<MediaDao> mediaDaoProvider,
      javax.inject.Provider<FaceDao> faceDaoProvider,
      javax.inject.Provider<SyncStateDao> syncStateDaoProvider) {
    return new AuthRepository_Factory(Providers.asDaggerProvider(firebaseAuthServiceProvider), Providers.asDaggerProvider(tokenProvider), Providers.asDaggerProvider(userDaoProvider), Providers.asDaggerProvider(partyDaoProvider), Providers.asDaggerProvider(mediaDaoProvider), Providers.asDaggerProvider(faceDaoProvider), Providers.asDaggerProvider(syncStateDaoProvider));
  }

  public static AuthRepository_Factory create(
      Provider<FirebaseAuthService> firebaseAuthServiceProvider,
      Provider<SecureTokenProvider> tokenProvider, Provider<UserDao> userDaoProvider,
      Provider<PartyDao> partyDaoProvider, Provider<MediaDao> mediaDaoProvider,
      Provider<FaceDao> faceDaoProvider, Provider<SyncStateDao> syncStateDaoProvider) {
    return new AuthRepository_Factory(firebaseAuthServiceProvider, tokenProvider, userDaoProvider, partyDaoProvider, mediaDaoProvider, faceDaoProvider, syncStateDaoProvider);
  }

  public static AuthRepository newInstance(FirebaseAuthService firebaseAuthService,
      SecureTokenProvider tokenProvider, UserDao userDao, PartyDao partyDao, MediaDao mediaDao,
      FaceDao faceDao, SyncStateDao syncStateDao) {
    return new AuthRepository(firebaseAuthService, tokenProvider, userDao, partyDao, mediaDao, faceDao, syncStateDao);
  }
}
