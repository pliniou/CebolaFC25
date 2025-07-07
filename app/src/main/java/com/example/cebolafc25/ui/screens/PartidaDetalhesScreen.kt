package com.example.cebolafc25.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cebolafc25.R
import com.example.cebolafc25.domain.model.UiState
import com.example.cebolafc25.domain.viewmodel.PartidaDetalhesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidaDetalhesScreen(
    navController: NavController,
    viewModel: PartidaDetalhesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Resultado") },
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
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Text(
                    text = state.message ?: "Erro desconhecido",
                    color = MaterialTheme.colorScheme.error
                )
                is UiState.Success -> {
                    val detalhesState = state.data
                    FormularioResultado(
                        state = detalhesState,
                        viewModel = viewModel,
                        onSave = {
                            viewModel.savePartida()
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FormularioResultado(
    state: com.example.cebolafc25.domain.viewmodel.PartidaDetalhesState,
    viewModel: PartidaDetalhesViewModel,
    onSave: () -> Unit
) {
    val leagues = viewModel.teamRepository.getLeagues()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Exibição dos jogadores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(state.nomeJogador1, style = MaterialTheme.typography.headlineSmall)
            Text("vs", style = MaterialTheme.typography.titleLarge)
            Text(state.nomeJogador2, style = MaterialTheme.typography.headlineSmall)
        }

        HorizontalDivider()

        // Formulário de placar e times
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Time Jogador 1
            TeamSelection(
                modifier = Modifier.weight(1f),
                leagues = leagues,
                selectedTeam = state.time1Nome,
                onTeamSelected = viewModel::onTime1Change,
                teamRepository = viewModel.teamRepository,
                label = "Time J1"
            )

            // Placar
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = state.placar1,
                    onValueChange = viewModel::onPlacar1Change,
                    label = { Text("Gols") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(80.dp),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.placar2,
                    onValueChange = viewModel::onPlacar2Change,
                    label = { Text("Gols") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(80.dp),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
            }

            // Time Jogador 2
            TeamSelection(
                modifier = Modifier.weight(1f),
                leagues = leagues,
                selectedTeam = state.time2Nome,
                onTeamSelected = viewModel::onTime2Change,
                teamRepository = viewModel.teamRepository,
                label = "Time J2"
            )
        }

        Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
            Text("Salvar Resultado")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSelection(
    modifier: Modifier = Modifier,
    leagues: List<String>,
    selectedTeam: String,
    onTeamSelected: (String) -> Unit,
    teamRepository: com.example.cebolafc25.data.repository.TeamRepository,
    label: String
) {
    var selectedLeague by remember {
        mutableStateOf(teamRepository.getTeamsForLeague("").find { it.nome == selectedTeam }?.liga ?: leagues.firstOrNull() ?: "")
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Dropdown de Ligas
        var leagueExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = leagueExpanded, onExpandedChange = { leagueExpanded = !leagueExpanded }) {
            OutlinedTextField(
                value = selectedLeague,
                onValueChange = {},
                readOnly = true,
                label = { Text("Liga") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = leagueExpanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(expanded = leagueExpanded, onDismissRequest = { leagueExpanded = false }) {
                leagues.forEach { league ->
                    DropdownMenuItem(text = { Text(league) }, onClick = {
                        selectedLeague = league
                        leagueExpanded = false
                    })
                }
            }
        }

        // Dropdown de Times
        var teamExpanded by remember { mutableStateOf(false) }
        val teamsInLeague = teamRepository.getTeamsForLeague(selectedLeague)
        ExposedDropdownMenuBox(expanded = teamExpanded, onExpandedChange = { teamExpanded = !teamExpanded }) {
            OutlinedTextField(
                value = selectedTeam,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = teamExpanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(expanded = teamExpanded, onDismissRequest = { teamExpanded = false }) {
                teamsInLeague.forEach { team ->
                    DropdownMenuItem(text = { Text(team.nome) }, onClick = {
                        onTeamSelected(team.nome)
                        teamExpanded = false
                    })
                }
            }
        }
    }
}