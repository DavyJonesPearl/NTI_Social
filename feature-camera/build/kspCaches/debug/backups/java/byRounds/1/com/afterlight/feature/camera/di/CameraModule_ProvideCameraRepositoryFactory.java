package com.afterlight.feature.camera.di;

import android.content.Context;
import com.afterlight.core.security.SecurityManager;
import com.afterlight.data.local.dao.MediaDao;
import com.afterlight.data.remote.api.MediaApi;
import com.afterlight.feature.camera.domain.CameraRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class CameraModule_ProvideCameraRepositoryFactory implements Factory<CameraRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<SecurityManager> securityManagerProvider;

  private final Provider<MediaDao> mediaDaoProvider;

  private final Provider<MediaApi> mediaApiProvider;

  public CameraModule_ProvideCameraRepositoryFactory(Provider<Context> contextProvider,
      Provider<SecurityManager> securityManagerProvider, Provider<MediaDao> mediaDaoProvider,
      Provider<MediaApi> mediaApiProvider) {
    this.contextProvider = contextProvider;
    this.securityManagerProvider = securityManagerProvider;
    this.mediaDaoProvider = mediaDaoProvider;
    this.mediaApiProvider = mediaApiProvider;
  }

  @Override
  public CameraRepository get() {
    return provideCameraRepository(contextProvider.get(), securityManagerProvider.get(), mediaDaoProvider.get(), mediaApiProvider.get());
  }

  public static CameraModule_ProvideCameraRepositoryFactory create(
      javax.inject.Provider<Context> contextProvider,
      javax.inject.Provider<SecurityManager> securityManagerProvider,
      javax.inject.Provider<MediaDao> mediaDaoProvider,
      javax.inject.Provider<MediaApi> mediaApiProvider) {
    return new CameraModule_ProvideCameraRepositoryFactory(Providers.asDaggerProvider(contextProvider), Providers.asDaggerProvider(securityManagerProvider), Providers.asDaggerProvider(mediaDaoProvider), Providers.asDaggerProvider(mediaApiProvider));
  }

  public static CameraModule_ProvideCameraRepositoryFactory create(
      Provider<Context> contextProvider, Provider<SecurityManager> securityManagerProvider,
      Provider<MediaDao> mediaDaoProvider, Provider<MediaApi> mediaApiProvider) {
    return new CameraModule_ProvideCameraRepositoryFactory(contextProvider, securityManagerProvider, mediaDaoProvider, mediaApiProvider);
  }

  public static CameraRepository provideCameraRepository(Context context,
      SecurityManager securityManager, MediaDao mediaDao, MediaApi mediaApi) {
    return Preconditions.checkNotNullFromProvides(CameraModule.INSTANCE.provideCameraRepository(context, securityManager, mediaDao, mediaApi));
  }
}
