package com.example.cebolafc25

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.cebolafc25.navigation.AppNavigation
import com.example.cebolafc25.ui.theme.EAFC25ManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita o modo edge-to-edge
        setContent {
            EAFC25ManagerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    // O ViewModel será injetado pelo Hilt nas telas que o necessitarem.
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}