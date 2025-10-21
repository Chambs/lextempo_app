package com.lextempo.calculadorapenal.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lextempo.calculadorapenal.ui.screens.CalculatorScreen
import com.lextempo.calculadorapenal.ui.screens.ResultScreen
import com.lextempo.calculadorapenal.ui.viewmodel.CalculatorViewModel

sealed class Routes(val route: String) {
    data object Calc : Routes("calc")
    data object Result : Routes("result")
}

@Composable
fun NavRoot(nav: NavHostController = rememberNavController()) {
    val vm: CalculatorViewModel = hiltViewModel()
    NavHost(navController = nav, startDestination = Routes.Calc.route) {
        composable(Routes.Calc.route) {
            CalculatorScreen(
                state = vm.uiState,
                onIntent = vm::onIntent,
                onCalculate = {
                    vm.calculate()
                    nav.navigate(Routes.Result.route)
                }
            )
        }
        composable(Routes.Result.route) {
            ResultScreen(
                result = vm.lastResult,
                onNewCalc = { nav.popBackStack(Routes.Calc.route, inclusive = false) }
            )
        }
    }
}
