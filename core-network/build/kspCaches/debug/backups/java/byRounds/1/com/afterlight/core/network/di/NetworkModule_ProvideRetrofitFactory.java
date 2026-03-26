package com.afterlight.core.network.di;

import com.afterlight.core.network.RetrofitFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;
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
public final class NetworkModule_ProvideRetrofitFactory implements Factory<Retrofit> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  private final Provider<RetrofitFactory> retrofitFactoryProvider;

  public NetworkModule_ProvideRetrofitFactory(Provider<OkHttpClient> okHttpClientProvider,
      Provider<RetrofitFactory> retrofitFactoryProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
    this.retrofitFactoryProvider = retrofitFactoryProvider;
  }

  @Override
  public Retrofit get() {
    return provideRetrofit(okHttpClientProvider.get(), retrofitFactoryProvider.get());
  }

  public static NetworkModule_ProvideRetrofitFactory create(
      javax.inject.Provider<OkHttpClient> okHttpClientProvider,
      javax.inject.Provider<RetrofitFactory> retrofitFactoryProvider) {
    return new NetworkModule_ProvideRetrofitFactory(Providers.asDaggerProvider(okHttpClientProvider), Providers.asDaggerProvider(retrofitFactoryProvider));
  }

  public static NetworkModule_ProvideRetrofitFactory create(
      Provider<OkHttpClient> okHttpClientProvider,
      Provider<RetrofitFactory> retrofitFactoryProvider) {
    return new NetworkModule_ProvideRetrofitFactory(okHttpClientProvider, retrofitFactoryProvider);
  }

  public static Retrofit provideRetrofit(OkHttpClient okHttpClient,
      RetrofitFactory retrofitFactory) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideRetrofit(okHttpClient, retrofitFactory));
  }
}
