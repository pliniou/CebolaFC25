package com.example.cebolafc25.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cebolafc25.R
import com.example.cebolafc25.data.model.CampeonatoEntity
import com.example.cebolafc25.domain.model.TipoCampeonato
import com.example.cebolafc25.domain.viewmodel.CampeonatosViewModel
import com.example.cebolafc25.navigation.TOURNAMENT_DETAILS_ROUTE
import java.time.format.DateTimeFormatter
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampeonatosScreen(
    navController: NavController,
    viewModel: CampeonatosViewModel = hiltViewModel()
) {
    val campeonatos by viewModel.campeonatos.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val jogadores by viewModel.jogadores.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(id = R.string.tournaments_title)) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Formulário de Criação como primeiro item da lista
            item {
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(id = R.string.tournaments_create_new), style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = formState.nome,
                            onValueChange = viewModel::onNomeChange,
                            label = { Text(stringResource(id = R.string.tournaments_name_label)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TipoCampeonatoDropdown(
                            selectedType = formState.tipo,
                            types = viewModel.tiposDeCampeonato,
                            onTypeSelected = viewModel::onTipoChange
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("Selecione os Participantes (mínimo 2)", style = MaterialTheme.typography.titleMedium)
                        jogadores.forEach { jogador ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onParticipanteSelected(jogador.id, !formState.participantesSelecionados.contains(jogador.id))
                                    }
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = formState.participantesSelecionados.contains(jogador.id),
                                    onCheckedChange = { isSelected ->
                                        viewModel.onParticipanteSelected(jogador.id, isSelected)
                                    }
                                )
                                Text(text = jogador.nome, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = viewModel::createCampeonato,
                            enabled = formState.nome.isNotBlank() && formState.participantesSelecionados.size >= 2,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            val createText = stringResource(id = R.string.tournaments_create_button)
                            Icon(Icons.Default.Add, contentDescription = createText)
                            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                            Text(createText)
                        }
                    }
                }
            }

            // Cabeçalho da lista de campeonatos existentes
            if (campeonatos.isNotEmpty()) {
                item {
                    Text(
                        stringResource(id = R.string.tournaments_existing_title),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }

            // Lista de Campeonatos
            items(items = campeonatos, key = { it.id }) { camp ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            navController.navigate("$TOURNAMENT_DETAILS_ROUTE/${camp.id}")
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(camp.nome, style = MaterialTheme.typography.titleMedium)
                            Text(camp.tipo, style = MaterialTheme.typography.bodySmall)
                        }
                        Text(
                            text = camp.dataCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yy")),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            
            if (campeonatos.isEmpty()) {
                 item {
                    Text(stringResource(id = R.string.tournaments_none_created))
                 }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipoCampeonatoDropdown(
    selectedType: TipoCampeonato,
    types: List<TipoCampeonato>,
    onTypeSelected: (TipoCampeonato) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedType.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(id = R.string.tournaments_type_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}