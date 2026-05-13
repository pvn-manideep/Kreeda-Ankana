@file:OptIn(ExperimentalMaterial3Api::class)
package com.kreeda.ankana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreeda.ankana.model.MatchScore
import com.kreeda.ankana.model.Slot
import com.kreeda.ankana.model.Team
import com.kreeda.ankana.ui.theme.KC
import com.kreeda.ankana.viewmodel.MainViewModel

@Composable
fun ScoreboardScreen(viewModel: MainViewModel) {
    val scores by viewModel.scores.collectAsState()
    val rankings by viewModel.rankings.collectAsState()
    val team by viewModel.userTeam.collectAsState()
    val allActive by viewModel.allActiveSlots.collectAsState()
    val isAdmin = team?.role == "admin"
    var tab by remember { mutableIntStateOf(0) }
    var showScoreDialog by remember { mutableStateOf<Slot?>(null) }

    Column(Modifier.fillMaxSize()) {
        // Header
        Box(
            Modifier.fillMaxWidth()
                .background(Brush.verticalGradient(listOf(KC.SportAmber.copy(0.15f), Color.Transparent)))
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(40.dp).clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(listOf(KC.SportAmber, Color(0xFFFF6B35)))),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.Leaderboard, null, tint = Color.White, modifier = Modifier.size(20.dp)) }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Scoreboard", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                    Text("Rankings · Results${if (isAdmin) " · Admin" else ""}",
                        fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        TabRow(
            selectedTabIndex = tab,
            containerColor = Color.Transparent,
            contentColor = KC.SportAmber,
            indicator = { tabPositions ->
                if (tab < tabPositions.size) {
                    Box(Modifier.tabIndicatorOffset(tabPositions[tab])
                        .height(3.dp).clip(RoundedCornerShape(2.dp)).background(KC.SportAmber))
                }
            },
            divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.2f)) }
        ) {
            listOf("Rankings", "Results", if (isAdmin) "Post Score" else null)
                .filterNotNull().forEachIndexed { i, title ->
                    Tab(selected = tab == i, onClick = { tab = i }) {
                        Text(title, modifier = Modifier.padding(vertical = 12.dp),
                            fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
        }

        when (tab) {
            0 -> RankingsTab(rankings)
            1 -> ResultsTab(scores)
            2 -> if (isAdmin) AdminPostScoreTab(
                slots = allActive.filter { it.status in listOf("Playing","Booked") && it.opponentTeam.isNotBlank() },
                onPostScore = { showScoreDialog = it }
            )
        }
    }

    showScoreDialog?.let { slot ->
        PostScoreDialog(
            slot = slot,
            onDismiss = { showScoreDialog = null },
            onConfirm = { s1, s2, notes ->
                viewModel.postScore(slot, s1, s2, notes)
                showScoreDialog = null
            }
        )
    }
}

@Composable
fun RankingsTab(rankings: List<Team>) {
    if (rankings.isEmpty()) {
        EmptyState(Icons.Default.Leaderboard, "No teams yet.\nRegister from the Profile tab!")
        return
    }
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (rankings.size >= 3) {
            item {
                // Podium
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        Modifier.fillMaxWidth()
                            .background(Brush.verticalGradient(listOf(KC.SportAmber.copy(0.08f), Color.Transparent)),
                                RoundedCornerShape(20.dp))
                            .padding(20.dp)
                    ) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom) {
                            PodiumPillar(rankings[1], 2)
                            PodiumPillar(rankings[0], 1)
                            PodiumPillar(rankings[2], 3)
                        }
                    }
                }
            }
        }
        itemsIndexed(rankings.drop(if (rankings.size >= 3) 3 else 0)) { i, t ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("#${i + 4}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(32.dp))
                    Box(Modifier.size(38.dp).clip(CircleShape).background(KC.Violet.copy(0.15f)),
                        contentAlignment = Alignment.Center) {
                        Text(t.name.take(2).uppercase(), fontWeight = FontWeight.Black,
                            color = KC.Violet, fontSize = 13.sp)
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(t.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${t.sport} · ${t.village}", fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("${t.wins}W / ${t.losses}L", fontWeight = FontWeight.Bold,
                        color = KC.SportGreen, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun PodiumPillar(team: Team, rank: Int) {
    val (medal, color) = when (rank) {
        1 -> "🥇" to KC.SportAmber
        2 -> "🥈" to Color(0xFFB0BEC5)
        else -> "🥉" to Color(0xFFCD7F32)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(medal, fontSize = if (rank == 1) 32.sp else 26.sp)
        Spacer(Modifier.height(6.dp))
        Box(Modifier.size(if (rank == 1) 52.dp else 44.dp).clip(CircleShape)
            .background(color.copy(0.2f)), contentAlignment = Alignment.Center) {
            Text(team.name.take(2).uppercase(), fontWeight = FontWeight.Black,
                color = color, fontSize = if (rank == 1) 18.sp else 14.sp)
        }
        Spacer(Modifier.height(6.dp))
        Text(team.name, fontWeight = FontWeight.Bold,
            fontSize = if (rank == 1) 13.sp else 11.sp, textAlign = TextAlign.Center, maxLines = 1)
        Text("${team.wins}W", fontWeight = FontWeight.ExtraBold, color = color,
            fontSize = if (rank == 1) 14.sp else 12.sp)
    }
}

@Composable
fun ResultsTab(scores: List<MatchScore>) {
    if (scores.isEmpty()) {
        EmptyState(Icons.Default.SportsScore, "No match results yet.\nAdmin will post scores after games.")
        return
    }
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(scores) { score ->
            Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(sportEmoji(score.sport), fontSize = 18.sp)
                            Spacer(Modifier.width(6.dp))
                            Text(score.sport, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = KC.Violet)
                        }
                        Text(score.date, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(Modifier.height(14.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically) {
                        val t1Wins = score.winner == score.team1
                        // Team 1
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                            if (t1Wins) Text("WINNER", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold,
                                color = KC.SportGreen, letterSpacing = 1.sp)
                            Text(score.team1, fontWeight = FontWeight.Bold, fontSize = 13.sp, textAlign = TextAlign.Center, maxLines = 2)
                            Spacer(Modifier.height(4.dp))
                            Text("${score.score1}", fontSize = 38.sp, fontWeight = FontWeight.Black,
                                color = if (t1Wins) KC.SportGreen else MaterialTheme.colorScheme.onSurface)
                        }
                        // VS
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("VS", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (score.winner == "Draw") {
                                Spacer(Modifier.height(4.dp))
                                Surface(shape = RoundedCornerShape(6.dp), color = KC.SportAmber.copy(0.15f)) {
                                    Text("DRAW", modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                        fontSize = 9.sp, color = KC.SportAmber, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                        // Team 2
                        val t2Wins = score.winner == score.team2
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                            if (t2Wins) Text("WINNER", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold,
                                color = KC.SportGreen, letterSpacing = 1.sp)
                            Text(score.team2, fontWeight = FontWeight.Bold, fontSize = 13.sp, textAlign = TextAlign.Center, maxLines = 2)
                            Spacer(Modifier.height(4.dp))
                            Text("${score.score2}", fontSize = 38.sp, fontWeight = FontWeight.Black,
                                color = if (t2Wins) KC.SportGreen else MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    if (score.notes.isNotBlank()) {
                        Spacer(Modifier.height(10.dp))
                        Text("📝 ${score.notes}", fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminPostScoreTab(slots: List<Slot>, onPostScore: (Slot) -> Unit) {
    if (slots.isEmpty()) {
        EmptyState(Icons.Default.EmojiEvents, "No confirmed matches yet.\nBoth teams must accept a challenge first.")
        return
    }
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Text("SELECT MATCH TO POST SCORE", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 4.dp))
        }
        items(slots) { slot ->
            Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(sportEmoji(slot.sport), fontSize = 22.sp)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("${slot.bookedByTeam} vs ${slot.opponentTeam}",
                            fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${slot.sport} · ${slot.time} · ${slot.date}", fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Button(onClick = { onPostScore(slot) }, shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KC.SportGreen)) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(Modifier.width(4.dp))
                        Text("Score", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PostScoreDialog(slot: Slot, onDismiss: () -> Unit, onConfirm: (Int, Int, String) -> Unit) {
    var s1 by remember { mutableStateOf("") }
    var s2 by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        title = {
            Column {
                Text("Post Score", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Text("${slot.bookedByTeam} vs ${slot.opponentTeam}",
                    fontSize = 13.sp, color = KC.SportGreen, fontWeight = FontWeight.SemiBold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = s1, onValueChange = { s1 = it },
                        label = { Text(slot.bookedByTeam.take(10)) },
                        modifier = Modifier.weight(1f), singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = s2, onValueChange = { s2 = it },
                        label = { Text((slot.opponentTeam).take(10)) },
                        modifier = Modifier.weight(1f), singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                OutlinedTextField(
                    value = notes, onValueChange = { notes = it },
                    label = { Text("Match notes (optional)") },
                    modifier = Modifier.fillMaxWidth(), maxLines = 2,
                    shape = RoundedCornerShape(12.dp)
                )
                // Winner preview
                val score1 = s1.toIntOrNull() ?: -1
                val score2 = s2.toIntOrNull() ?: -1
                if (score1 >= 0 && score2 >= 0) {
                    val winner = when { score1 > score2 -> slot.bookedByTeam; score2 > score1 -> slot.opponentTeam; else -> "Draw" }
                    Surface(shape = RoundedCornerShape(10.dp), color = KC.SportGreen.copy(0.1f)) {
                        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EmojiEvents, null, tint = KC.SportGreen, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Winner: $winner", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = KC.SportGreen)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(s1.toIntOrNull() ?: 0, s2.toIntOrNull() ?: 0, notes) },
                enabled = s1.isNotBlank() && s2.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KC.SportGreen)
            ) { Text("Post Score", fontWeight = FontWeight.Bold) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun EmptyState(icon: androidx.compose.ui.graphics.vector.ImageVector, message: String) {
    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(80.dp).clip(CircleShape).background(KC.SportAmber.copy(0.1f)),
                contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = KC.SportAmber.copy(0.5f), modifier = Modifier.size(36.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text(message, textAlign = TextAlign.Center, fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
