package com.mathapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mathapp.ui.screens.*
import com.mathapp.ui.theme.MathAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MathAppTheme {
                MathAppNavigation()
            }
        }
    }
}

@Composable
fun MathAppNavigation() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != "canvas") {
                NavigationBar(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Edit, contentDescription = "Canvas") },
                        label = { Text("رسم") },
                        selected = currentRoute == "canvas",
                        onClick = { navController.navigate("canvas") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Functions, contentDescription = "Graph") },
                        label = { Text("رسم الدوال") },
                        selected = currentRoute == "graph",
                        onClick = { navController.navigate("graph") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.History, contentDescription = "History") },
                        label = { Text("السجل") },
                        selected = currentRoute == "history",
                        onClick = { navController.navigate("history") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                        label = { Text("الإعدادات") },
                        selected = currentRoute == "settings",
                        onClick = { navController.navigate("settings") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "canvas",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("canvas") {
                CanvasScreen(navController)
            }
            composable("graph") {
                GraphScreen()
            }
            composable("history") {
                HistoryScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
        }
    }
}
