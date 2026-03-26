package com.afterlight.feature.gallery.domain;

import com.afterlight.core.security.SecurityManager;
import com.afterlight.data.local.dao.MediaDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class GalleryRepository_Factory implements Factory<GalleryRepository> {
  private final Provider<SecurityManager> securityManagerProvider;

  private final Provider<MediaDao> mediaDaoProvider;

  public GalleryRepository_Factory(Provider<SecurityManager> securityManagerProvider,
      Provider<MediaDao> mediaDaoProvider) {
    this.securityManagerProvider = securityManagerProvider;
    this.mediaDaoProvider = mediaDaoProvider;
  }

  @Override
  public GalleryRepository get() {
    return newInstance(securityManagerProvider.get(), mediaDaoProvider.get());
  }

  public static GalleryRepository_Factory create(
      javax.inject.Provider<SecurityManager> securityManagerProvider,
      javax.inject.Provider<MediaDao> mediaDaoProvider) {
    return new GalleryRepository_Factory(Providers.asDaggerProvider(securityManagerProvider), Providers.asDaggerProvider(mediaDaoProvider));
  }

  public static GalleryRepository_Factory create(Provider<SecurityManager> securityManagerProvider,
      Provider<MediaDao> mediaDaoProvider) {
    return new GalleryRepository_Factory(securityManagerProvider, mediaDaoProvider);
  }

  public static GalleryRepository newInstance(SecurityManager securityManager, MediaDao mediaDao) {
    return new GalleryRepository(securityManager, mediaDao);
  }
}
