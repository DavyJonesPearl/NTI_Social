package com.afterlight.feature.party.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PartyExpirationWorker_AssistedFactory_Impl implements PartyExpirationWorker_AssistedFactory {
  private final PartyExpirationWorker_Factory delegateFactory;

  PartyExpirationWorker_AssistedFactory_Impl(PartyExpirationWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public PartyExpirationWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<PartyExpirationWorker_AssistedFactory> create(
      PartyExpirationWorker_Factory delegateFactory) {
    return InstanceFactory.create(new PartyExpirationWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<PartyExpirationWorker_AssistedFactory> createFactoryProvider(
      PartyExpirationWorker_Factory delegateFactory) {
    return InstanceFactory.create(new PartyExpirationWorker_AssistedFactory_Impl(delegateFactory));
  }
}
