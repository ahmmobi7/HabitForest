package com.habitforest.ui.viewmodel;

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
public final class RewardViewModel_Factory implements Factory<RewardViewModel> {
  private final Provider<HabitRepository> repoProvider;

  public RewardViewModel_Factory(Provider<HabitRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public RewardViewModel get() {
    return newInstance(repoProvider.get());
  }

  public static RewardViewModel_Factory create(Provider<HabitRepository> repoProvider) {
    return new RewardViewModel_Factory(repoProvider);
  }

  public static RewardViewModel newInstance(HabitRepository repo) {
    return new RewardViewModel(repo);
  }
}
