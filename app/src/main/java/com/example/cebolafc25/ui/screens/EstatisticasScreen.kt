package com.example.cebolafc25.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cebolafc25.R
import com.example.cebolafc25.domain.model.EstatisticasJogador
import com.example.cebolafc25.domain.viewmodel.EstatisticasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstatisticasScreen(
    viewModel: EstatisticasViewModel = hiltViewModel()
) {
    val estatisticas by viewModel.estatisticas.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(id = R.string.stats_title)) })
        }
    ) { paddingValues ->
        if (estatisticas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(id = R.string.stats_no_data))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .semantics { heading() }
                    ) {
                        Text(stringResource(id = R.string.stats_header_player), Modifier.weight(2.5f), fontWeight = FontWeight.Bold)
                        StatHeader(stringResource(id = R.string.stats_header_points), Modifier.weight(0.8f))
                        StatHeader(stringResource(id = R.string.stats_header_played), Modifier.weight(0.8f))
                        StatHeader(stringResource(id = R.string.stats_header_wins), Modifier.weight(0.8f))
                        StatHeader(stringResource(id = R.string.stats_header_draws), Modifier.weight(0.8f))
                        StatHeader(stringResource(id = R.string.stats_header_losses), Modifier.weight(0.8f))
                        StatHeader(stringResource(id = R.string.stats_header_goal_difference), Modifier.weight(1f))
                    }
                    HorizontalDivider(Modifier.padding(top = 8.dp))
                }
                items(estatisticas, key = { it.jogador.id }) { stats ->
                    EstatisticaRow(stats = stats)
                }
            }
        }
    }
}

@Composable
fun EstatisticaRow(stats: EstatisticasJogador) {
    Card(shape = MaterialTheme.shapes.medium) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stats.jogador.nome, Modifier.weight(2.5f), style = MaterialTheme.typography.bodyLarge)
            StatCell(stats.pontos.toString(), Modifier.weight(0.8f), FontWeight.Bold)
            StatCell(stats.jogos.toString(), Modifier.weight(0.8f))
            StatCell(stats.vitorias.toString(), Modifier.weight(0.8f))
            StatCell(stats.empates.toString(), Modifier.weight(0.8f))
            StatCell(stats.derrotas.toString(), Modifier.weight(0.8f))
            StatCell(stats.saldoGols.toString(), Modifier.weight(1f))
        }
    }
}

@Composable
fun StatHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun StatCell(text: String, modifier: Modifier = Modifier, fontWeight: FontWeight? = null) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = fontWeight
    )
}
