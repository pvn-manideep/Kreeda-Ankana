@file:OptIn(ExperimentalMaterial3Api::class)
package com.kreeda.ankana.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreeda.ankana.model.Challenge
import com.kreeda.ankana.model.Slot
import com.kreeda.ankana.model.Team
import com.kreeda.ankana.ui.theme.KC
import com.kreeda.ankana.ui.theme.ThemeState
import com.kreeda.ankana.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val active by viewModel.allActiveSlots.collectAsState()
    val challenges by viewModel.challenges.collectAsState()
    val rankings by viewModel.rankings.collectAsState()
    val team by viewModel.userTeam.collectAsState()
    val today = viewModel.todayDate
    val sdf = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    val sdfParse = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayDisplay = try { sdf.format(sdfParse.parse(today)!!) } catch (e: Exception) { today }

    val live = active.filter { it.status == "Playing" }
    val upcoming = active.filter { it.status == "Booked" }
    val openChallenges = challenges.filter { it.status == "pending" }

    LazyColumn(Modifier.fillMaxSize()) {
        // ── Hero Header ──────────────────────────────────────────────────
        item {
            Box(
                Modifier.fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(KC.Violet.copy(0.2f), Color.Transparent)))
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(todayDisplay, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(2.dp))
                            Text(
                                if (team != null) "Hey, ${team!!.name} 👋" else "Welcome back! 👋",
                                fontSize = 24.sp, fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        // Theme toggle
                        IconButton(onClick = { ThemeState.isDark = !ThemeState.isDark }) {
                            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
                                Icon(
                                    if (ThemeState.isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    "Toggle theme",
                                    modifier = Modifier.padding(8.dp).size(18.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    // Stats row
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatCard("${live.size}", "Live Now", KC.SportRed, Icons.Default.RadioButtonChecked, Modifier.weight(1f))
                        StatCard("${upcoming.size}", "Upcoming", KC.Violet, Icons.Default.CalendarMonth, Modifier.weight(1f))
                        StatCard("${openChallenges.size}", "Challenges", KC.SportAmber, Icons.Default.FlashOn, Modifier.weight(1f))
                    }
                }
            }
        }

        // ── Live Matches ─────────────────────────────────────────────────
        if (live.isNotEmpty()) {
            item { SectionHeader("Live Now", KC.SportRed, Icons.Default.RadioButtonChecked) }
            items(live) { slot -> LiveMatchCard(slot) }
        }

        // ── Open Challenges ───────────────────────────────────────────────
        if (openChallenges.isNotEmpty()) {
            item { SectionHeader("Open Challenges", KC.SportAmber, Icons.Default.FlashOn) }
            items(openChallenges.take(3)) { ch ->
                HomeChallengeCard(ch,
                    canAccept = team != null && ch.fromTeamUid != team!!.uid,
                    onAccept = { viewModel.acceptChallenge(ch) })
            }
        }

        // ── Upcoming ──────────────────────────────────────────────────────
        if (upcoming.isNotEmpty()) {
            item { SectionHeader("Coming Up", KC.Violet, Icons.Default.CalendarMonth) }
            items(upcoming.take(4)) { slot -> UpcomingMatchCard(slot) }
        }

        if (live.isEmpty() && upcoming.isEmpty() && openChallenges.isEmpty()) {
            item {
                Column(
                    Modifier.fillMaxWidth().padding(top = 60.dp, start = 32.dp, end = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(Modifier.size(90.dp).clip(CircleShape)
                        .background(Brush.radialGradient(listOf(KC.Violet.copy(0.2f), Color.Transparent))),
                        contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.SportsScore, null, tint = KC.Violet, modifier = Modifier.size(44.dp))
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("Ground is ready!", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(8.dp))
                    Text("No matches yet today.\nBook a slot to start playing!",
                        textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // ── Top Teams ─────────────────────────────────────────────────────
        if (rankings.isNotEmpty()) {
            item { SectionHeader("Top Teams", KC.SportAmber, Icons.Default.Leaderboard) }
            items(rankings.take(5).mapIndexed { i, t -> Pair(i, t) }) { (i, team) ->
                TopTeamRow(i + 1, team)
            }
        }

        item { Spacer(Modifier.height(20.dp)) }
    }
}

@Composable
fun StatCard(value: String, label: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(Modifier.padding(12.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(Modifier.height(6.dp))
            Text(value, fontWeight = FontWeight.Black, fontSize = 22.sp, color = color)
            Text(label, fontSize = 10.sp, color = color.copy(0.8f), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun SectionHeader(title: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(title.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
            color = color, letterSpacing = 1.5.sp)
    }
}

@Composable
fun LiveMatchCard(slot: Slot) {
    val pulse = rememberInfiniteTransition("pulse")
    val alpha by pulse.animateFloat(0.5f, 1f,
        infiniteRepeatable(tween(700, easing = EaseInOut), RepeatMode.Reverse), "a")

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.5.dp, KC.SportRed.copy(alpha))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(10.dp).clip(CircleShape).background(KC.SportRed.copy(alpha)))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("${slot.bookedByTeam}  vs  ${slot.opponentTeam.ifBlank{"????"}}",
                    fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                Text("${slot.sport} · ${slot.time}", fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(shape = RoundedCornerShape(8.dp), color = KC.SportRed.copy(0.15f)) {
                Text("LIVE", modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = KC.SportRed, letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
fun UpcomingMatchCard(slot: Slot) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(10.dp), color = KC.Violet.copy(0.15f),
                modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Text(sportEmoji(slot.sport), fontSize = 22.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(slot.bookedByTeam, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("${slot.sport} · ${slot.time} · ${slot.date}", fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (slot.opponentTeam.isNotBlank())
                    Text("vs ${slot.opponentTeam}", fontSize = 12.sp, color = KC.SportGreen,
                        fontWeight = FontWeight.SemiBold)
                else
                    Text("Seeking opponent…", fontSize = 11.sp, color = KC.SportAmber)
            }
        }
    }
}

@Composable
fun HomeChallengeCard(ch: Challenge, canAccept: Boolean, onAccept: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(16.dp),
        color = KC.SportAmber.copy(0.07f),
        border = BorderStroke(1.dp, KC.SportAmber.copy(0.25f))
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(sportEmoji(ch.sport), fontSize = 26.sp)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(ch.fromTeam, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                Text("${ch.sport} · ${ch.time} · ${ch.date}", fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (ch.message.isNotBlank())
                    Text("\"${ch.message}\"", fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
            if (canAccept) {
                Button(onClick = onAccept,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KC.SportAmber)) {
                    Text("Accept", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun TopTeamRow(rank: Int, team: Team) {
    val medal = when(rank) { 1 -> "🥇"; 2 -> "🥈"; 3 -> "🥉"; else -> "#$rank" }
    val accent = when(rank) { 1 -> KC.SportAmber; 2 -> KC.DarkSubText; 3 -> Color(0xFFCD7F32); else -> MaterialTheme.colorScheme.onSurfaceVariant }
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(medal, fontSize = if (rank <= 3) 20.sp else 14.sp,
                modifier = Modifier.width(36.dp), textAlign = TextAlign.Center,
                color = accent, fontWeight = FontWeight.ExtraBold)
            Box(Modifier.size(36.dp).clip(CircleShape).background(accent.copy(0.15f)),
                contentAlignment = Alignment.Center) {
                Text(team.name.take(2).uppercase(), fontWeight = FontWeight.Black,
                    color = accent, fontSize = 12.sp)
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(team.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("${team.sport} · ${team.village}", fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${team.wins}W", fontWeight = FontWeight.ExtraBold, color = KC.SportGreen, fontSize = 14.sp)
                Text("${team.losses}L", fontSize = 11.sp, color = KC.SportRed)
            }
        }
    }
}

fun sportEmoji(sport: String?) = when(sport) {
    "Volleyball" -> "🏐"; "Cricket" -> "🏏"; "Football" -> "⚽"
    "Kabaddi" -> "🤼"; "Badminton" -> "🏸"; "Kho-Kho" -> "🏃"; "Tennis" -> "🎾"
    else -> "🏟"
}
