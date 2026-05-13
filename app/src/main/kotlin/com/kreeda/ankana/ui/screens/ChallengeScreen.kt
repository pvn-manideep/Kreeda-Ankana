@file:OptIn(ExperimentalMaterial3Api::class)
package com.kreeda.ankana.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.kreeda.ankana.model.Challenge
import com.kreeda.ankana.ui.theme.KC
import com.kreeda.ankana.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChallengeScreen(viewModel: MainViewModel) {
    val challenges by viewModel.challenges.collectAsState()
    val team by viewModel.userTeam.collectAsState()
    var tab by remember { mutableIntStateOf(0) }

    val myUid = team?.uid
    val openChallenges = challenges.filter { it.status == "pending" && it.toTeam == "OPEN" }
    val directChallenges = challenges.filter { it.status == "pending" && it.toTeam != "OPEN" }
    val myChallenges = challenges.filter { it.fromTeamUid == myUid }

    Column(Modifier.fillMaxSize()) {
        // Header
        Box(
            Modifier.fillMaxWidth()
                .background(Brush.verticalGradient(listOf(KC.SportAmber.copy(0.15f), Color.Transparent)))
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(40.dp).clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(listOf(KC.SportAmber, Color(0xFFFF6B35)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.FlashOn, null, tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Battle Arena", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                        Text("Challenge teams · Accept matches", fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = tab,
            containerColor = Color.Transparent,
            contentColor = KC.SportAmber,
            indicator = { tabPositions ->
                if (tab < tabPositions.size) {
                    Box(Modifier.tabIndicatorOffset(tabPositions[tab])
                        .height(3.dp).clip(RoundedCornerShape(2.dp))
                        .background(KC.SportAmber))
                }
            },
            divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.2f)) }
        ) {
            listOf(
                "Open (${openChallenges.size})",
                "Direct (${directChallenges.size})",
                "Mine (${myChallenges.size})"
            ).forEachIndexed { i, title ->
                Tab(selected = tab == i, onClick = { tab = i }) {
                    Text(title, modifier = Modifier.padding(vertical = 12.dp),
                        fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        when (tab) {
            0 -> ChallengeList(
                challenges = openChallenges,
                myUid = myUid,
                emptyMsg = "No open challenges right now.\nBook a slot and post one from the Booking tab!",
                showAccept = true,
                showDecline = false,
                onAccept = { viewModel.acceptChallenge(it) },
                onDecline = {}
            )
            1 -> ChallengeList(
                challenges = directChallenges,
                myUid = myUid,
                emptyMsg = "No direct challenges sent to you.",
                showAccept = true,
                showDecline = true,
                onAccept = { viewModel.acceptChallenge(it) },
                onDecline = { viewModel.declineChallenge(it) }
            )
            2 -> ChallengeList(
                challenges = myChallenges,
                myUid = myUid,
                emptyMsg = "You haven't posted any challenges yet.",
                showAccept = false,
                showDecline = false,
                onAccept = {},
                onDecline = {}
            )
        }
    }
}

@Composable
fun ChallengeList(
    challenges: List<Challenge>,
    myUid: String?,
    emptyMsg: String,
    showAccept: Boolean,
    showDecline: Boolean,
    onAccept: (Challenge) -> Unit,
    onDecline: (Challenge) -> Unit
) {
    if (challenges.isEmpty()) {
        Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.size(80.dp).clip(CircleShape)
                        .background(KC.SportAmber.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FlashOff, null, tint = KC.SportAmber.copy(0.5f),
                        modifier = Modifier.size(36.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text(emptyMsg, textAlign = TextAlign.Center, fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        return
    }

    var confirmAccept by remember { mutableStateOf<Challenge?>(null) }
    var confirmDecline by remember { mutableStateOf<Challenge?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(challenges, key = { it.id }) { ch ->
            FullChallengeCard(
                challenge = ch,
                isMyChallenge = ch.fromTeamUid == myUid,
                canAccept = showAccept && ch.fromTeamUid != myUid,
                canDecline = showDecline && ch.fromTeamUid != myUid,
                onAccept = { confirmAccept = ch },
                onDecline = { confirmDecline = ch }
            )
        }
    }

    confirmAccept?.let { ch ->
        AlertDialog(
            onDismissRequest = { confirmAccept = null },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            icon = { Text("⚔️", fontSize = 28.sp) },
            title = { Text("Accept Challenge?", fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center) },
            text = {
                Text(
                    "You're accepting ${ch.fromTeam}'s challenge for ${ch.sport} at ${ch.time} on ${ch.date}.\n\nThe slot will be marked as PLAYING with your team as opponent.",
                    textAlign = TextAlign.Center, fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { onAccept(ch); confirmAccept = null },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KC.SportGreen)
                ) { Text("Accept — Let's play! 🏆", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { confirmAccept = null },
                    modifier = Modifier.fillMaxWidth()) {
                    Text("Cancel")
                }
            }
        )
    }

    confirmDecline?.let { ch ->
        AlertDialog(
            onDismissRequest = { confirmDecline = null },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            title = { Text("Decline Challenge?", fontWeight = FontWeight.Bold) },
            text = { Text("Decline ${ch.fromTeam}'s ${ch.sport} challenge?") },
            confirmButton = {
                Button(
                    onClick = { onDecline(ch); confirmDecline = null },
                    colors = ButtonDefaults.buttonColors(containerColor = KC.SportRed)
                ) { Text("Decline") }
            },
            dismissButton = { TextButton(onClick = { confirmDecline = null }) { Text("Keep Open") } }
        )
    }
}

@Composable
fun FullChallengeCard(
    challenge: Challenge, isMyChallenge: Boolean,
    canAccept: Boolean, canDecline: Boolean,
    onAccept: () -> Unit, onDecline: () -> Unit
) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val disp = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    val dateDisplay = try { disp.format(sdf.parse(challenge.date)!!) } catch (e: Exception) { challenge.date }
    var expanded by remember { mutableStateOf(false) }
    val borderColor = if (isMyChallenge) KC.Violet else KC.SportAmber

    Surface(
        onClick = { expanded = !expanded },
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, borderColor.copy(0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Sport icon box
                Box(
                    Modifier.size(48.dp).clip(RoundedCornerShape(14.dp))
                        .background(borderColor.copy(0.12f)),
                    contentAlignment = Alignment.Center
                ) { Text(sportEmoji(challenge.sport), fontSize = 24.sp) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(challenge.fromTeam, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        if (isMyChallenge) {
                            Spacer(Modifier.width(6.dp))
                            Surface(shape = RoundedCornerShape(4.dp), color = KC.Violet.copy(0.2f)) {
                                Text("YOU", modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                    fontSize = 8.sp, color = KC.Violet, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                    Text("${challenge.sport} · $dateDisplay · ${challenge.time}",
                        fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                // Status badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (challenge.status) {
                        "accepted" -> KC.SportGreen.copy(0.15f)
                        "declined" -> KC.SportRed.copy(0.15f)
                        else -> KC.SportAmber.copy(0.15f)
                    }
                ) {
                    Text(
                        challenge.status.replaceFirstChar { it.uppercase() },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                        color = when (challenge.status) {
                            "accepted" -> KC.SportGreen; "declined" -> KC.SportRed; else -> KC.SportAmber
                        }
                    )
                }
            }

            // Expandable section
            AnimatedVisibility(expanded || challenge.message.isNotBlank(), enter = expandVertically(), exit = shrinkVertically()) {
                Column {
                    if (challenge.message.isNotBlank()) {
                        Spacer(Modifier.height(12.dp))
                        Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surface) {
                            Row(Modifier.padding(10.dp)) {
                                Icon(Icons.Default.FormatQuote, null,
                                    tint = borderColor, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(challenge.message, fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.8f))
                            }
                        }
                    }
                    if ((canAccept || canDecline) && challenge.status == "pending") {
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            if (canDecline) {
                                OutlinedButton(
                                    onClick = onDecline,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, KC.SportRed.copy(0.5f)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = KC.SportRed)
                                ) {
                                    Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Decline", fontWeight = FontWeight.Bold)
                                }
                            }
                            Button(
                                onClick = onAccept,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = KC.SportGreen)
                            ) {
                                Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp), tint = Color.White)
                                Spacer(Modifier.width(4.dp))
                                Text("Accept ⚔️", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            if (!expanded && challenge.message.isBlank() && (canAccept || canDecline)) {
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { expanded = true }, modifier = Modifier.align(Alignment.End),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)) {
                    Text("Tap to respond", fontSize = 11.sp, color = borderColor)
                    Icon(Icons.Default.ExpandMore, null, modifier = Modifier.size(14.dp), tint = borderColor)
                }
            }
        }
    }
}
