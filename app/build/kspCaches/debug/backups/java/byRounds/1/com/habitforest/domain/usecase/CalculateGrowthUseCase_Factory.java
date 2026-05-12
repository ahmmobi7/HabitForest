package com.habitforest.domain.usecase;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class CalculateGrowthUseCase_Factory implements Factory<CalculateGrowthUseCase> {
  @Override
  public CalculateGrowthUseCase get() {
    return newInstance();
  }

  public static CalculateGrowthUseCase_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CalculateGrowthUseCase newInstance() {
    return new CalculateGrowthUseCase();
  }

  private static final class InstanceHolder {
    private static final CalculateGrowthUseCase_Factory INSTANCE = new CalculateGrowthUseCase_Factory();
  }
}
