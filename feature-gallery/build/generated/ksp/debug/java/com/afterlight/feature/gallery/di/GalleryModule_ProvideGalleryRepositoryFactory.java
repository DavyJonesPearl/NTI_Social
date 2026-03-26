package com.afterlight.feature.gallery.di;

import com.afterlight.core.security.SecurityManager;
import com.afterlight.data.local.dao.MediaDao;
import com.afterlight.feature.gallery.domain.GalleryRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
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
public final class GalleryModule_ProvideGalleryRepositoryFactory implements Factory<GalleryRepository> {
  private final Provider<SecurityManager> securityManagerProvider;

  private final Provider<MediaDao> mediaDaoProvider;

  public GalleryModule_ProvideGalleryRepositoryFactory(
      Provider<SecurityManager> securityManagerProvider, Provider<MediaDao> mediaDaoProvider) {
    this.securityManagerProvider = securityManagerProvider;
    this.mediaDaoProvider = mediaDaoProvider;
  }

  @Override
  public GalleryRepository get() {
    return provideGalleryRepository(securityManagerProvider.get(), mediaDaoProvider.get());
  }

  public static GalleryModule_ProvideGalleryRepositoryFactory create(
      javax.inject.Provider<SecurityManager> securityManagerProvider,
      javax.inject.Provider<MediaDao> mediaDaoProvider) {
    return new GalleryModule_ProvideGalleryRepositoryFactory(Providers.asDaggerProvider(securityManagerProvider), Providers.asDaggerProvider(mediaDaoProvider));
  }

  public static GalleryModule_ProvideGalleryRepositoryFactory create(
      Provider<SecurityManager> securityManagerProvider, Provider<MediaDao> mediaDaoProvider) {
    return new GalleryModule_ProvideGalleryRepositoryFactory(securityManagerProvider, mediaDaoProvider);
  }

  public static GalleryRepository provideGalleryRepository(SecurityManager securityManager,
      MediaDao mediaDao) {
    return Preconditions.checkNotNullFromProvides(GalleryModule.INSTANCE.provideGalleryRepository(securityManager, mediaDao));
  }
}
