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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<HabitRepository> repoProvider;

  public AuthViewModel_Factory(Provider<HabitRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(repoProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<HabitRepository> repoProvider) {
    return new AuthViewModel_Factory(repoProvider);
  }

  public static AuthViewModel newInstance(HabitRepository repo) {
    return new AuthViewModel(repo);
  }
}
