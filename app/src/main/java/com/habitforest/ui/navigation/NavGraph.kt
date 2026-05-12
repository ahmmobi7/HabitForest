package com.habitforest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.habitforest.ui.screens.*

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home       : Screen("home")
    object Forest     : Screen("forest")
    object CheckIn    : Screen("checkin")
    object AddHabit   : Screen("add_habit")
    object Rewards    : Screen("rewards")
}

@Composable
fun HabitForestNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToForest  = { navController.navigate(Screen.Forest.route) },
                onNavigateToCheckIn = { navController.navigate(Screen.CheckIn.route) },
                onNavigateToRewards = { navController.navigate(Screen.Rewards.route) },
                onAddHabit          = { navController.navigate(Screen.AddHabit.route) }
            )
        }

        composable(Screen.Forest.route) {
            ForestScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.CheckIn.route) {
            CheckInScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.AddHabit.route) {
            AddHabitScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Rewards.route) {
            RewardScreen(onBack = { navController.popBackStack() })
        }
    }
}
