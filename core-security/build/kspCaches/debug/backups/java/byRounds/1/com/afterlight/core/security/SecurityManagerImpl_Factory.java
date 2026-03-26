package com.afterlight.core.security;

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
public final class SecurityManagerImpl_Factory implements Factory<SecurityManagerImpl> {
  private final Provider<Context> contextProvider;

  public SecurityManagerImpl_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SecurityManagerImpl get() {
    return newInstance(contextProvider.get());
  }

  public static SecurityManagerImpl_Factory create(javax.inject.Provider<Context> contextProvider) {
    return new SecurityManagerImpl_Factory(Providers.asDaggerProvider(contextProvider));
  }

  public static SecurityManagerImpl_Factory create(Provider<Context> contextProvider) {
    return new SecurityManagerImpl_Factory(contextProvider);
  }

  public static SecurityManagerImpl newInstance(Context context) {
    return new SecurityManagerImpl(context);
  }
}
