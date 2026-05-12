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
public final class GetHabitsUseCase_Factory implements Factory<GetHabitsUseCase> {
  private final Provider<HabitRepository> repoProvider;

  public GetHabitsUseCase_Factory(Provider<HabitRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public GetHabitsUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static GetHabitsUseCase_Factory create(Provider<HabitRepository> repoProvider) {
    return new GetHabitsUseCase_Factory(repoProvider);
  }

  public static GetHabitsUseCase newInstance(HabitRepository repo) {
    return new GetHabitsUseCase(repo);
  }
}
