package com.example.expensetracker.data.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.expensetracker.ui.components.BottomNavigationBar
import com.example.expensetracker.ui.screens.AddTransactions
import com.example.expensetracker.ui.screens.Alltransaction
import com.example.expensetracker.ui.screens.CurrencySelectionMinimal
import com.example.expensetracker.ui.screens.DashboardScreen
import com.example.expensetracker.ui.screens.AiInsightsDashboard
import com.example.expensetracker.ui.screens.EditProfileScreen
import com.example.expensetracker.ui.screens.HomeScreen
import com.example.expensetracker.ui.screens.SettingsScreen
import com.example.expensetracker.ui.screens.SearchTransactions
import com.example.expensetracker.ui.screens.SplashScreen
import com.example.expensetracker.ui.screens.UpdateTransactions
import com.example.expensetracker.ui.screens.UserInfoScreen
import com.example.expensetracker.viewmodels.ProfileViewModel
import com.example.expensetracker.viewmodels.Transacviewmodel
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

@Serializable
object StartupScrn

@Serializable
object SettingsScrn

@Serializable
object CurrencyScrn

@Serializable
object EditProfileScrn
@Serializable
object SplashScrn

@Serializable
object AiDashboardScrn
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(viewModel: Transacviewmodel = viewModel(),
               navController: NavHostController = rememberNavController()) {

    // Get the current back stack entry
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route ?: ""

    // Check if current route is an auth screen/startup
    val shouldShowBottomBar = when {
        currentRoute.contains("Splash", ignoreCase = true) -> false
        currentRoute.contains("Startup", ignoreCase = true) -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SplashScrn,//SignUpScrn,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<StartupScrn> {
                UserInfoScreen(navController = navController)
            }

            // MAIN APP SCREENS (with bottom bar)
            composable<HomeScrn> {
                HomeScreen(navController = navController, viewModel = viewModel)
            }

            composable<AddTransac> {
                AddTransactions(viewModel = viewModel, navController = navController)
            }

            composable<AllTransac> {
                Alltransaction(viewModel = viewModel, navController = navController)
            }

            composable<SearchTransac> {
                SearchTransactions(viewModel = viewModel, navController = navController)
            }

            composable<DashboardTransac> {
                DashboardScreen(viewModel = viewModel, navController = navController)
            }

            composable<UpdateTransac> { backStackEntry ->
                val args = backStackEntry.toRoute<UpdateTransac>()
                UpdateTransactions(
                    id = args.id,
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable<SettingsScrn> {
                SettingsScreen(navController = navController, viewModel = viewModel)
            }
            composable<EditProfileScrn> {
                val pviewModel: ProfileViewModel= viewModel()
                EditProfileScreen(navController = navController, viewModel = pviewModel)
            }
            composable<SplashScrn> {
                SplashScreen(navController = navController)
            }
            composable<CurrencyScrn> {
                CurrencySelectionMinimal(navController = navController)
            }
            composable<AiDashboardScrn> {
                AiInsightsDashboard(navController = navController, viewModel = viewModel)
            }
        }
    }
}


