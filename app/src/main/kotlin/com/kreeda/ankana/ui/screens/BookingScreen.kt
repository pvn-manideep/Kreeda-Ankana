@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
package com.kreeda.ankana.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreeda.ankana.model.Slot
import com.kreeda.ankana.ui.theme.KC
import com.kreeda.ankana.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BookingScreen(viewModel: MainViewModel) {
    val slots by viewModel.slots.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val dates = viewModel.next14Days
    val team by viewModel.userTeam.collectAsState()
    val mySlots by viewModel.mySlots.collectAsState()
    val isAdmin = team?.role == "admin"

    var showBookDialog by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("") }
    var showCancel by remember { mutableStateOf<Slot?>(null) }
    var showReschedule by remember { mutableStateOf<Slot?>(null) }
    var showOpenChallenge by remember { mutableStateOf<Slot?>(null) }

    val sdfParse = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val sdfDay = SimpleDateFormat("EEE", Locale.getDefault())
    val sdfDate = SimpleDateFormat("d", Locale.getDefault())
    val sdfMonth = SimpleDateFormat("MMM", Locale.getDefault())
    val times = listOf("6 AM","8 AM","10 AM","12 PM","2 PM","4 PM","6 PM","8 PM")
    val sports = listOf("Volleyball","Cricket","Football","Kabaddi","Badminton","Kho-Kho")

    Column(Modifier.fillMaxSize()) {
        // Header
        Box(
            Modifier.fillMaxWidth()
                .background(Brush.verticalGradient(listOf(KC.Violet.copy(0.15f), Color.Transparent)))
                .padding(start = 20.dp, top = 18.dp, end = 20.dp, bottom = 0.dp)
        ) {
            Column {
                Text("Ground Calendar", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                Text("14-day booking · 1 slot/day${if (isAdmin) " · Admin Mode" else ""}",
                    fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(16.dp))

                // Date strip
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(dates) { date ->
                        val cal = try { sdfParse.parse(date) } catch (e: Exception) { null }
                        val isSelected = date == selectedDate
                        val isToday = date == viewModel.todayDate
                        val hasMyBooking = mySlots.any { it.date == date && it.status != "Rescheduled" }

                        Box(
                            modifier = Modifier
                                .width(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    when {
                                        isSelected -> Brush.linearGradient(KC.GradPrimary)
                                        hasMyBooking -> Brush.linearGradient(listOf(KC.Violet.copy(0.15f), KC.Violet.copy(0.15f)))
                                        else -> Brush.linearGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surfaceVariant))
                                    }
                                )
                                .clickable { viewModel.selectDate(date) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    if (isToday) "TODAY" else cal?.let { sdfDay.format(it) } ?: "",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) Color.White.copy(0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    cal?.let { sdfDate.format(it) } ?: "",
                                    fontSize = 18.sp, fontWeight = FontWeight.Black,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    cal?.let { sdfMonth.format(it) } ?: "",
                                    fontSize = 9.sp,
                                    color = if (isSelected) Color.White.copy(0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (hasMyBooking && !isSelected) {
                                    Spacer(Modifier.height(3.dp))
                                    Box(Modifier.size(5.dp).clip(CircleShape).background(KC.Violet))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Slot count indicator
        val bookedCount = slots.size
        val totalCount = times.size
        LinearProgressIndicator(
            progress = { bookedCount.toFloat() / totalCount },
            modifier = Modifier.fillMaxWidth().height(3.dp),
            color = KC.Violet,
            trackColor = MaterialTheme.colorScheme.outline.copy(0.2f)
        )

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${totalCount - bookedCount} of $totalCount slots free",
                fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(selectedDate, fontSize = 12.sp, color = KC.Violet, fontWeight = FontWeight.SemiBold)
        }

        // Slot list
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(times) { time ->
                val booking = slots.find { it.time == time }
                val isMyBooking = booking?.bookedByUid == team?.uid
                SlotCard(
                    time = time,
                    booking = booking,
                    isMyBooking = isMyBooking,
                    isAdmin = isAdmin,
                    onBook = { selectedTime = time; showBookDialog = true },
                    onCancel = { showCancel = booking },
                    onReschedule = { showReschedule = booking },
                    onOpenChallenge = { showOpenChallenge = booking }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }

    // Dialogs
    if (showBookDialog) {
        BookDialog(
            time = selectedTime, date = selectedDate, sports = sports,
            onDismiss = { showBookDialog = false },
            onConfirm = { sport -> viewModel.bookSlot(selectedTime, sport, selectedDate); showBookDialog = false }
        )
    }
    showCancel?.let { slot ->
        ConfirmDialog(
            title = "Cancel Booking",
            message = "Cancel ${slot.bookedByTeam}'s slot at ${slot.time} on ${slot.date}?",
            confirmLabel = "Cancel Booking",
            confirmColor = KC.SportRed,
            onConfirm = { viewModel.cancelSlot(slot); showCancel = null },
            onDismiss = { showCancel = null }
        )
    }
    showReschedule?.let { slot ->
        RescheduleDialog(
            slot = slot, dates = dates, times = times,
            onDismiss = { showReschedule = null },
            onConfirm = { newDate, newTime ->
                viewModel.rescheduleSlot(slot, newDate, newTime); showReschedule = null
            }
        )
    }
    showOpenChallenge?.let { slot ->
        OpenChallengeDialog(
            slot = slot,
            onDismiss = { showOpenChallenge = null },
            onPost = { msg -> viewModel.postOpenChallenge(slot, msg); showOpenChallenge = null }
        )
    }
}

@Composable
fun SlotCard(
    time: String, booking: Slot?, isMyBooking: Boolean, isAdmin: Boolean,
    onBook: () -> Unit, onCancel: () -> Unit,
    onReschedule: () -> Unit, onOpenChallenge: () -> Unit
) {
    val isVacant = booking == null
    val isPlaying = booking?.status == "Playing"
    val accent = when {
        isPlaying -> KC.SportRed; isMyBooking -> KC.Violet; isVacant -> KC.SportGreen; else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = when {
            isPlaying -> KC.SportRed.copy(0.07f)
            isMyBooking -> KC.Violet.copy(0.07f)
            isVacant -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        border = BorderStroke(
            if (isMyBooking || isPlaying) 1.dp else 0.5.dp,
            if (isMyBooking || isPlaying) accent.copy(0.4f) else MaterialTheme.colorScheme.outline.copy(0.2f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            // Time badge
            Box(
                Modifier.width(58.dp).clip(RoundedCornerShape(10.dp))
                    .background(accent.copy(0.12f))
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                val parts = time.split(" ")
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(parts[0], fontWeight = FontWeight.Black, fontSize = 15.sp, color = accent)
                    Text(parts.getOrElse(1) { "" }, fontSize = 9.sp, color = accent.copy(0.8f),
                        fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.width(12.dp))

            // Status column
            Column(Modifier.weight(1f)) {
                when {
                    isVacant -> {
                        Text("Available", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = KC.SportGreen)
                        Text("Tap + to book this slot", fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    isPlaying -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(7.dp).clip(CircleShape).background(KC.SportRed))
                            Spacer(Modifier.width(5.dp))
                            Text("LIVE", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold,
                                color = KC.SportRed, letterSpacing = 1.sp)
                        }
                        Spacer(Modifier.height(2.dp))
                        Text("${booking!!.bookedByTeam} vs ${booking.opponentTeam}",
                            fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                        Text(booking.sport, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    else -> {
                        if (isMyBooking) Text("MY SLOT", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold,
                            color = KC.Violet, letterSpacing = 1.sp)
                        Text(booking!!.bookedByTeam, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(booking.sport, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (booking.opponentTeam.isNotBlank())
                            Text("vs ${booking.opponentTeam}", fontSize = 12.sp, color = KC.SportGreen,
                                fontWeight = FontWeight.SemiBold)
                        else if (isMyBooking)
                            Text("No opponent yet", fontSize = 11.sp, color = KC.SportAmber)
                    }
                }
            }

            // Action buttons
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (isVacant) {
                    FilledIconButton(
                        onClick = onBook, modifier = Modifier.size(38.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = KC.SportGreen)
                    ) { Icon(Icons.Default.Add, "Book", tint = Color.White, modifier = Modifier.size(20.dp)) }
                } else if (isMyBooking || isAdmin) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        // Reschedule
                        IconButton(onClick = onReschedule, modifier = Modifier.size(34.dp)) {
                            Icon(Icons.Default.SwapHoriz, "Reschedule",
                                tint = KC.SportAmber, modifier = Modifier.size(18.dp))
                        }
                        // Cancel
                        IconButton(onClick = onCancel, modifier = Modifier.size(34.dp)) {
                            Icon(Icons.Default.Delete, "Cancel",
                                tint = KC.SportRed, modifier = Modifier.size(18.dp))
                        }
                    }
                    // Open challenge button for my slots
                    if (isMyBooking && booking?.opponentTeam.isNullOrBlank()) {
                        TextButton(
                            onClick = onOpenChallenge,
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                            modifier = Modifier.height(26.dp)
                        ) {
                            Icon(Icons.Default.FlashOn, null, tint = KC.SportAmber,
                                modifier = Modifier.size(12.dp))
                            Text("Challenge", fontSize = 10.sp, color = KC.SportAmber,
                                fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookDialog(time: String, date: String, sports: List<String>,
               onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var sport by remember { mutableStateOf(sports[0]) }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        title = {
            Column {
                Text("Book Slot", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Text("$time · $date", fontSize = 13.sp, color = KC.Violet, fontWeight = FontWeight.SemiBold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Select Sport", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sports.forEach { s ->
                        FilterChip(
                            selected = sport == s, onClick = { sport = s },
                            label = { Text(s, fontSize = 13.sp) },
                            leadingIcon = { Text(sportEmoji(s), fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = KC.Violet,
                                selectedLabelColor = Color.White
                            ), shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
                Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surface) {
                    Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = KC.SportAmber, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("One booking per day. This locks the slot for you.",
                            fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(sport) }, shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KC.Violet)) {
                Text("Confirm Booking", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun ConfirmDialog(title: String, message: String, confirmLabel: String,
                  confirmColor: Color, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        title = { Text(title, fontWeight = FontWeight.ExtraBold) },
        text = { Text(message, fontSize = 14.sp) },
        confirmButton = {
            Button(onClick = onConfirm, shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = confirmColor)) {
                Text(confirmLabel, fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Keep") } }
    )
}

@Composable
fun RescheduleDialog(slot: Slot, dates: List<String>, times: List<String>,
                     onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val disp = SimpleDateFormat("MMM d", Locale.getDefault())
    var newDate by remember { mutableStateOf(dates.firstOrNull { it != slot.date } ?: dates.first()) }
    var newTime by remember { mutableStateOf(slot.time) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        title = {
            Column {
                Text("Reschedule Slot", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Text("From: ${slot.time} on ${slot.date}", fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("New Date", style = MaterialTheme.typography.labelLarge)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(dates.filter { it != slot.date }) { d ->
                        val label = try { disp.format(sdf.parse(d)!!) } catch (e: Exception) { d }
                        FilterChip(selected = newDate == d, onClick = { newDate = d },
                            label = { Text(label, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = KC.SportAmber,
                                selectedLabelColor = Color.White
                            ), shape = RoundedCornerShape(10.dp))
                    }
                }
                Text("New Time", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    times.forEach { t ->
                        FilterChip(selected = newTime == t, onClick = { newTime = t },
                            label = { Text(t, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = KC.SportAmber,
                                selectedLabelColor = Color.White
                            ), shape = RoundedCornerShape(10.dp))
                    }
                }
                Text("→ New slot: $newTime on $newDate",
                    fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = KC.SportAmber)
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(newDate, newTime) }, shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KC.SportAmber)) {
                Text("Reschedule", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun OpenChallengeDialog(slot: Slot, onDismiss: () -> Unit, onPost: (String) -> Unit) {
    var message by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        title = {
            Column {
                Text("Post Open Challenge", fontWeight = FontWeight.ExtraBold)
                Text("${slot.sport} · ${slot.time} · ${slot.date}",
                    fontSize = 12.sp, color = KC.SportAmber, fontWeight = FontWeight.SemiBold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Any team can accept this challenge from the Battles tab.",
                    fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = message, onValueChange = { message = it },
                    label = { Text("Battle cry (optional)") },
                    placeholder = { Text("Who dares challenge us? 😤") },
                    modifier = Modifier.fillMaxWidth(), maxLines = 2,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = { onPost(message.trim()) }, shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KC.SportAmber)) {
                Icon(Icons.Default.FlashOn, null, modifier = Modifier.size(16.dp), tint = Color.White)
                Spacer(Modifier.width(6.dp))
                Text("Post Challenge!", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
