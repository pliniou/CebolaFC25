package com.example.cebolafc25.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cebolafc25.R
import com.example.cebolafc25.domain.viewmodel.HomeViewModel
import com.example.cebolafc25.navigation.BottomNavItem
import com.example.cebolafc25.ui.components.PartidaCard

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val ultimasPartidas by viewModel.ultimasPartidas.collectAsStateWithLifecycle()
    val jogadores by viewModel.jogadores.collectAsStateWithLifecycle()
    val unknownPlayer = stringResource(id = R.string.matches_player_unknown)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.home_welcome),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(
            onClick = { navController.navigate(BottomNavItem.Matches.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(stringResource(id = R.string.home_register_match))
        }
        Button(
            onClick = { navController.navigate(BottomNavItem.Tournaments.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.home_create_tournament))
        }
        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Últimas Partidas",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start)
        )
        if (ultimasPartidas.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma partida registrada ainda.")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(ultimasPartidas, key = { it.id }) { partida ->
                    val nomeJogador1 = jogadores.find { it.id == partida.jogador1Id }?.nome ?: unknownPlayer
                    val nomeJogador2 = jogadores.find { it.id == partida.jogador2Id }?.nome ?: unknownPlayer
                    PartidaCard(
                        partida = partida,
                        nomeJogador1 = nomeJogador1,
                        nomeJogador2 = nomeJogador2
                    )
                }
            }
        }
    }
}