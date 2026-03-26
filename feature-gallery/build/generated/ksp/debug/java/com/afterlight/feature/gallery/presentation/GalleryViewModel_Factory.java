package com.afterlight.feature.gallery.presentation;

import com.afterlight.feature.gallery.domain.GalleryRepository;
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
public final class GalleryViewModel_Factory implements Factory<GalleryViewModel> {
  private final Provider<GalleryRepository> repositoryProvider;

  public GalleryViewModel_Factory(Provider<GalleryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GalleryViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static GalleryViewModel_Factory create(
      javax.inject.Provider<GalleryRepository> repositoryProvider) {
    return new GalleryViewModel_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static GalleryViewModel_Factory create(Provider<GalleryRepository> repositoryProvider) {
    return new GalleryViewModel_Factory(repositoryProvider);
  }

  public static GalleryViewModel newInstance(GalleryRepository repository) {
    return new GalleryViewModel(repository);
  }
}
