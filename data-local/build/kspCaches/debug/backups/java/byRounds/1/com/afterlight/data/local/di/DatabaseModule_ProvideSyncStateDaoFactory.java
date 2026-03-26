package com.afterlight.data.local.di;

import com.afterlight.data.local.AfterLightDatabase;
import com.afterlight.data.local.dao.SyncStateDao;
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
public final class DatabaseModule_ProvideSyncStateDaoFactory implements Factory<SyncStateDao> {
  private final Provider<AfterLightDatabase> databaseProvider;

  public DatabaseModule_ProvideSyncStateDaoFactory(Provider<AfterLightDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SyncStateDao get() {
    return provideSyncStateDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSyncStateDaoFactory create(
      javax.inject.Provider<AfterLightDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSyncStateDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideSyncStateDaoFactory create(
      Provider<AfterLightDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSyncStateDaoFactory(databaseProvider);
  }

  public static SyncStateDao provideSyncStateDao(AfterLightDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSyncStateDao(database));
  }
}
