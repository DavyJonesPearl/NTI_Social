package com.afterlight.data.local.di;

import com.afterlight.data.local.AfterLightDatabase;
import com.afterlight.data.local.dao.PartyDao;
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
public final class DatabaseModule_ProvidePartyDaoFactory implements Factory<PartyDao> {
  private final Provider<AfterLightDatabase> databaseProvider;

  public DatabaseModule_ProvidePartyDaoFactory(Provider<AfterLightDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PartyDao get() {
    return providePartyDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePartyDaoFactory create(
      javax.inject.Provider<AfterLightDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePartyDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvidePartyDaoFactory create(
      Provider<AfterLightDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePartyDaoFactory(databaseProvider);
  }

  public static PartyDao providePartyDao(AfterLightDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePartyDao(database));
  }
}
