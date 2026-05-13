@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
package com.kreeda.ankana.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreeda.ankana.ai.LocalAIAction
import com.kreeda.ankana.ui.theme.KC
import com.kreeda.ankana.viewmodel.ChatMsg
import com.kreeda.ankana.viewmodel.MainViewModel

@Composable
fun AIAssistantScreen(viewModel: MainViewModel) {
    val messages by viewModel.aiMessages.collectAsState()
    val aiLoading by viewModel.aiLoading.collectAsState()
    val pendingAction by viewModel.pendingAiAction.collectAsState()
    val keyboard = LocalSoftwareKeyboardController.current
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    val quickPrompts = listOf(
        "What slots are free today?",
        "Book 4 PM for Volleyball",
        "Show open challenges",
        "Who's playing now?",
        "Cancel my booking",
        "Reschedule to 6 PM"
    )

    Column(Modifier.fillMaxSize()) {
        // Header
        Box(
            Modifier.fillMaxWidth()
                .background(Brush.verticalGradient(listOf(KC.Violet.copy(0.15f), Color.Transparent)))
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Bot avatar
                Box(
                    Modifier.size(46.dp).clip(RoundedCornerShape(14.dp))
                        .background(Brush.linearGradient(KC.GradPrimary)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, null,
                        tint = Color.White, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("KreedaBot", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(7.dp).clip(CircleShape).background(KC.SportGreen))
                        Spacer(Modifier.width(5.dp))
                        Text("Online · No API key needed", fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                if (messages.isNotEmpty()) {
                    IconButton(onClick = { viewModel.clearAiHistory() }) {
                        Icon(Icons.Default.DeleteSweep, "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.2f))

        // Pending action banner
        pendingAction?.let { action ->
            Surface(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                shape = RoundedCornerShape(16.dp),
                color = KC.SportGreen.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, KC.SportGreen.copy(0.4f))
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bolt, null, tint = KC.SportGreen, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("KreedaBot suggests:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = KC.SportGreen)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(aiActionLabel(action), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { viewModel.executeAiAction() },
                            colors = ButtonDefaults.buttonColors(containerColor = KC.SportGreen),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                        ) { Text("Confirm ✓", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                        OutlinedButton(
                            onClick = { viewModel.dismissAiAction() },
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) { Text("Dismiss", fontSize = 13.sp) }
                    }
                }
            }
        }

        // Chat messages or empty state
        if (messages.isEmpty()) {
            Column(
                Modifier.weight(1f).padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier.size(90.dp).clip(CircleShape)
                        .background(Brush.radialGradient(listOf(KC.Violet.copy(0.3f), KC.DarkCard))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, null,
                        tint = KC.Violet, modifier = Modifier.size(40.dp))
                }
                Spacer(Modifier.height(20.dp))
                Text("Hey, I'm KreedaBot!", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                Spacer(Modifier.height(6.dp))
                Text("I know your bookings, free slots, challenges — and I can act on them. No API key needed!",
                    textAlign = TextAlign.Center, fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(24.dp))
                Text("Try one of these:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(10.dp))
                quickPrompts.chunked(2).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { prompt ->
                            Surface(
                                onClick = {
                                    viewModel.askAI(prompt)
                                    keyboard?.hide()
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, KC.Violet.copy(0.3f))
                            ) {
                                Text(prompt, modifier = Modifier.padding(10.dp),
                                    fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages) { msg -> BotChatBubble(msg) }
                if (aiLoading) {
                    item { BotTyping() }
                }
            }
        }

        // Input bar
        Surface(
            shadowElevation = 12.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = input, onValueChange = { input = it },
                    placeholder = { Text("Ask me anything…", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        if (input.isNotBlank() && !aiLoading) {
                            viewModel.askAI(input.trim()); input = ""; keyboard?.hide()
                        }
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = KC.Violet,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.5f)
                    )
                )
                Spacer(Modifier.width(10.dp))
                Box(
                    Modifier.size(48.dp).clip(CircleShape)
                        .background(if (input.isNotBlank() && !aiLoading)
                            Brush.linearGradient(KC.GradPrimary) else Brush.linearGradient(
                                listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surfaceVariant))),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (input.isNotBlank() && !aiLoading) {
                                viewModel.askAI(input.trim()); input = ""; keyboard?.hide()
                            }
                        },
                        enabled = input.isNotBlank() && !aiLoading
                    ) {
                        Icon(Icons.Default.Send, "Send",
                            tint = if (input.isNotBlank() && !aiLoading) Color.White
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.3f),
                            modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BotChatBubble(msg: ChatMsg) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!msg.isUser) {
            Box(
                Modifier.size(30.dp).clip(CircleShape)
                    .background(Brush.linearGradient(KC.GradPrimary)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
            Spacer(Modifier.width(8.dp))
        }
        Surface(
            shape = RoundedCornerShape(
                topStart = if (msg.isUser) 18.dp else 4.dp,
                topEnd = if (msg.isUser) 4.dp else 18.dp,
                bottomStart = 18.dp, bottomEnd = 18.dp
            ),
            color = if (msg.isUser) KC.Violet else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                msg.text, modifier = Modifier.padding(12.dp, 10.dp),
                fontSize = 14.sp,
                color = if (msg.isUser) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
        if (msg.isUser) {
            Spacer(Modifier.width(8.dp))
            Box(Modifier.size(30.dp).clip(CircleShape).background(KC.Violet),
                contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun BotTyping() {
    Row {
        Box(Modifier.size(30.dp).clip(CircleShape).background(Brush.linearGradient(KC.GradPrimary)),
            contentAlignment = Alignment.Center) {
            Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(14.dp))
        }
        Spacer(Modifier.width(8.dp))
        Surface(shape = RoundedCornerShape(4.dp, 18.dp, 18.dp, 18.dp),
            color = MaterialTheme.colorScheme.surfaceVariant) {
            Row(Modifier.padding(14.dp, 12.dp), horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically) {
                repeat(3) { i ->
                    val inf = rememberInfiniteTransition("t$i")
                    val scale by inf.animateFloat(0.5f, 1f,
                        infiniteRepeatable(tween(500, easing = EaseInOut), RepeatMode.Reverse,
                            initialStartOffset = StartOffset(i * 150)), "s")
                    Box(Modifier.size(7.dp * scale).clip(CircleShape)
                        .background(KC.Violet.copy(alpha = scale)))
                }
            }
        }
    }
}

fun aiActionLabel(action: LocalAIAction) = when (action) {
    is LocalAIAction.Book -> "📅 Book ${action.sport} at ${action.time} on ${action.date}"
    is LocalAIAction.Cancel -> "❌ Cancel your booking"
    is LocalAIAction.Reschedule -> "🔄 Reschedule to ${action.newTime} on ${action.newDate}"
    is LocalAIAction.AcceptChallenge -> "⚔️ Accept the challenge"
}
