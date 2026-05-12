package com.habitforest.utils;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
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
public final class DailyReminderWorker_Factory {
  public DailyReminderWorker get(Context ctx, WorkerParameters params) {
    return newInstance(ctx, params);
  }

  public static DailyReminderWorker_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DailyReminderWorker newInstance(Context ctx, WorkerParameters params) {
    return new DailyReminderWorker(ctx, params);
  }

  private static final class InstanceHolder {
    private static final DailyReminderWorker_Factory INSTANCE = new DailyReminderWorker_Factory();
  }
}
