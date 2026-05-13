@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
package com.kreeda.ankana.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreeda.ankana.ui.theme.KC
import com.kreeda.ankana.viewmodel.MainViewModel

@Composable
fun RegisterTeamScreen(viewModel: MainViewModel) {
    var teamName by remember { mutableStateOf("") }
    var sport by remember { mutableStateOf("Volleyball") }
    var motto by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var playerCount by remember { mutableStateOf("") }
    val sports = listOf("Volleyball","Cricket","Football","Kabaddi","Badminton","Kho-Kho")

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .background(Brush.verticalGradient(listOf(KC.Violet.copy(0.1f), Color.Transparent)))
            .padding(horizontal = 24.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        Box(Modifier.size(64.dp).clip(CircleShape)
            .background(Brush.linearGradient(KC.GradPrimary)).align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Groups, null, tint = Color.White, modifier = Modifier.size(32.dp))
        }
        Text("Register Your Squad", fontWeight = FontWeight.ExtraBold, fontSize = 26.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally))
        Text("Tell us about your team to start booking, challenging, and climbing the leaderboard.",
            fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally))

        TextButton(
            onClick = { viewModel.signOut() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(Icons.Default.Logout, null, modifier = Modifier.size(15.dp), tint = KC.Violet)
            Spacer(Modifier.width(6.dp))
            Text("Not you? Sign out", fontSize = 13.sp, color = KC.Violet)
        }

        Spacer(Modifier.height(4.dp))

        val fieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = KC.Violet, focusedLabelColor = KC.Violet,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(0.5f), cursorColor = KC.Violet
        )

        OutlinedTextField(value = teamName, onValueChange = { teamName = it },
            label = { Text("Team Name *") },
            leadingIcon = { Icon(Icons.Default.Shield, null, tint = KC.Violet, modifier = Modifier.size(18.dp)) },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            shape = RoundedCornerShape(14.dp), colors = fieldColors)

        OutlinedTextField(value = village, onValueChange = { village = it },
            label = { Text("Village / Area *") },
            leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = KC.Violet, modifier = Modifier.size(18.dp)) },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            shape = RoundedCornerShape(14.dp), colors = fieldColors)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = phone, onValueChange = { phone = it },
                label = { Text("Phone") },
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = KC.Violet, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.weight(1f), singleLine = true,
                shape = RoundedCornerShape(14.dp), colors = fieldColors)
            OutlinedTextField(value = playerCount, onValueChange = { playerCount = it },
                label = { Text("Players") },
                leadingIcon = { Icon(Icons.Default.Group, null, tint = KC.Violet, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.weight(1f), singleLine = true,
                shape = RoundedCornerShape(14.dp), colors = fieldColors)
        }

        OutlinedTextField(value = motto, onValueChange = { motto = it },
            label = { Text("Team Motto (optional)") },
            leadingIcon = { Icon(Icons.Default.FormatQuote, null, tint = KC.Violet, modifier = Modifier.size(18.dp)) },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            shape = RoundedCornerShape(14.dp), colors = fieldColors)

        Text("Primary Sport *", style = MaterialTheme.typography.labelLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            sports.forEach { s ->
                FilterChip(
                    selected = sport == s, onClick = { sport = s },
                    label = { Text(s, fontSize = 13.sp) },
                    leadingIcon = { Text(sportEmoji(s), fontSize = 13.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = KC.Violet, selectedLabelColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                if (teamName.isNotBlank() && village.isNotBlank())
                    viewModel.registerTeam(teamName.trim(), sport,
                        motto.ifBlank { "Valor and Victory." },
                        village.trim(), phone.trim(), playerCount.toIntOrNull() ?: 0)
            },
            enabled = teamName.isNotBlank() && village.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = KC.Violet, disabledContainerColor = KC.Violet.copy(0.4f))
        ) {
            Icon(Icons.Default.EmojiEvents, null, modifier = Modifier.size(20.dp), tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Create Team & Enter Arena!", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
        }
    }
}
