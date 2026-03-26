package com.afterlight.feature.camera.di;

import com.afterlight.feature.camera.controller.CameraController;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class CameraModule_ProvideCameraControllerFactory implements Factory<CameraController> {
  @Override
  public CameraController get() {
    return provideCameraController();
  }

  public static CameraModule_ProvideCameraControllerFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CameraController provideCameraController() {
    return Preconditions.checkNotNullFromProvides(CameraModule.INSTANCE.provideCameraController());
  }

  private static final class InstanceHolder {
    static final CameraModule_ProvideCameraControllerFactory INSTANCE = new CameraModule_ProvideCameraControllerFactory();
  }
}
