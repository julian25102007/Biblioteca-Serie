package com.example.bibliotecadeseries.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.Flow
import com.example.bibliotecadeseries.ui.login.LoginScreen
import com.example.bibliotecadeseries.ui.register.RegisterScreen
import com.example.bibliotecadeseries.ui.detail.SerieDetailScreen
import com.example.bibliotecadeseries.ui.form.SerieFormScreen
import com.example.bibliotecadeseries.ui.home.SeriesScreen

@Composable
fun AppNavHost(
    startDestination: String,
    sessionExpired: Flow<Unit>,
    navController: NavHostController = rememberNavController()
) {

    val irAHome = {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.LOGIN) { inclusive = true }
        }
    }


    LaunchedEffect(Unit) {
        sessionExpired.collect {
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.HOME) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onSesionIniciada = irAHome,
                onIrARegistro = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegistroExitoso = irAHome,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            SeriesScreen(
                onNuevaSerie = { navController.navigate(Routes.form()) },
                onVerDetalle = { id -> navController.navigate(Routes.detail(id)) },
                onEditar = { id -> navController.navigate(Routes.form(id)) },
                onSesionCerrada = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.IntType })
        ) {
            SerieDetailScreen(
                onVolver = { navController.popBackStack() },
                onEditar = { id -> navController.navigate(Routes.form(id)) }
            )
        }

        composable(
            route = Routes.FORM,
            arguments = listOf(
                navArgument(Routes.ARG_ID) {
                    type = NavType.IntType
                    defaultValue = Routes.NEW_ID
                }
            )
        ) {
            SerieFormScreen(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }
    }
}