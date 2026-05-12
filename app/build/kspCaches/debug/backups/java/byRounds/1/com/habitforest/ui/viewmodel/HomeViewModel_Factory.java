package com.habitforest.ui.viewmodel;

import com.habitforest.data.repository.HabitRepository;
import com.habitforest.domain.usecase.AddHabitUseCase;
import com.habitforest.domain.usecase.GetHabitsUseCase;
import com.habitforest.domain.usecase.InsightUseCase;
import com.habitforest.domain.usecase.LogHabitUseCase;
import com.habitforest.domain.usecase.RewardEngine;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<HabitRepository> repoProvider;

  private final Provider<GetHabitsUseCase> getHabitsProvider;

  private final Provider<LogHabitUseCase> logHabitProvider;

  private final Provider<AddHabitUseCase> addHabitProvider;

  private final Provider<InsightUseCase> insightUseCaseProvider;

  private final Provider<RewardEngine> rewardEngineProvider;

  public HomeViewModel_Factory(Provider<HabitRepository> repoProvider,
      Provider<GetHabitsUseCase> getHabitsProvider, Provider<LogHabitUseCase> logHabitProvider,
      Provider<AddHabitUseCase> addHabitProvider, Provider<InsightUseCase> insightUseCaseProvider,
      Provider<RewardEngine> rewardEngineProvider) {
    this.repoProvider = repoProvider;
    this.getHabitsProvider = getHabitsProvider;
    this.logHabitProvider = logHabitProvider;
    this.addHabitProvider = addHabitProvider;
    this.insightUseCaseProvider = insightUseCaseProvider;
    this.rewardEngineProvider = rewardEngineProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(repoProvider.get(), getHabitsProvider.get(), logHabitProvider.get(), addHabitProvider.get(), insightUseCaseProvider.get(), rewardEngineProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<HabitRepository> repoProvider,
      Provider<GetHabitsUseCase> getHabitsProvider, Provider<LogHabitUseCase> logHabitProvider,
      Provider<AddHabitUseCase> addHabitProvider, Provider<InsightUseCase> insightUseCaseProvider,
      Provider<RewardEngine> rewardEngineProvider) {
    return new HomeViewModel_Factory(repoProvider, getHabitsProvider, logHabitProvider, addHabitProvider, insightUseCaseProvider, rewardEngineProvider);
  }

  public static HomeViewModel newInstance(HabitRepository repo, GetHabitsUseCase getHabits,
      LogHabitUseCase logHabit, AddHabitUseCase addHabit, InsightUseCase insightUseCase,
      RewardEngine rewardEngine) {
    return new HomeViewModel(repo, getHabits, logHabit, addHabit, insightUseCase, rewardEngine);
  }
}
