package com.ud.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.ud.myapplication.persistence.EnumNavigation
import com.ud.myapplication.views.HomeScreen
import com.ud.myapplication.views.LoginScreen

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = EnumNavigation.LOGIN.toString()){
        composable(EnumNavigation.LOGIN.toString()) {
            LoginScreen(navController)
        }
        composable (
            route="${EnumNavigation.HOME}/{idPerson}",
            arguments = listOf(navArgument("idPerson") {type = NavType.StringType})
            ){
            val idPerson = it.arguments?.getString("idPerson")
            HomeScreen()
        }
    }

}