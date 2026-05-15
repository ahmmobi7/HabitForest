package com.habitforest.data.remote

import android.util.Log
import com.habitforest.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.github.jan.supabase.annotations.SupabaseInternal

@OptIn(SupabaseInternal::class)
val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_KEY
) {
    httpEngine = OkHttp.create()
    install(Auth)
    install(Postgrest)

    httpConfig {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("SupabaseClient", message)
                }
            }
            level = LogLevel.ALL
        }
    }
}
