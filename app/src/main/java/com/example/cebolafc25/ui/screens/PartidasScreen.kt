package com.example.cebolafc25.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.cebolafc25.data.model.PartidaEntity
import com.example.cebolafc25.domain.viewmodel.PartidaFormEvent
import com.example.cebolafc25.domain.viewmodel.PartidaFormState
import com.example.cebolafc25.domain.viewmodel.PartidasViewModel
import com.example.cebolafc25.domain.viewmodel.UiEvent
import java.time.format.DateTimeFormatter
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
            if (partidas.isEmpty() && jogadores.size < 2) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(id = R.string.matches_need_2_players),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else if (partidas.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(stringResource(id = R.string.matches_none_registered), style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(top = 16.dp, bottom = 8.dp)) {
                    items(partidas, key = { it.id }) { partida ->
                        PartidaCard(partida = partida, getNomeJogador = { id ->
                            jogadores.find { it.id == id }?.nome ?: stringResource(id = R.string.matches_player_unknown)
                        })
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            MatchRegistrationForm(
                formState = formState,
                onEvent = viewModel::onEvent,
                jogadores = jogadores
            )
        }
    }
}

@Composable
fun MatchRegistrationForm(
    formState: PartidaFormState,
    onEvent: (PartidaFormEvent) -> Unit,
    jogadores: List<JogadorEntity>
) {
    val availableJogadores2 = jogadores.filter { it.id != formState.jogador1Id }

    Column(modifier = Modifier.heightIn(max = 350.dp)) {
        Text(
            text = stringResource(id = R.string.matches_register_new),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val goalsLabel = stringResource(id = R.string.matches_goals_label)
            OutlinedTextField(value = formState.time1Nome, onValueChange = { onEvent(PartidaFormEvent.UpdateTime1(it)) }, label = { Text(stringResource(id = R.string.matches_team1_label)) }, modifier = Modifier.weight(1f), singleLine = true)
            OutlinedTextField(value = formState.placar1, onValueChange = { onEvent(PartidaFormEvent.UpdatePlacar1(it)) }, label = { Text(goalsLabel) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.width(80.dp), textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center))
            Text(text = "x", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(value = formState.placar2, onValueChange = { onEvent(PartidaFormEvent.UpdatePlacar2(it)) }, label = { Text(goalsLabel) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.width(80.dp), textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center))
            OutlinedTextField(value = formState.time2Nome, onValueChange = { onEvent(PartidaFormEvent.UpdateTime2(it)) }, label = { Text(stringResource(id = R.string.matches_team2_label)) }, modifier = Modifier.weight(1f), singleLine = true)
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
fun PlayerDropdown(
    label: String,
    selectedPlayerId: UUID?,
    players: List<JogadorEntity>,
    onPlayerSelected: (UUID) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedPlayerName = players.find { it.id == selectedPlayerId }?.nome ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if(enabled) expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedPlayerName,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            enabled = enabled
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            players.forEach { jogador ->
                DropdownMenuItem(
                    text = { Text(jogador.nome) },
                    onClick = {
                        onPlayerSelected(jogador.id)
                        expanded = false
                    }
                    // CORREÇÃO: Removido o modificador .clickable {} redundante.
                    // O parâmetro onClick do DropdownMenuItem já gerencia a ação de clique.
                )
            }
        }
    }
}

@Composable
fun PartidaCard(partida: PartidaEntity, getNomeJogador: @Composable (UUID) -> String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.matches_date_prefix, partida.data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerResultColumn(
                    playerName = getNomeJogador(partida.jogador1Id),
                    teamName = partida.time1Nome
                )
                Text(
                    text = "${partida.placar1} x ${partida.placar2}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                PlayerResultColumn(
                    playerName = getNomeJogador(partida.jogador2Id),
                    teamName = partida.time2Nome
                )
            }
        }
    }
}

@Composable
fun RowScope.PlayerResultColumn(playerName: String, teamName: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Text(text = playerName, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, maxLines = 1)
        Text(
            text = teamName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}