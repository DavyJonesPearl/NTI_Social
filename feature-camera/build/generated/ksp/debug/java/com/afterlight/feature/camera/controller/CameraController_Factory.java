package com.afterlight.feature.camera.controller;

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
public final class CameraController_Factory implements Factory<CameraController> {
  @Override
  public CameraController get() {
    return newInstance();
  }

  public static CameraController_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CameraController newInstance() {
    return new CameraController();
  }

  private static final class InstanceHolder {
    static final CameraController_Factory INSTANCE = new CameraController_Factory();
  }
}
