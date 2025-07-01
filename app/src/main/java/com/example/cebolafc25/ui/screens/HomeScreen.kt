// ARQUIVO: app/src/main/java/com/example/cebolafc25/ui/screens/HomeScreen.kt

package com.example.cebolafc25.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cebolafc25.R
import com.example.cebolafc25.navigation.BottomNavItem

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.home_welcome),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            // Este código agora compilará corretamente
            onClick = { navController.navigate(BottomNavItem.Matches.route) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text(stringResource(id = R.string.home_register_match))
        }
        Button(
            // Este código agora compilará corretamente
            onClick = { navController.navigate(BottomNavItem.Tournaments.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.home_create_tournament))
        }
    }
}