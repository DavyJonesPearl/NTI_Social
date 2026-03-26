package com.afterlight.core.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class WebSocketManager_Factory implements Factory<WebSocketManager> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public WebSocketManager_Factory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public WebSocketManager get() {
    return newInstance(okHttpClientProvider.get());
  }

  public static WebSocketManager_Factory create(
      javax.inject.Provider<OkHttpClient> okHttpClientProvider) {
    return new WebSocketManager_Factory(Providers.asDaggerProvider(okHttpClientProvider));
  }

  public static WebSocketManager_Factory create(Provider<OkHttpClient> okHttpClientProvider) {
    return new WebSocketManager_Factory(okHttpClientProvider);
  }

  public static WebSocketManager newInstance(OkHttpClient okHttpClient) {
    return new WebSocketManager(okHttpClient);
  }
}
