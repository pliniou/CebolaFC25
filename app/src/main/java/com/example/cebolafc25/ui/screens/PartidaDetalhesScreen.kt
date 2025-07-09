package com.example.cebolafc25.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.cebolafc25.data.repository.TeamRepository
import com.example.cebolafc25.domain.viewmodel.PartidaDetalhesState
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
                title = { Text("Registrar Resultado da Partida") },
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
            val state = uiState
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.partida == null) {
                Text(
                    text = "Partida não encontrada.",
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                FormularioResultado(
                    state = state,
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

@Composable
fun FormularioResultado(
    state: PartidaDetalhesState,
    viewModel: PartidaDetalhesViewModel,
    onSave: () -> Unit
) {
    val isFormEnabled = !state.isFinalizada
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(state.nomeJogador1, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            Text("vs", style = MaterialTheme.typography.titleLarge)
            Text(state.nomeJogador2, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TeamSelection(
                    selectedLeague = state.liga1,
                    onLeagueSelected = viewModel::onLiga1Change,
                    selectedTeam = state.time1Nome,
                    onTeamSelected = viewModel::onTime1Change,
                    teamRepository = viewModel.teamRepository,
                    label = "Time - ${state.nomeJogador1}",
                    enabled = isFormEnabled
                )
                OutlinedTextField(
                    value = state.placar1,
                    onValueChange = viewModel::onPlacar1Change,
                    label = { Text("Gols") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(120.dp),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                    enabled = isFormEnabled
                )
            }
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TeamSelection(
                    selectedLeague = state.liga2,
                    onLeagueSelected = viewModel::onLiga2Change,
                    selectedTeam = state.time2Nome,
                    onTeamSelected = viewModel::onTime2Change,
                    teamRepository = viewModel.teamRepository,
                    label = "Time - ${state.nomeJogador2}",
                    enabled = isFormEnabled
                )
                OutlinedTextField(
                    value = state.placar2,
                    onValueChange = viewModel::onPlacar2Change,
                    label = { Text("Gols") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(120.dp),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                    enabled = isFormEnabled
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormEnabled && state.time1Nome.isNotBlank() && state.time2Nome.isNotBlank() && state.placar1.isNotBlank() && state.placar2.isNotBlank()
        ) {
            Icon(Icons.Default.Save, contentDescription = "Salvar")
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text("Salvar Resultado")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSelection(
    modifier: Modifier = Modifier,
    selectedLeague: String,
    onLeagueSelected: (String) -> Unit,
    selectedTeam: String,
    onTeamSelected: (String) -> Unit,
    teamRepository: TeamRepository,
    label: String,
    enabled: Boolean
) {
    var leagueExpanded by remember { mutableStateOf(false) }
    var teamExpanded by remember { mutableStateOf(false) }
    val leagues by produceState(initialValue = emptyList<String>(), teamRepository) {
        value = teamRepository.getLeagues()
    }
    val teamsInLeague by produceState(initialValue = emptyList(), selectedLeague, teamRepository) {
        value = teamRepository.getTeamsForLeague(selectedLeague).map { it.nome }
    }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ExposedDropdownMenuBox(
            expanded = leagueExpanded && enabled,
            onExpandedChange = { if (enabled) leagueExpanded = !it }
        ) {
            OutlinedTextField(
                value = selectedLeague,
                onValueChange = {},
                readOnly = true,
                label = { Text("Liga") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = leagueExpanded) },
                modifier = Modifier.menuAnchor(),
                enabled = enabled
            )
            ExposedDropdownMenu(
                expanded = leagueExpanded && enabled,
                onDismissRequest = { leagueExpanded = false }
            ) {
                leagues.forEach { league ->
                    DropdownMenuItem(text = { Text(league) }, onClick = {
                        onLeagueSelected(league)
                        leagueExpanded = false
                    })
                }
            }
        }
        ExposedDropdownMenuBox(
            expanded = teamExpanded && enabled,
            onExpandedChange = { if (enabled) teamExpanded = !it }
        ) {
            OutlinedTextField(
                value = if (selectedTeam == "A definir") "" else selectedTeam,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = teamExpanded) },
                modifier = Modifier.menuAnchor(),
                enabled = enabled
            )
            ExposedDropdownMenu(
                expanded = teamExpanded && enabled,
                onDismissRequest = { teamExpanded = false }
            ) {
                teamsInLeague.forEach { teamName ->
                    DropdownMenuItem(text = { Text(teamName) }, onClick = {
                        onTeamSelected(teamName)
                        teamExpanded = false
                    })
                }
            }
        }
    }
}
