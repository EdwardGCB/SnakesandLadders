package com.ud.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ud.myapplication.persistence.EnumNavigation
import com.ud.myapplication.views.HomeScreen
import com.ud.myapplication.views.LoginScreen
import com.ud.myapplication.views.MainGame

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = EnumNavigation.LOGIN.toString()){
        composable(EnumNavigation.LOGIN.toString()) {
            LoginScreen(navController)
        }
        composable (EnumNavigation.HOME.toString()){
            HomeScreen(navController)
        }
        composable(
            route="${EnumNavigation.PLAY}/{idBoard}",
            arguments = listOf(navArgument("idBoard") {type = NavType.StringType})
        ){
            val idBoard = it.arguments?.getString("idBoard")
            MainGame(idBoard, navController)
        }
    }

}