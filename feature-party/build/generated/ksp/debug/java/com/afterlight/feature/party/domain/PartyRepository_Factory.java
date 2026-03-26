package com.afterlight.feature.party.domain;

import com.afterlight.data.local.dao.PartyDao;
import com.afterlight.data.remote.api.PartyApi;
import com.afterlight.feature.party.worker.PartyExpirationScheduler;
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
public final class PartyRepository_Factory implements Factory<PartyRepository> {
  private final Provider<PartyApi> partyApiProvider;

  private final Provider<PartyDao> partyDaoProvider;

  private final Provider<PartyExpirationScheduler> expirationSchedulerProvider;

  public PartyRepository_Factory(Provider<PartyApi> partyApiProvider,
      Provider<PartyDao> partyDaoProvider,
      Provider<PartyExpirationScheduler> expirationSchedulerProvider) {
    this.partyApiProvider = partyApiProvider;
    this.partyDaoProvider = partyDaoProvider;
    this.expirationSchedulerProvider = expirationSchedulerProvider;
  }

  @Override
  public PartyRepository get() {
    return newInstance(partyApiProvider.get(), partyDaoProvider.get(), expirationSchedulerProvider.get());
  }

  public static PartyRepository_Factory create(javax.inject.Provider<PartyApi> partyApiProvider,
      javax.inject.Provider<PartyDao> partyDaoProvider,
      javax.inject.Provider<PartyExpirationScheduler> expirationSchedulerProvider) {
    return new PartyRepository_Factory(Providers.asDaggerProvider(partyApiProvider), Providers.asDaggerProvider(partyDaoProvider), Providers.asDaggerProvider(expirationSchedulerProvider));
  }

  public static PartyRepository_Factory create(Provider<PartyApi> partyApiProvider,
      Provider<PartyDao> partyDaoProvider,
      Provider<PartyExpirationScheduler> expirationSchedulerProvider) {
    return new PartyRepository_Factory(partyApiProvider, partyDaoProvider, expirationSchedulerProvider);
  }

  public static PartyRepository newInstance(PartyApi partyApi, PartyDao partyDao,
      PartyExpirationScheduler expirationScheduler) {
    return new PartyRepository(partyApi, partyDao, expirationScheduler);
  }
}
