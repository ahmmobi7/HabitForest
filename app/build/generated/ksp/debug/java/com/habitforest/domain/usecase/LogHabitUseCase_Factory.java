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
public final class LogHabitUseCase_Factory implements Factory<LogHabitUseCase> {
  private final Provider<HabitRepository> repoProvider;

  private final Provider<CalculateGrowthUseCase> calculateGrowthProvider;

  public LogHabitUseCase_Factory(Provider<HabitRepository> repoProvider,
      Provider<CalculateGrowthUseCase> calculateGrowthProvider) {
    this.repoProvider = repoProvider;
    this.calculateGrowthProvider = calculateGrowthProvider;
  }

  @Override
  public LogHabitUseCase get() {
    return newInstance(repoProvider.get(), calculateGrowthProvider.get());
  }

  public static LogHabitUseCase_Factory create(Provider<HabitRepository> repoProvider,
      Provider<CalculateGrowthUseCase> calculateGrowthProvider) {
    return new LogHabitUseCase_Factory(repoProvider, calculateGrowthProvider);
  }

  public static LogHabitUseCase newInstance(HabitRepository repo,
      CalculateGrowthUseCase calculateGrowth) {
    return new LogHabitUseCase(repo, calculateGrowth);
  }
}
