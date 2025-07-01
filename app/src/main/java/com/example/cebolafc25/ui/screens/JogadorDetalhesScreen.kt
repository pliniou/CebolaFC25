package com.example.cebolafc25.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cebolafc25.R
import com.example.cebolafc25.domain.model.EstatisticasJogador
import com.example.cebolafc25.domain.model.UiState
import com.example.cebolafc25.domain.viewmodel.JogadorDetalhesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JogadorDetalhesScreen(
    navController: NavController,
    viewModel: JogadorDetalhesViewModel = hiltViewModel()
) {
    val uiState by viewModel.jogadorDetalhesState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.players_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back) // Acessibilidade
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UiState.Error -> {
                    Text(
                        text = state.message ?: stringResource(R.string.error_unknown), // String de recurso
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is UiState.Success -> {
                    DetalhesContent(stats = state.data)
                }
            }
        }
    }
}

@Composable
fun DetalhesContent(stats: EstatisticasJogador) {
    // Exemplo de responsividade: usa BoxWithConstraints para adaptar o layout
    BoxWithConstraints {
        val isTablet = this.maxWidth > 600.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stats.jogador.nome, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))

            if (isTablet) {
                // Layout em linha para telas largas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CardStats(stats, modifier = Modifier.weight(1f))
                    // Poderia adicionar outra coluna aqui, ex: histórico de partidas
                    Box(modifier = Modifier.weight(1f)) {
                        Text("Histórico de Partidas (A implementar)")
                    }
                }
            } else {
                // Layout em coluna para telas estreitas
                CardStats(stats, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                Text("Histórico de Partidas (A implementar)")
            }
        }
    }
}

@Composable
fun CardStats(stats: EstatisticasJogador, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Estatísticas Gerais", style = MaterialTheme.typography.titleLarge)
            HorizontalDivider()
            StatRow("Pontos", stats.pontos.toString())
            StatRow("Partidas Jogadas", stats.jogos.toString())
            StatRow("Vitórias", stats.vitorias.toString())
            StatRow("Empates", stats.empates.toString())
            StatRow("Derrotas", stats.derrotas.toString())
            StatRow("Saldo de Gols", stats.saldoGols.toString())
            StatRow("Gols Pró", stats.golsPro.toString())
            StatRow("Gols Contra", stats.golsContra.toString())
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}