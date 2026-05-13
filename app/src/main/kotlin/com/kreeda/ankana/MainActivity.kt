package com.kreeda.ankana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kreeda.ankana.auth.FirebaseAuthManager
import com.kreeda.ankana.db.AppDatabase
import com.kreeda.ankana.repository.LocalRepository
import com.kreeda.ankana.ui.screens.*
import com.kreeda.ankana.ui.theme.KreedaTheme
import com.kreeda.ankana.ui.theme.ThemeState
import com.kreeda.ankana.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "kreeda-v6")
            .fallbackToDestructiveMigration().build()
    }
    private val authManager by lazy { FirebaseAuthManager() }
    private val repository by lazy {
        LocalRepository(db.teamDao(), db.slotDao(), db.challengeDao(), db.matchScoreDao(), db.notificationDao())
    }
    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel(repository, authManager) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KreedaTheme {
                AppRoot(viewModel)
            }
        }
    }
}

sealed class KScreen(val route: String, val label: String, val icon: ImageVector, val iconFilled: ImageVector) {
    object Home      : KScreen("home",    "Home",    Icons.Default.Home,         Icons.Default.Home)
    object Booking   : KScreen("book",    "Book",    Icons.Default.CalendarMonth, Icons.Default.CalendarMonth)
    object Challenge : KScreen("chal",    "Battles", Icons.Default.FlashOn,      Icons.Default.FlashOn)
    object Scores    : KScreen("scores",  "Scores",  Icons.Default.Leaderboard,  Icons.Default.Leaderboard)
    object Bot       : KScreen("ai",      "Bot",     Icons.Default.AutoAwesome,  Icons.Default.AutoAwesome)
    object Profile   : KScreen("profile", "Me",      Icons.Default.AccountCircle, Icons.Default.AccountCircle)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppRoot(viewModel: MainViewModel) {
    val firebaseUser by viewModel.firebaseUser.collectAsState()
    val userTeam by viewModel.userTeam.collectAsState()
    val snackMsg by viewModel.snackMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val unreadCount by viewModel.unreadCount.collectAsState()
    var currentScreen by remember { mutableStateOf<KScreen>(KScreen.Home) }

    LaunchedEffect(snackMsg) {
        snackMsg?.let { scope.launch { snackbarHostState.showSnackbar(it) }; viewModel.clearSnack() }
    }

    // Auth / Registration gates
    AnimatedContent(
        targetState = when {
            firebaseUser == null -> "auth"
            userTeam == null     -> "register"
            else                 -> "main"
        },
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "root_anim"
    ) { state ->
        when (state) {
            "auth"     -> AuthScreen(viewModel)
            "register" -> RegisterTeamScreen(viewModel)
            else -> {
                val items = listOf(KScreen.Home, KScreen.Booking, KScreen.Challenge, KScreen.Scores, KScreen.Bot, KScreen.Profile)
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 0.dp
                        ) {
                            items.forEach { screen ->
                                val selected = currentScreen == screen
                                NavigationBarItem(
                                    icon = {
                                        BadgedBox(badge = {
                                            if (screen == KScreen.Profile && unreadCount > 0)
                                                Badge { Text(if (unreadCount > 9) "9+" else "$unreadCount", fontSize = 9.sp) }
                                        }) {
                                            Icon(screen.icon, screen.label,
                                                modifier = Modifier.size(if (selected) 26.dp else 22.dp))
                                        }
                                    },
                                    label = { Text(screen.label, fontSize = 10.sp) },
                                    selected = selected,
                                    onClick = { currentScreen = screen },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                ) { pad ->
                    Box(Modifier.padding(pad).fillMaxSize()) {
                        when (currentScreen) {
                            KScreen.Home      -> HomeScreen(viewModel)
                            KScreen.Booking   -> BookingScreen(viewModel)
                            KScreen.Challenge -> ChallengeScreen(viewModel)
                            KScreen.Scores    -> ScoreboardScreen(viewModel)
                            KScreen.Bot       -> AIAssistantScreen(viewModel)
                            KScreen.Profile   -> ProfileScreen(viewModel)
                        }
                        // Theme toggle FAB (top-right via overlay)
                        Row(
                            Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 16.dp)
                        ) {}
                    }
                }
            }
        }
    }
}
