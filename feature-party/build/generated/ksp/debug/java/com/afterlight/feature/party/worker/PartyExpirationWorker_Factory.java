package com.afterlight.feature.party.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.afterlight.core.security.SecurityManager;
import com.afterlight.data.local.dao.MediaDao;
import com.afterlight.data.local.dao.PartyDao;
import com.afterlight.data.remote.api.PartyApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class PartyExpirationWorker_Factory {
  private final Provider<PartyDao> partyDaoProvider;

  private final Provider<MediaDao> mediaDaoProvider;

  private final Provider<PartyApi> partyApiProvider;

  private final Provider<SecurityManager> securityManagerProvider;

  public PartyExpirationWorker_Factory(Provider<PartyDao> partyDaoProvider,
      Provider<MediaDao> mediaDaoProvider, Provider<PartyApi> partyApiProvider,
      Provider<SecurityManager> securityManagerProvider) {
    this.partyDaoProvider = partyDaoProvider;
    this.mediaDaoProvider = mediaDaoProvider;
    this.partyApiProvider = partyApiProvider;
    this.securityManagerProvider = securityManagerProvider;
  }

  public PartyExpirationWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, partyDaoProvider.get(), mediaDaoProvider.get(), partyApiProvider.get(), securityManagerProvider.get());
  }

  public static PartyExpirationWorker_Factory create(
      javax.inject.Provider<PartyDao> partyDaoProvider,
      javax.inject.Provider<MediaDao> mediaDaoProvider,
      javax.inject.Provider<PartyApi> partyApiProvider,
      javax.inject.Provider<SecurityManager> securityManagerProvider) {
    return new PartyExpirationWorker_Factory(Providers.asDaggerProvider(partyDaoProvider), Providers.asDaggerProvider(mediaDaoProvider), Providers.asDaggerProvider(partyApiProvider), Providers.asDaggerProvider(securityManagerProvider));
  }

  public static PartyExpirationWorker_Factory create(Provider<PartyDao> partyDaoProvider,
      Provider<MediaDao> mediaDaoProvider, Provider<PartyApi> partyApiProvider,
      Provider<SecurityManager> securityManagerProvider) {
    return new PartyExpirationWorker_Factory(partyDaoProvider, mediaDaoProvider, partyApiProvider, securityManagerProvider);
  }

  public static PartyExpirationWorker newInstance(Context context, WorkerParameters params,
      PartyDao partyDao, MediaDao mediaDao, PartyApi partyApi, SecurityManager securityManager) {
    return new PartyExpirationWorker(context, params, partyDao, mediaDao, partyApi, securityManager);
  }
}
