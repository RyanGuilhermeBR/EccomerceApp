package com.example.eccomerceapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eccomerceapp.data.api.AuthApi
import com.example.eccomerceapp.data.api.ProductApi
import com.example.eccomerceapp.data.repository.AuthRepository
import com.example.eccomerceapp.data.repository.ProductRepository
import com.example.eccomerceapp.presentation.home.HomeScreen
import com.example.eccomerceapp.presentation.home.HomeViewModel
import com.example.eccomerceapp.presentation.login.LoginScreen
import com.example.eccomerceapp.presentation.login.LoginViewModel
import com.example.eccomerceapp.presentation.register.RegisterScreen
import com.example.eccomerceapp.presentation.register.RegisterViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    // TODO: Replace with actual API base URL
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi = retrofit.create(AuthApi::class.java)
    val productApi = retrofit.create(ProductApi::class.java)

    val authRepository = AuthRepository(authApi)
    val productRepository = ProductRepository(productApi)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            val viewModel = LoginViewModel(authRepository)
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            val viewModel = RegisterViewModel(authRepository)
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            val viewModel = HomeViewModel(productRepository)
            HomeScreen(
                viewModel = viewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
