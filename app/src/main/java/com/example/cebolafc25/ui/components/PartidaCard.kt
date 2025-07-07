package com.example.cebolafc25.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cebolafc25.R
import com.example.cebolafc25.data.model.PartidaEntity
import java.time.format.DateTimeFormatter

@Composable
fun PartidaCard(
    partida: PartidaEntity,
    nomeJogador1: String,
    nomeJogador2: String
) {
    val vencedorId = when {
        partida.placar1 > partida.placar2 -> partida.jogador1Id
        partida.placar2 > partida.placar1 -> partida.jogador2Id
        else -> null
    }
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerResultColumn(
                    playerName = nomeJogador1,
                    teamName = partida.time1Nome,
                    isWinner = vencedorId == partida.jogador1Id
                )
                Text(
                    text = "${partida.placar1} x ${partida.placar2}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                PlayerResultColumn(
                    playerName = nomeJogador2,
                    teamName = partida.time2Nome,
                    isWinner = vencedorId == partida.jogador2Id
                )
            }
        }
    }
}

@Composable
private fun RowScope.PlayerResultColumn(playerName: String, teamName: String, isWinner: Boolean) {
    val fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal
    val color = if (isWinner) MaterialTheme.colorScheme.primary else Color.Unspecified
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = playerName,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            fontWeight = fontWeight,
            color = color
        )
        Text(
            text = teamName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}