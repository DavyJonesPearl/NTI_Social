package com.afterlight.data.remote.di;

import com.afterlight.data.remote.api.PartyApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvidePartyApiFactory implements Factory<PartyApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvidePartyApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public PartyApi get() {
    return providePartyApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvidePartyApiFactory create(
      javax.inject.Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvidePartyApiFactory(Providers.asDaggerProvider(retrofitProvider));
  }

  public static NetworkModule_ProvidePartyApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvidePartyApiFactory(retrofitProvider);
  }

  public static PartyApi providePartyApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.providePartyApi(retrofit));
  }
}
