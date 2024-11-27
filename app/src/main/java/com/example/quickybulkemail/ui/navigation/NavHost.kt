package com.example.quickybulkemail.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quickybulkemail.ui.screens.Main
import com.example.quickybulkemail.ui.screens.Screens
import com.example.quickybulkemail.ui.screens.Second
import com.example.quickybulkemail.ui.viewmodel.EmailViewModel

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    viewModel: EmailViewModel,
    modifier: Modifier,
    p: PaddingValues){

    NavHost(
        navController = navController,
        startDestination = Screens.Main.route
    ) {
        composable(route = Screens.Main.route) {
            Main(navController = navController,viewModel,modifier,p)
        }
        composable(route = Screens.Second.route) {
            Second(navController,viewModel,modifier,p)
        }
    }
}