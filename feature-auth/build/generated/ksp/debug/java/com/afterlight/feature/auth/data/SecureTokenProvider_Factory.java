package com.afterlight.feature.auth.data;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class SecureTokenProvider_Factory implements Factory<SecureTokenProvider> {
  private final Provider<Context> contextProvider;

  public SecureTokenProvider_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SecureTokenProvider get() {
    return newInstance(contextProvider.get());
  }

  public static SecureTokenProvider_Factory create(javax.inject.Provider<Context> contextProvider) {
    return new SecureTokenProvider_Factory(Providers.asDaggerProvider(contextProvider));
  }

  public static SecureTokenProvider_Factory create(Provider<Context> contextProvider) {
    return new SecureTokenProvider_Factory(contextProvider);
  }

  public static SecureTokenProvider newInstance(Context context) {
    return new SecureTokenProvider(context);
  }
}
