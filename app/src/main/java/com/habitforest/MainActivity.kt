package com.habitforest

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.habitforest.data.remote.supabase
import com.habitforest.ui.navigation.HabitForestNavGraph
import com.habitforest.ui.navigation.Screen
import com.habitforest.ui.theme.HabitForestTheme
import com.habitforest.utils.DailyReminderWorker
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.auth.auth

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val notifPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) DailyReminderWorker.schedule(this)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request notification permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            DailyReminderWorker.schedule(this)
        }

        // Choose start destination based on auth state
        val isLoggedIn = supabase.auth.currentUserOrNull() != null
        val start = if (isLoggedIn) Screen.Home.route else Screen.Onboarding.route

        setContent {
            HabitForestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color    = MaterialTheme.colorScheme.background
                ) {
                    HabitForestNavGraph(
                        navController     = rememberNavController(),
                        startDestination  = start
                    )
                }
            }
        }
    }
}
