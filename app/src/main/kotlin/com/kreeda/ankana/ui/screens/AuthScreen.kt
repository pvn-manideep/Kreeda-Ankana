@file:OptIn(ExperimentalMaterial3Api::class)
package com.kreeda.ankana.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreeda.ankana.ui.theme.KC
import com.kreeda.ankana.ui.theme.ThemeState
import com.kreeda.ankana.viewmodel.MainViewModel

@Composable
fun AuthScreen(viewModel: MainViewModel) {
    var showEmailForm by remember { mutableStateOf(false) }
    var isSignIn by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var showPw by remember { mutableStateOf(false) }
    var showReset by remember { mutableStateOf(false) }
    val authLoading by viewModel.authLoading.collectAsState()

    Box(Modifier.fillMaxSize()) {
        // Background
        Box(Modifier.fillMaxSize().background(
            Brush.radialGradient(listOf(KC.Violet.copy(0.4f), KC.DarkBg), radius = 1200f)))
        // Blurred orbs
        Box(Modifier.offset((-60).dp, 80.dp).size(280.dp).blur(90.dp)
            .background(KC.Magenta.copy(0.25f), CircleShape))
        Box(Modifier.offset(160.dp, 500.dp).size(220.dp).blur(80.dp)
            .background(KC.Cyan.copy(0.2f), CircleShape))

        // Theme toggle
        IconButton(onClick = { ThemeState.isDark = !ThemeState.isDark },
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
            Icon(if (ThemeState.isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                "Toggle theme", tint = Color.White.copy(0.7f))
        }

        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Brand mark
            Box(Modifier.size(80.dp).clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(KC.GradPrimary)),
                contentAlignment = Alignment.Center) {
                Text("K", fontSize = 40.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
            Spacer(Modifier.height(20.dp))
            Text("Kreeda-Ankana", fontSize = 30.sp, fontWeight = FontWeight.Black,
                color = Color.White, letterSpacing = (-0.5).sp)
            Text("Your village sports hub", fontSize = 14.sp, color = Color.White.copy(0.55f))
            Spacer(Modifier.height(48.dp))

            // ── Email toggle ──────────────────────────────────────────────
            AnimatedVisibility(!showEmailForm) {
                OutlinedButton(
                    onClick = { showEmailForm = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.White.copy(0.25f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Icon(Icons.Default.Email, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("Continue with Email", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
            }

            // ── Email form (expands on tap) ───────────────────────────────
            AnimatedVisibility(showEmailForm) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Tab
                    Surface(shape = RoundedCornerShape(12.dp), color = Color.White.copy(0.08f)) {
                        Row(Modifier.padding(4.dp)) {
                            listOf(true to "Sign In", false to "Sign Up").forEach { (it, label) ->
                                Button(
                                    onClick = { isSignIn = it },
                                    modifier = Modifier.weight(1f).height(38.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSignIn == it) KC.Violet else Color.Transparent,
                                        contentColor = Color.White
                                    ), elevation = ButtonDefaults.buttonElevation(0.dp)
                                ) { Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                            }
                        }
                    }

                    val fieldColors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = KC.Violet, focusedLabelColor = KC.Violet,
                        unfocusedBorderColor = Color.White.copy(0.2f),
                        unfocusedLabelColor = Color.White.copy(0.4f),
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        cursorColor = KC.Violet
                    )

                    OutlinedTextField(value = email, onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = KC.Violet, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.fillMaxWidth(), singleLine = true,
                        shape = RoundedCornerShape(14.dp), colors = fieldColors,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))

                    OutlinedTextField(value = password, onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = KC.Violet, modifier = Modifier.size(18.dp)) },
                        trailingIcon = {
                            IconButton(onClick = { showPw = !showPw }) {
                                Icon(if (showPw) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    null, tint = Color.White.copy(0.4f), modifier = Modifier.size(18.dp))
                            }
                        },
                        visualTransformation = if (showPw) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(), singleLine = true,
                        shape = RoundedCornerShape(14.dp), colors = fieldColors)

                    AnimatedVisibility(!isSignIn) {
                        OutlinedTextField(value = confirm, onValueChange = { confirm = it },
                            label = { Text("Confirm Password") },
                            leadingIcon = { Icon(Icons.Default.LockOpen, null, tint = KC.Violet, modifier = Modifier.size(18.dp)) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(), singleLine = true,
                            shape = RoundedCornerShape(14.dp), colors = fieldColors)
                    }

                    if (isSignIn) {
                        TextButton(onClick = { showReset = true },
                            modifier = Modifier.align(Alignment.End)) {
                            Text("Forgot password?", fontSize = 12.sp, color = KC.VioletLight)
                        }
                    }

                    Button(
                        onClick = {
                            if (isSignIn) viewModel.signIn(email.trim(), password)
                            else if (password.length >= 6 && password == confirm)
                                viewModel.signUp(email.trim(), password)
                        },
                        enabled = !authLoading && email.isNotBlank() && password.length >= 6
                                && (isSignIn || password == confirm),
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = KC.Violet,
                            disabledContainerColor = KC.Violet.copy(0.4f))
                    ) {
                        if (authLoading)
                            CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                        else {
                            Icon(if (isSignIn) Icons.Default.Login else Icons.Default.PersonAdd,
                                null, modifier = Modifier.size(18.dp), tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text(if (isSignIn) "Sign In" else "Create Account",
                                fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Admin hint
            Surface(shape = RoundedCornerShape(12.dp), color = KC.Violet.copy(0.12f),
                border = BorderStroke(1.dp, KC.Violet.copy(0.25f)),
                modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("👑", fontSize = 16.sp)
                    Spacer(Modifier.width(8.dp))
                    Text("Sign in From Admin mail for admin access",
                        fontSize = 11.sp, color = Color.White.copy(0.55f))
                }
            }

            Spacer(Modifier.height(24.dp))

            // Feature strip
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("📅" to "Book", "⚔️" to "Battle", "🤖" to "Bot", "🏆" to "Scores")
                    .forEach { (emoji, label) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(emoji, fontSize = 20.sp)
                            Text(label, fontSize = 10.sp, color = Color.White.copy(0.4f),
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
            }
        }
    }

    if (showReset) {
        var resetEmail by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showReset = false },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            title = { Text("Reset Password", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(value = resetEmail, onValueChange = { resetEmail = it },
                    label = { Text("Your email") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, shape = RoundedCornerShape(12.dp))
            },
            confirmButton = {
                Button(onClick = { viewModel.sendPasswordReset(resetEmail.trim()); showReset = false },
                    colors = ButtonDefaults.buttonColors(containerColor = KC.Violet)) {
                    Text("Send Reset Link")
                }
            },
            dismissButton = { TextButton(onClick = { showReset = false }) { Text("Cancel") } }
        )
    }
}
