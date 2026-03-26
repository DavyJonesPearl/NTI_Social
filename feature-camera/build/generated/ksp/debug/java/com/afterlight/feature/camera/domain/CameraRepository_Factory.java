package com.afterlight.feature.camera.domain;

import android.content.Context;
import com.afterlight.core.security.SecurityManager;
import com.afterlight.data.local.dao.MediaDao;
import com.afterlight.data.remote.api.MediaApi;
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
public final class CameraRepository_Factory implements Factory<CameraRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<SecurityManager> securityManagerProvider;

  private final Provider<MediaDao> mediaDaoProvider;

  private final Provider<MediaApi> mediaApiProvider;

  public CameraRepository_Factory(Provider<Context> contextProvider,
      Provider<SecurityManager> securityManagerProvider, Provider<MediaDao> mediaDaoProvider,
      Provider<MediaApi> mediaApiProvider) {
    this.contextProvider = contextProvider;
    this.securityManagerProvider = securityManagerProvider;
    this.mediaDaoProvider = mediaDaoProvider;
    this.mediaApiProvider = mediaApiProvider;
  }

  @Override
  public CameraRepository get() {
    return newInstance(contextProvider.get(), securityManagerProvider.get(), mediaDaoProvider.get(), mediaApiProvider.get());
  }

  public static CameraRepository_Factory create(javax.inject.Provider<Context> contextProvider,
      javax.inject.Provider<SecurityManager> securityManagerProvider,
      javax.inject.Provider<MediaDao> mediaDaoProvider,
      javax.inject.Provider<MediaApi> mediaApiProvider) {
    return new CameraRepository_Factory(Providers.asDaggerProvider(contextProvider), Providers.asDaggerProvider(securityManagerProvider), Providers.asDaggerProvider(mediaDaoProvider), Providers.asDaggerProvider(mediaApiProvider));
  }

  public static CameraRepository_Factory create(Provider<Context> contextProvider,
      Provider<SecurityManager> securityManagerProvider, Provider<MediaDao> mediaDaoProvider,
      Provider<MediaApi> mediaApiProvider) {
    return new CameraRepository_Factory(contextProvider, securityManagerProvider, mediaDaoProvider, mediaApiProvider);
  }

  public static CameraRepository newInstance(Context context, SecurityManager securityManager,
      MediaDao mediaDao, MediaApi mediaApi) {
    return new CameraRepository(context, securityManager, mediaDao, mediaApi);
  }
}
