package com.afterlight.data.local.di;

import com.afterlight.data.local.AfterLightDatabase;
import com.afterlight.data.local.dao.MediaDao;
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
public final class DatabaseModule_ProvideMediaDaoFactory implements Factory<MediaDao> {
  private final Provider<AfterLightDatabase> databaseProvider;

  public DatabaseModule_ProvideMediaDaoFactory(Provider<AfterLightDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MediaDao get() {
    return provideMediaDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMediaDaoFactory create(
      javax.inject.Provider<AfterLightDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMediaDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideMediaDaoFactory create(
      Provider<AfterLightDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMediaDaoFactory(databaseProvider);
  }

  public static MediaDao provideMediaDao(AfterLightDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMediaDao(database));
  }
}
