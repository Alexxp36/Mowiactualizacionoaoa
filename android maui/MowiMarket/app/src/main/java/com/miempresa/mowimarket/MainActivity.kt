package com.miempresa.mowimarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.miempresa.mowimarket.ui.navigation.NavGraph
import com.miempresa.mowimarket.ui.navigation.Screen
import com.miempresa.mowimarket.ui.theme.MowiMarketTheme
import com.miempresa.mowimarket.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MowiMarketTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val viewModel = remember { MainViewModel(context) }
                    val currentUser by viewModel.currentUser.collectAsState()
                    val navController = rememberNavController()

                    // Determinar la pantalla inicial basada en el rol del usuario
                    val startDestination = if (currentUser?.isAdmin == true || currentUser?.isStaff == true) {
                        Screen.AdminDashboard.route
                    } else {
                        Screen.Home.route
                    }

                    NavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}