package com.example.expensetracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable



@Serializable
object AddTransac

@Serializable
object AllTransac

@Serializable
object SearchTransac

@Serializable
object HomeScrn

@Serializable
data class UpdateTransac(val id: Long)
@Serializable
object DashboardTransac

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(viewModel : Transacviewmodel = viewModel(),
               navController: NavHostController = rememberNavController()) {

    NavHost(
        navController = navController,
        startDestination = HomeScrn


    ) {
        composable<HomeScrn> {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable<AddTransac> {
            AddTransactions(viewModel = viewModel, navController = navController)
        }

        composable<AllTransac> {

            Alltransaction(viewModel = viewModel, navController = navController)
        }
        composable<UpdateTransac> { backStackEntry ->
            val args = backStackEntry.toRoute<UpdateTransac>()
            UpdateTransactions(id = args.id, viewModel = viewModel, navController = navController)
        }
        composable<SearchTransac> {
            SearchTransactions(viewModel = viewModel, navController = navController)
        }
        composable<DashboardTransac> {
            DashboardScreen(navController = navController)
        }

    }
}

