package com.afterlight.feature.party.presentation;

import com.afterlight.feature.party.domain.PartyRepository;
import com.afterlight.feature.party.worker.PartyExpirationScheduler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class PartyViewModel_Factory implements Factory<PartyViewModel> {
  private final Provider<PartyRepository> partyRepositoryProvider;

  private final Provider<PartyExpirationScheduler> expirationSchedulerProvider;

  public PartyViewModel_Factory(Provider<PartyRepository> partyRepositoryProvider,
      Provider<PartyExpirationScheduler> expirationSchedulerProvider) {
    this.partyRepositoryProvider = partyRepositoryProvider;
    this.expirationSchedulerProvider = expirationSchedulerProvider;
  }

  @Override
  public PartyViewModel get() {
    return newInstance(partyRepositoryProvider.get(), expirationSchedulerProvider.get());
  }

  public static PartyViewModel_Factory create(
      javax.inject.Provider<PartyRepository> partyRepositoryProvider,
      javax.inject.Provider<PartyExpirationScheduler> expirationSchedulerProvider) {
    return new PartyViewModel_Factory(Providers.asDaggerProvider(partyRepositoryProvider), Providers.asDaggerProvider(expirationSchedulerProvider));
  }

  public static PartyViewModel_Factory create(Provider<PartyRepository> partyRepositoryProvider,
      Provider<PartyExpirationScheduler> expirationSchedulerProvider) {
    return new PartyViewModel_Factory(partyRepositoryProvider, expirationSchedulerProvider);
  }

  public static PartyViewModel newInstance(PartyRepository partyRepository,
      PartyExpirationScheduler expirationScheduler) {
    return new PartyViewModel(partyRepository, expirationScheduler);
  }
}
