package com.afterlight.data.local.di;

import com.afterlight.data.local.AfterLightDatabase;
import com.afterlight.data.local.dao.FaceDao;
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
public final class DatabaseModule_ProvideFaceDaoFactory implements Factory<FaceDao> {
  private final Provider<AfterLightDatabase> databaseProvider;

  public DatabaseModule_ProvideFaceDaoFactory(Provider<AfterLightDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public FaceDao get() {
    return provideFaceDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideFaceDaoFactory create(
      javax.inject.Provider<AfterLightDatabase> databaseProvider) {
    return new DatabaseModule_ProvideFaceDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideFaceDaoFactory create(
      Provider<AfterLightDatabase> databaseProvider) {
    return new DatabaseModule_ProvideFaceDaoFactory(databaseProvider);
  }

  public static FaceDao provideFaceDao(AfterLightDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideFaceDao(database));
  }
}
