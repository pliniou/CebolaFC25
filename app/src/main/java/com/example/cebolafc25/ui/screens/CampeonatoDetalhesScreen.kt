package com.example.cebolafc25.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cebolafc25.R
import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.domain.model.EstatisticasJogador
import com.example.cebolafc25.domain.model.UiState
import com.example.cebolafc25.domain.viewmodel.CampeonatoDetalhesViewModel
import com.example.cebolafc25.navigation.MATCH_DETAILS_ROUTE
import com.example.cebolafc25.ui.components.PartidaCard
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampeonatoDetalhesScreen(
    navController: NavController,
    viewModel: CampeonatoDetalhesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            val title = when (val state = uiState) {
                is UiState.Success -> state.data.campeonato?.nome ?: "Detalhes"
                else -> "Detalhes do Campeonato"
            }
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
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
            contentAlignment = Alignment.TopCenter
        ) {
            when (val state = uiState) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is UiState.Error -> Text(
                    text = state.message ?: "Erro desconhecido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
                is UiState.Success -> {
                    val detalhesState = state.data
                    TabScreen(
                        navController = navController,
                        classificacao = detalhesState.classificacao,
                        partidas = detalhesState.partidas,
                        jogadoresMap = detalhesState.todosJogadores
                    )
                }
            }
        }
    }
}

@Composable
fun TabScreen(
    navController: NavController,
    classificacao: List<EstatisticasJogador>,
    partidas: List<PartidaEntity>,
    jogadoresMap: Map<UUID, String>
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Classificação", "Partidas")
    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> ClassificacaoTab(classificacao)
            1 -> PartidasTab(navController, partidas, jogadoresMap)
        }
    }
}

@Composable
fun ClassificacaoTab(classificacao: List<EstatisticasJogador>) {
    if (classificacao.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Nenhuma partida finalizada para gerar a classificação.", textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
        }
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                Text(text = stringResource(id = R.string.stats_header_player), modifier = Modifier.weight(2.5f))
                StatHeader(text = stringResource(id = R.string.stats_header_points), modifier = Modifier.weight(0.8f))
                StatHeader(text = stringResource(id = R.string.stats_header_played), modifier = Modifier.weight(0.8f))
                StatHeader(text = stringResource(id = R.string.stats_header_wins), modifier = Modifier.weight(0.8f))
                StatHeader(text = stringResource(id = R.string.stats_header_draws), modifier = Modifier.weight(0.8f))
                StatHeader(text = stringResource(id = R.string.stats_header_losses), modifier = Modifier.weight(0.8f))
                StatHeader(text = stringResource(id = R.string.stats_header_goal_difference), modifier = Modifier.weight(1f))
            }
            HorizontalDivider(Modifier.padding(top = 8.dp))
        }
        items(classificacao, key = { it.jogador.id }) { stats ->
            EstatisticaRow(stats = stats)
        }
    }
}

@Composable
fun PartidasTab(
    navController: NavController,
    partidas: List<PartidaEntity>,
    jogadoresMap: Map<UUID, String>
) {
    if (partidas.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Nenhuma partida gerada para este campeonato.")
        }
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(partidas, key = { it.id }) { partida ->
            val nomeJogador1 = jogadoresMap[partida.jogador1Id] ?: "???"
            val nomeJogador2 = jogadoresMap[partida.jogador2Id] ?: "???"
            Box(modifier = Modifier.clickable {
                navController.navigate("$MATCH_DETAILS_ROUTE/${partida.id}")
            }) {
                PartidaCard(
                    partida = partida,
                    nomeJogador1 = nomeJogador1,
                    nomeJogador2 = nomeJogador2
                )
            }
        }
    }
}
