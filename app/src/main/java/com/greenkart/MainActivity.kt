package com.greenkart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.greenkart.presentation.navigation.AppNavigation
import com.greenkart.ui.theme.GreenkartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreenkartTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}