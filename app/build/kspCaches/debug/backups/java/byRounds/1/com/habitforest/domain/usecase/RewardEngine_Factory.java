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
public final class RewardEngine_Factory implements Factory<RewardEngine> {
  private final Provider<HabitRepository> repoProvider;

  public RewardEngine_Factory(Provider<HabitRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public RewardEngine get() {
    return newInstance(repoProvider.get());
  }

  public static RewardEngine_Factory create(Provider<HabitRepository> repoProvider) {
    return new RewardEngine_Factory(repoProvider);
  }

  public static RewardEngine newInstance(HabitRepository repo) {
    return new RewardEngine(repo);
  }
}
