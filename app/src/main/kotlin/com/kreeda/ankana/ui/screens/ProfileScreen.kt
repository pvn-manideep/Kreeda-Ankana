@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
package com.kreeda.ankana.ui.screens

import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.kreeda.ankana.model.AppNotification
import com.kreeda.ankana.model.Slot
import com.kreeda.ankana.model.Team
import com.kreeda.ankana.ui.theme.KC
import com.kreeda.ankana.viewmodel.MainViewModel

@Composable
fun ProfileScreen(viewModel: MainViewModel) {
    val team by viewModel.userTeam.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val mySlots by viewModel.mySlots.collectAsState()
    val unreadCount by viewModel.unreadCount.collectAsState()
    val allActive by viewModel.allActiveSlots.collectAsState()
    val rankings by viewModel.rankings.collectAsState()
    val isAdmin = team?.role == "admin"
    var tab by remember { mutableIntStateOf(0) }
    var showSignOut by remember { mutableStateOf(false) }
    var showGrantAdmin by remember { mutableStateOf(false) }
    var grantEmail by remember { mutableStateOf("") }

    val tabs = buildList {
        add("My Slots")
        add("Inbox${if (unreadCount > 0) " ($unreadCount)" else ""}")
        if (isAdmin) add("Admin 👑")
    }

    Column(Modifier.fillMaxSize()) {
        // Profile header
        Box(
            Modifier.fillMaxWidth()
                .background(Brush.verticalGradient(listOf(KC.Violet.copy(0.2f), Color.Transparent)))
                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 16.dp)
        ) {
            if (team != null) {
                val t = team!!
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar
                        Box(
                            Modifier.size(60.dp).clip(CircleShape)
                                .background(Brush.linearGradient(KC.GradPrimary)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(t.name.take(2).uppercase(), fontWeight = FontWeight.Black,
                                fontSize = 22.sp, color = Color.White)
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(t.name, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                                if (t.role == "admin") {
                                    Spacer(Modifier.width(8.dp))
                                    Surface(shape = RoundedCornerShape(6.dp),
                                        color = KC.SportAmber.copy(0.2f)) {
                                        Text(" 👑 ADMIN ", fontSize = 9.sp, color = KC.SportAmber,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp))
                                    }
                                }
                            }
                            Text("${t.sport} · ${t.village}", fontSize = 13.sp, color = KC.Violet,
                                fontWeight = FontWeight.SemiBold)
                            if (t.email.isNotBlank())
                                Text(t.email, fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { showSignOut = true }) {
                            Icon(Icons.Default.Logout, "Sign out",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    // Stats
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        listOf(
                            Triple(t.wins.toString(), "Wins", KC.SportGreen),
                            Triple(t.losses.toString(), "Losses", KC.SportRed),
                            Triple(t.matchesPlayed.toString(), "Played", KC.Violet),
                            Triple(t.playerCount.toString(), "Players", KC.Cyan)
                        ).forEach { (val_, label, color) ->
                            Surface(shape = RoundedCornerShape(12.dp), color = color.copy(0.1f),
                                modifier = Modifier.weight(1f)) {
                                Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(val_, fontWeight = FontWeight.Black, fontSize = 18.sp, color = color)
                                    Text(label, fontSize = 9.sp, color = color.copy(0.8f),
                                        fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                    if (t.motto.isNotBlank()) {
                        Spacer(Modifier.height(10.dp))
                        Text("\"${t.motto}\"", fontSize = 12.sp, textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }

        TabRow(
            selectedTabIndex = tab, containerColor = Color.Transparent, contentColor = KC.Violet,
            indicator = { tabPositions ->
                if (tab < tabPositions.size) {
                    Box(Modifier.tabIndicatorOffset(tabPositions[tab])
                        .height(3.dp).clip(RoundedCornerShape(2.dp)).background(KC.Violet))
                }
            },
            divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.2f)) }
        ) {
            tabs.forEachIndexed { i, title ->
                Tab(selected = tab == i, onClick = {
                    tab = i
                    if (title.startsWith("Inbox")) viewModel.markAllNotificationsRead()
                }) {
                    Text(title, modifier = Modifier.padding(vertical = 12.dp),
                        fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        when (tab) {
            0 -> MySlotsTab(mySlots, viewModel)
            1 -> NotificationsTab(notifications)
            2 -> if (isAdmin) AdminTab(allActive, rankings, viewModel,
                onGrantAdmin = { showGrantAdmin = true })
        }
    }

    if (showSignOut) {
        AlertDialog(
            onDismissRequest = { showSignOut = false },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            title = { Text("Sign Out?", fontWeight = FontWeight.ExtraBold) },
            text = { Text("You'll need to sign in again to book slots or post challenges.") },
            confirmButton = {
                Button(onClick = { viewModel.signOut(); showSignOut = false },
                    colors = ButtonDefaults.buttonColors(containerColor = KC.SportRed),
                    shape = RoundedCornerShape(12.dp)) { Text("Sign Out", fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showSignOut = false }) { Text("Stay") } }
        )
    }

    if (showGrantAdmin) {
        AlertDialog(
            onDismissRequest = { showGrantAdmin = false },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            title = { Text("Grant Admin Access", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter email to promote to admin.", fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    OutlinedTextField(value = grantEmail, onValueChange = { grantEmail = it },
                        label = { Text("Email") }, modifier = Modifier.fillMaxWidth(),
                        singleLine = true, shape = RoundedCornerShape(12.dp))
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.repository.grantAdmin(grantEmail.trim(), viewModel)
                    showGrantAdmin = false; grantEmail = ""
                }, colors = ButtonDefaults.buttonColors(containerColor = KC.Violet),
                    shape = RoundedCornerShape(12.dp)) { Text("Grant Admin") }
            },
            dismissButton = { TextButton(onClick = { showGrantAdmin = false }) { Text("Cancel") } }
        )
    }
}

@Composable
fun MySlotsTab(mySlots: List<Slot>, viewModel: MainViewModel) {
    val active = mySlots.filter { it.status in listOf("Booked","Playing") }
    val past = mySlots.filter { it.status in listOf("Completed") }
    var showReschedule by remember { mutableStateOf<Slot?>(null) }
    val times = listOf("6 AM","8 AM","10 AM","12 PM","2 PM","4 PM","6 PM","8 PM")

    if (mySlots.isEmpty()) {
        EmptyState(Icons.Default.CalendarMonth, "No bookings yet.\nHead to the Book tab to reserve a slot.")
        return
    }
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (active.isNotEmpty()) {
            item { SectionLabel("UPCOMING") }
            items(active) { slot ->
                BookingRow(slot = slot,
                    onCancel = { viewModel.cancelSlot(slot) },
                    onReschedule = { showReschedule = slot })
            }
        }
        if (past.isNotEmpty()) {
            item { SectionLabel("PAST") }
            items(past) { slot -> BookingRow(slot = slot, isPast = true) }
        }
    }
    showReschedule?.let { slot ->
        RescheduleDialog(slot = slot, dates = viewModel.next14Days, times = times,
            onDismiss = { showReschedule = null },
            onConfirm = { d, t -> viewModel.rescheduleSlot(slot, d, t); showReschedule = null })
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(text, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.5.sp,
        modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun BookingRow(slot: Slot, isPast: Boolean = false, onCancel: () -> Unit = {}, onReschedule: () -> Unit = {}) {
    val statusColor = when(slot.status) {
        "Playing" -> KC.SportRed; "Booked" -> KC.Violet; "Completed" -> KC.SportGreen; else -> KC.DarkSubText
    }
    Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(sportEmoji(slot.sport), fontSize = 26.sp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(slot.sport, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("${slot.time} · ${slot.date}", fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (slot.opponentTeam.isNotBlank())
                    Text("vs ${slot.opponentTeam}", fontSize = 12.sp, color = KC.SportGreen,
                        fontWeight = FontWeight.SemiBold)
                Surface(shape = RoundedCornerShape(5.dp), color = statusColor.copy(0.12f),
                    modifier = Modifier.padding(top = 4.dp)) {
                    Text(slot.status, modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                        fontSize = 9.sp, color = statusColor, fontWeight = FontWeight.ExtraBold)
                }
            }
            if (!isPast) {
                Column(horizontalAlignment = Alignment.End) {
                    IconButton(onClick = onReschedule, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.SwapHoriz, null, tint = KC.SportAmber, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onCancel, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, null, tint = KC.SportRed, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationsTab(notifications: List<AppNotification>) {
    if (notifications.isEmpty()) {
        EmptyState(Icons.Default.Notifications, "No notifications yet.\nBookings and challenges will appear here.")
        return
    }
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(notifications) { n ->
            val (color, emoji) = when(n.type) {
                "challenge" -> KC.SportAmber to "⚔️"; "score" -> KC.SportGreen to "🏆"
                "reschedule" -> KC.Cyan to "🔄"; "booking" -> KC.Violet to "📅"
                else -> KC.DarkSubText to "🔔"
            }
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (n.isRead) MaterialTheme.colorScheme.surfaceVariant else color.copy(0.08f),
                border = if (!n.isRead) androidx.compose.foundation.BorderStroke(1.dp, color.copy(0.25f)) else null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Text(emoji, fontSize = 20.sp)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(n.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(n.body, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (!n.isRead) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(color).align(Alignment.CenterVertically))
                    }
                }
            }
        }
    }
}

@Composable
fun AdminTab(allSlots: List<Slot>, teams: List<Team>, viewModel: MainViewModel, onGrantAdmin: () -> Unit) {
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Surface(shape = RoundedCornerShape(16.dp), color = KC.SportAmber.copy(0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, KC.SportAmber.copy(0.3f)),
                modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("👑", fontSize = 24.sp)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Admin Panel", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = KC.SportAmber)
                        Text("Manage all bookings, post scores, grant access.",
                            fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        item {
            Button(onClick = onGrantAdmin, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KC.Violet)) {
                Icon(Icons.Default.AdminPanelSettings, null, modifier = Modifier.size(18.dp), tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Grant Admin Access", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        item { SectionLabel("ALL ACTIVE BOOKINGS (${allSlots.size})") }

        if (allSlots.isEmpty()) {
            item { Text("No active bookings.", fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else {
            items(allSlots) { slot ->
                Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(sportEmoji(slot.sport), fontSize = 20.sp)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(slot.bookedByTeam, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("${slot.sport} · ${slot.time} · ${slot.date}", fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (slot.opponentTeam.isNotBlank())
                                Text("vs ${slot.opponentTeam}", fontSize = 11.sp, color = KC.SportGreen,
                                    fontWeight = FontWeight.SemiBold)
                        }
                        IconButton(onClick = { viewModel.cancelSlot(slot) }, modifier = Modifier.size(30.dp)) {
                            Icon(Icons.Default.Delete, null, tint = KC.SportRed, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        item { SectionLabel("REGISTERED TEAMS (${teams.size})") }
        items(teams) { t ->
            Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(36.dp).clip(CircleShape)
                        .background(KC.Violet.copy(0.15f)), contentAlignment = Alignment.Center) {
                        Text(t.name.take(2).uppercase(), fontWeight = FontWeight.Black,
                            color = KC.Violet, fontSize = 12.sp)
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(t.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("${t.sport} · ${t.village}", fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (t.email.isNotBlank()) Text(t.email, fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (t.role == "admin")
                        Surface(shape = RoundedCornerShape(4.dp), color = KC.SportAmber.copy(0.2f)) {
                            Text(" 👑 ", fontSize = 10.sp, color = KC.SportAmber,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                        }
                }
            }
        }
    }
}

// Extension for grant admin from UI
fun com.kreeda.ankana.repository.LocalRepository.grantAdmin(email: String, viewModel: MainViewModel) {
    viewModel.publicScope.launch {
        runCatching { teamDao.grantAdmin(email) }
    }
}
