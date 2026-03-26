package com.afterlight.feature.camera.presentation;

import com.afterlight.feature.camera.domain.CameraRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class CameraViewModel_Factory implements Factory<CameraViewModel> {
  private final Provider<CameraRepository> repositoryProvider;

  public CameraViewModel_Factory(Provider<CameraRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CameraViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static CameraViewModel_Factory create(
      javax.inject.Provider<CameraRepository> repositoryProvider) {
    return new CameraViewModel_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static CameraViewModel_Factory create(Provider<CameraRepository> repositoryProvider) {
    return new CameraViewModel_Factory(repositoryProvider);
  }

  public static CameraViewModel newInstance(CameraRepository repository) {
    return new CameraViewModel(repository);
  }
}
