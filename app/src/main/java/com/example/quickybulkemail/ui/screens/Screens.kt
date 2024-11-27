package com.example.quickybulkemail.ui.screens

sealed class Screens(val route: String) {
    object Main : Screens(route = "main_screen")
    object Second : Screens(route = "second_screen")
}