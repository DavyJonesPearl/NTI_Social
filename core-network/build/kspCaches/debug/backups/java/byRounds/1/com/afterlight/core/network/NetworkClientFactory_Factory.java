package com.afterlight.core.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class NetworkClientFactory_Factory implements Factory<NetworkClientFactory> {
  @Override
  public NetworkClientFactory get() {
    return newInstance();
  }

  public static NetworkClientFactory_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NetworkClientFactory newInstance() {
    return new NetworkClientFactory();
  }

  private static final class InstanceHolder {
    static final NetworkClientFactory_Factory INSTANCE = new NetworkClientFactory_Factory();
  }
}
