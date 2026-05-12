package com.habitforest.domain.usecase;

import com.habitforest.data.repository.HabitRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "deprecation"
})
public final class InsightUseCase_Factory implements Factory<InsightUseCase> {
  private final Provider<HabitRepository> repoProvider;

  public InsightUseCase_Factory(Provider<HabitRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public InsightUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static InsightUseCase_Factory create(Provider<HabitRepository> repoProvider) {
    return new InsightUseCase_Factory(repoProvider);
  }

  public static InsightUseCase newInstance(HabitRepository repo) {
    return new InsightUseCase(repo);
  }
}
