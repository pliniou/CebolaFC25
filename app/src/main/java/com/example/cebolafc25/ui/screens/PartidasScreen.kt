package com.example.cebolafc25.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cebolafc25.R
import com.example.cebolafc25.data.model.JogadorEntity
import com.example.cebolafc25.data.model.TimeEntity
import com.example.cebolafc25.domain.viewmodel.PartidaFormEvent
import com.example.cebolafc25.domain.viewmodel.PartidaFormState
import com.example.cebolafc25.domain.viewmodel.PartidasViewModel
import com.example.cebolafc25.domain.viewmodel.UiEvent
import com.example.cebolafc25.ui.components.PartidaCard
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidasScreen(
    viewModel: PartidasViewModel = hiltViewModel()
) {
    val partidas by viewModel.partidas.collectAsStateWithLifecycle()
    val jogadores by viewModel.jogadores.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val unknownPlayer = stringResource(id = R.string.matches_player_unknown)

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowSnackbar -> {
                    val message = context.getString(event.messageResId, *event.args.toTypedArray())
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(id = R.string.matches_title)) }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(top = 16.dp, bottom = 8.dp)) {
                if (jogadores.size < 2) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(id = R.string.matches_need_2_players),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else if (partidas.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                            Text(stringResource(id = R.string.matches_none_registered), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                } else {
                    items(partidas, key = { it.id }) { partida ->
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
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            if (jogadores.size >= 2) {
                MatchRegistrationForm(
                    formState = formState,
                    onEvent = viewModel::onEvent,
                    jogadores = jogadores,
                    getAvailableLeagues = viewModel::getAvailableLeagues,
                    getTeamsForLeague = viewModel::getTeamsForLeague
                )
            }
        }
    }
}

@Composable
private fun MatchRegistrationForm(
    formState: PartidaFormState,
    onEvent: (PartidaFormEvent) -> Unit,
    jogadores: List<JogadorEntity>,
    getAvailableLeagues: () -> List<String>,
    getTeamsForLeague: (String) -> List<TimeEntity>
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.matches_register_new),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        // Player Selection
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val availableJogadores2 = jogadores.filter { it.id != formState.jogador1Id }
            PlayerDropdown(
                label = stringResource(id = R.string.matches_player1_label),
                selectedPlayerId = formState.jogador1Id,
                players = jogadores,
                onPlayerSelected = { onEvent(PartidaFormEvent.UpdateJogador1(it)) },
                modifier = Modifier.weight(1f),
                enabled = jogadores.isNotEmpty()
            )
            PlayerDropdown(
                label = stringResource(id = R.string.matches_player2_label),
                selectedPlayerId = formState.jogador2Id,
                players = availableJogadores2,
                onPlayerSelected = { onEvent(PartidaFormEvent.UpdateJogador2(it)) },
                modifier = Modifier.weight(1f),
                enabled = availableJogadores2.isNotEmpty()
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Team and Score Selection
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Player 1 Team Selection
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                GenericDropdown(
                    label = "Liga J1",
                    selectedValue = formState.liga1,
                    items = getAvailableLeagues(),
                    onItemSelected = { onEvent(PartidaFormEvent.UpdateLiga1(it)) }
                )
                GenericDropdown(
                    label = stringResource(id = R.string.matches_team1_label),
                    selectedValue = formState.time1Nome,
                    items = getTeamsForLeague(formState.liga1).map { it.nome },
                    onItemSelected = { onEvent(PartidaFormEvent.UpdateTime1(it)) },
                    enabled = formState.liga1.isNotBlank()
                )
            }
            // Score
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 24.dp) // Align with text fields
            ) {
                val goalsLabel = stringResource(id = R.string.matches_goals_label)
                OutlinedTextField(
                    value = formState.placar1,
                    onValueChange = { onEvent(PartidaFormEvent.UpdatePlacar1(it)) },
                    label = { Text(goalsLabel) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(80.dp),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
                Text(text = "x", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 8.dp))
                OutlinedTextField(
                    value = formState.placar2,
                    onValueChange = { onEvent(PartidaFormEvent.UpdatePlacar2(it)) },
                    label = { Text(goalsLabel) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(80.dp),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
            }
            // Player 2 Team Selection
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                GenericDropdown(
                    label = "Liga J2",
                    selectedValue = formState.liga2,
                    items = getAvailableLeagues(),
                    onItemSelected = { onEvent(PartidaFormEvent.UpdateLiga2(it)) }
                )
                GenericDropdown(
                    label = stringResource(id = R.string.matches_team2_label),
                    selectedValue = formState.time2Nome,
                    items = getTeamsForLeague(formState.liga2).map { it.nome },
                    onItemSelected = { onEvent(PartidaFormEvent.UpdateTime2(it)) },
                    enabled = formState.liga2.isNotBlank()
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onEvent(PartidaFormEvent.RegisterPartida) },
            modifier = Modifier.fillMaxWidth(),
            enabled = formState.isFormValid
        ) {
            Text(stringResource(id = R.string.home_register_match))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerDropdown(
    label: String,
    selectedPlayerId: UUID?,
    players: List<JogadorEntity>,
    onPlayerSelected: (UUID) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val selectedPlayerName = players.find { it.id == selectedPlayerId }?.nome ?: ""
    GenericDropdown(
        label = label,
        selectedValue = selectedPlayerName,
        items = players.map { it.nome },
        onItemSelected = { selectedName ->
            players.find { it.nome == selectedName }?.id?.let { onPlayerSelected(it) }
        },
        modifier = modifier,
        enabled = enabled,
        itemToValue = { selectedName ->
            players.find { it.nome == selectedName }?.let { it.nome } ?: ""
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenericDropdown(
    label: String,
    selectedValue: String,
    items: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemToValue: (String) -> String = { it }
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            enabled = enabled
        )
        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(itemToValue(item))
                        expanded = false
                    }
                )
            }
        }
    }
}