package com.afterlight.core.network.di;

import com.afterlight.core.network.NetworkClientFactory;
import com.afterlight.core.network.TokenProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;

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
public final class NetworkModule_ProvideOkHttpClientFactory implements Factory<OkHttpClient> {
  private final Provider<NetworkClientFactory> networkClientFactoryProvider;

  private final Provider<TokenProvider> tokenProvider;

  public NetworkModule_ProvideOkHttpClientFactory(
      Provider<NetworkClientFactory> networkClientFactoryProvider,
      Provider<TokenProvider> tokenProvider) {
    this.networkClientFactoryProvider = networkClientFactoryProvider;
    this.tokenProvider = tokenProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideOkHttpClient(networkClientFactoryProvider.get(), tokenProvider.get());
  }

  public static NetworkModule_ProvideOkHttpClientFactory create(
      javax.inject.Provider<NetworkClientFactory> networkClientFactoryProvider,
      javax.inject.Provider<TokenProvider> tokenProvider) {
    return new NetworkModule_ProvideOkHttpClientFactory(Providers.asDaggerProvider(networkClientFactoryProvider), Providers.asDaggerProvider(tokenProvider));
  }

  public static NetworkModule_ProvideOkHttpClientFactory create(
      Provider<NetworkClientFactory> networkClientFactoryProvider,
      Provider<TokenProvider> tokenProvider) {
    return new NetworkModule_ProvideOkHttpClientFactory(networkClientFactoryProvider, tokenProvider);
  }

  public static OkHttpClient provideOkHttpClient(NetworkClientFactory networkClientFactory,
      TokenProvider tokenProvider) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideOkHttpClient(networkClientFactory, tokenProvider));
  }
}
