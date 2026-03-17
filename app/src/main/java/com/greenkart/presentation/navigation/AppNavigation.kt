package com.greenkart.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.greenkart.presentation.auth.LoginScreen
import com.greenkart.presentation.auth.SignupScreen
import com.greenkart.presentation.main.MainScreen

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Signup : Screen("signup")
    data object Main : Screen("main")
    data object Detail : Screen("detail/{vegetableId}") {
        fun createRoute(id: String) = "detail/$id"
    }
    data object Cart : Screen("cart")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable(Screen.Signup.route) {
            SignupScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable(Screen.Main.route) {
            MainScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToDetail = { vegetableId ->
                    navController.navigate(Screen.Detail.createRoute(vegetableId))
                },
                onViewCart = {
                    navController.navigate(Screen.Cart.route)
                }
            )
        }

        composable(Screen.Detail.route) { backStackEntry ->
            val vegetableId = backStackEntry.arguments?.getString("vegetableId") ?: ""
            com.greenkart.presentation.detail.VegetableDetailScreen(
                vegetableId = vegetableId,
                onBack = { navController.popBackStack() },
                onViewCart = { navController.navigate(Screen.Cart.route) }
            )
        }

        composable(Screen.Cart.route) {
            com.greenkart.presentation.cart.CartScreen(
                onBack = { navController.popBackStack() },
                onOrderPlaced = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
