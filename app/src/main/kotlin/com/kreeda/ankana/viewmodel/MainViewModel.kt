package com.kreeda.ankana.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreeda.ankana.ai.AIContext
import com.kreeda.ankana.ai.LocalAI
import com.kreeda.ankana.ai.LocalAIAction
import com.kreeda.ankana.auth.ADMIN_EMAIL
import com.kreeda.ankana.auth.FirebaseAuthManager
import com.kreeda.ankana.model.*
import com.kreeda.ankana.repository.LocalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ChatMsg(val text: String, val isUser: Boolean)

class MainViewModel(
    val repository: LocalRepository,
    val authManager: FirebaseAuthManager
) : ViewModel() {

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayDate: String = sdf.format(Date())
    val next14Days: List<String> = (0..13).map { offset ->
        sdf.format(Calendar.getInstance().also { it.add(Calendar.DATE, offset) }.time)
    }

    // ── Auth state ────────────────────────────────────────────────────────────
    private val _firebaseUser = MutableStateFlow(authManager.currentUser)
    val firebaseUser = _firebaseUser.asStateFlow()

    private val _userTeam = MutableStateFlow<Team?>(null)
    val userTeam = _userTeam.asStateFlow()

    val isAdmin: Boolean
        get() = authManager.isAdmin || _userTeam.value?.role == "admin"

    // ── UI data ───────────────────────────────────────────────────────────────
    private val _selectedDate = MutableStateFlow(todayDate)
    val selectedDate = _selectedDate.asStateFlow()

    private val _slots = MutableStateFlow<List<Slot>>(emptyList())
    val slots = _slots.asStateFlow()

    private val _allActiveSlots = MutableStateFlow<List<Slot>>(emptyList())
    val allActiveSlots = _allActiveSlots.asStateFlow()

    private val _rankings = MutableStateFlow<List<Team>>(emptyList())
    val rankings = _rankings.asStateFlow()

    private val _challenges = MutableStateFlow<List<Challenge>>(emptyList())
    val challenges = _challenges.asStateFlow()

    private val _scores = MutableStateFlow<List<MatchScore>>(emptyList())
    val scores = _scores.asStateFlow()

    private val _mySlots = MutableStateFlow<List<Slot>>(emptyList())
    val mySlots = _mySlots.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount = _unreadCount.asStateFlow()

    private val _authLoading = MutableStateFlow(false)
    val authLoading = _authLoading.asStateFlow()

    private val _snackMessage = MutableStateFlow<String?>(null)
    val snackMessage = _snackMessage.asStateFlow()

    // ── AI ────────────────────────────────────────────────────────────────────
    private val _aiMessages = MutableStateFlow<List<ChatMsg>>(emptyList())
    val aiMessages = _aiMessages.asStateFlow()

    private val _aiLoading = MutableStateFlow(false)
    val aiLoading = _aiLoading.asStateFlow()

    private val _pendingAiAction = MutableStateFlow<LocalAIAction?>(null)
    val pendingAiAction = _pendingAiAction.asStateFlow()

    // ── Scope exposed for UI extensions ───────────────────────────────────────
    val publicScope get() = viewModelScope

    init {
        // Watch Firebase auth changes — flatMapLatest cancels the previous user's team stream
        viewModelScope.launch {
            authManager.authStateFlow
                .flatMapLatest { fbUser ->
                    _firebaseUser.value = fbUser
                    if (fbUser != null) {
                        repository.getUserTeam(fbUser.uid).map { team ->
                            team?.let {
                                if (fbUser.email?.lowercase() == ADMIN_EMAIL && it.role != "admin")
                                    it.copy(role = "admin") else it
                            }
                        }
                    } else {
                        _mySlots.value = emptyList()
                        _notifications.value = emptyList()
                        flowOf(null)
                    }
                }
                .collect { team -> _userTeam.value = team }
        }
        viewModelScope.launch { repository.getTeamsRanked().collect { _rankings.value = it } }
        viewModelScope.launch { repository.getPendingChallenges().collect { _challenges.value = it } }
        viewModelScope.launch { repository.getAllScores().collect { _scores.value = it } }
        viewModelScope.launch { repository.getAllActiveSlots().collect { _allActiveSlots.value = it } }
        fetchSlotsForDate(todayDate)
    }

    private fun watchMyData(uid: String) {
        viewModelScope.launch { repository.getUserSlots(uid).collect { _mySlots.value = it } }
        viewModelScope.launch { repository.getNotifications(uid).collect { _notifications.value = it } }
        viewModelScope.launch { repository.getUnreadCount(uid).collect { _unreadCount.value = it } }
    }

    // ── Auth ──────────────────────────────────────────────────────────────────
    fun signUp(email: String, password: String, onSuccess: () -> Unit = {}) {
        _authLoading.value = true
        viewModelScope.launch {
            authManager.signUp(email, password)
                .onSuccess { user -> _firebaseUser.value = user; watchMyData(user.uid); onSuccess() }
                .onFailure { snack("Sign up failed: ${it.message}") }
            _authLoading.value = false
        }
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit = {}) {
        _authLoading.value = true
        viewModelScope.launch {
            authManager.signIn(email, password)
                .onSuccess { user -> _firebaseUser.value = user; watchMyData(user.uid); onSuccess() }
                .onFailure { snack("Sign in failed: ${it.message}") }
            _authLoading.value = false
        }
    }

    fun signInWithGoogle(idToken: String) {
        _authLoading.value = true
        viewModelScope.launch {
            authManager.signInWithGoogle(idToken)
                .onSuccess { user -> watchMyData(user.uid); snack("Signed in with Google! 🎉") }
                .onFailure { snack("Google sign-in failed: ${it.message}") }
            _authLoading.value = false
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            authManager.sendPasswordReset(email)
                .onSuccess { snack("Reset email sent! Check your inbox.") }
                .onFailure { snack("Failed: ${it.message}") }
        }
    }

    fun signOut() {
        authManager.signOut()
        _firebaseUser.value = null
        _userTeam.value = null
        _mySlots.value = emptyList()
        _notifications.value = emptyList()
        _unreadCount.value = 0
    }

    // ── Team ──────────────────────────────────────────────────────────────────
    fun registerTeam(name: String, sport: String, motto: String,
                     village: String, phone: String, playerCount: Int) {
        val fbUser = _firebaseUser.value ?: run { snack("Sign in first!"); return }
        viewModelScope.launch {
            val isAdminEmail = fbUser.email?.lowercase() == ADMIN_EMAIL
            val team = Team(
                name = name, sport = sport,
                motto = motto.ifBlank { "Valor and Victory." },
                uid = fbUser.uid, firebaseUid = fbUser.uid,
                email = fbUser.email ?: "",
                village = village, phone = phone,
                playerCount = playerCount,
                role = if (isAdminEmail) "admin" else "user"
            )
            repository.registerTeam(team).onSuccess {
                _userTeam.value = team
                watchMyData(team.uid)
                snack("Team '${team.name}' created! ${if (isAdminEmail) "Admin access granted 👑" else "Let's play 🎉"}")
            }.onFailure { snack("Error: ${it.message}") }
        }
    }

    // ── Slots ─────────────────────────────────────────────────────────────────
    fun selectDate(date: String) {
        _selectedDate.value = date
        fetchSlotsForDate(date)
    }

    fun fetchSlotsForDate(date: String) {
        viewModelScope.launch {
            repository.getSlots(date).collect { _slots.value = it }
        }
    }

    fun bookSlot(time: String, sport: String, date: String) {
        val team = _userTeam.value ?: run { snack("Register your team first!"); return }
        viewModelScope.launch {
            if (!isAdmin && !repository.canBookOnDate(date, team.uid)) {
                snack("You already have a booking on $date. One slot per day."); return@launch
            }
            val existing = _slots.value.find { it.time == time && it.date == date }
            if (existing != null) { snack("${time} on $date is already booked!"); return@launch }
            repository.bookSlot(
                Slot(
                    time = time, date = date, status = "Booked",
                    bookedByTeam = team.name, bookedByUid = team.uid,
                    bookedByEmail = team.email, sport = sport
                )
            ).onSuccess {
                snack("Slot booked: $time on $date ✅")
                fetchSlotsForDate(date)
                addNotification(team.uid, "Booking Confirmed 📅",
                    "${sport} at $time on $date is locked in.", "booking")
            }.onFailure { snack("Error: ${it.message}") }
        }
    }

    fun cancelSlot(slot: Slot) {
        val team = _userTeam.value ?: return
        if (slot.bookedByUid != team.uid && !isAdmin) {
            snack("You can only cancel your own bookings."); return
        }
        viewModelScope.launch {
            repository.cancelSlot(slot).onSuccess {
                snack("Booking cancelled.")
                fetchSlotsForDate(slot.date)
            }.onFailure { snack("Error: ${it.message}") }
        }
    }

    fun rescheduleSlot(slot: Slot, newDate: String, newTime: String) {
        val team = _userTeam.value ?: return
        if (slot.bookedByUid != team.uid && !isAdmin) {
            snack("You can only reschedule your own bookings."); return
        }
        viewModelScope.launch {
            if (!isAdmin && !repository.canBookOnDate(newDate, team.uid)) {
                snack("You already have a booking on $newDate."); return@launch
            }
            repository.cancelSlot(slot)
            repository.bookSlot(
                slot.copy(id = UUID.randomUUID().toString(),
                    date = newDate, time = newTime, rescheduleRequestedTo = "", rescheduleStatus = "")
            ).onSuccess {
                snack("Rescheduled to $newTime on $newDate ✅")
                fetchSlotsForDate(newDate)
                addNotification(team.uid, "Slot Rescheduled 🔄",
                    "${slot.sport} moved to $newTime on $newDate.", "reschedule")
            }
        }
    }

    // ── Challenges ────────────────────────────────────────────────────────────
    /** Post an OPEN challenge from your own slot — any team can accept */
    fun postOpenChallenge(slot: Slot, message: String) {
        val team = _userTeam.value ?: run { snack("Register first!"); return }
        if (slot.bookedByUid != team.uid) {
            snack("You can only post challenges for your own slots!"); return
        }
        viewModelScope.launch {
            repository.postChallenge(
                Challenge(fromTeam = team.name, fromTeamUid = team.uid,
                    toSlotId = slot.id, toTeam = "OPEN",
                    sport = slot.sport, time = slot.time, date = slot.date, message = message)
            ).onSuccess { snack("Challenge posted! 🔥 Others can now accept.") }
                .onFailure { snack("Error: ${it.message}") }
        }
    }

    /** Accept a challenge — enforces the "different team" rule */
    fun acceptChallenge(challenge: Challenge) {
        val team = _userTeam.value ?: run { snack("Register first!"); return }
        if (challenge.fromTeamUid == team.uid) {
            snack("You can't accept your own challenge!"); return
        }
        viewModelScope.launch {
            repository.updateChallenge(challenge.copy(status = "accepted"))
            repository.getSlotById(challenge.toSlotId)?.let { slot ->
                repository.updateSlot(slot.copy(opponentTeam = team.name, status = "Playing"))
            }
            fetchSlotsForDate(_selectedDate.value)
            addNotification(challenge.fromTeamUid, "Challenge Accepted! 🏆",
                "${team.name} accepted your ${challenge.sport} challenge on ${challenge.date}.", "challenge")
            snack("Challenge accepted! Game on 🏆")
        }
    }

    fun declineChallenge(challenge: Challenge) {
        val team = _userTeam.value ?: return
        if (challenge.fromTeamUid == team.uid) { snack("Can't decline your own challenge!"); return }
        viewModelScope.launch {
            repository.updateChallenge(challenge.copy(status = "declined"))
            snack("Challenge declined.")
        }
    }

    // ── Scores (Admin only) ───────────────────────────────────────────────────
    fun postScore(slot: Slot, score1: Int, score2: Int, notes: String) {
        if (!isAdmin) { snack("Admin only."); return }
        viewModelScope.launch {
            val winner = when {
                score1 > score2 -> slot.bookedByTeam
                score2 > score1 -> slot.opponentTeam
                else -> "Draw"
            }
            repository.postScore(
                MatchScore(slotId = slot.id, date = slot.date,
                    team1 = slot.bookedByTeam, team2 = slot.opponentTeam,
                    sport = slot.sport, score1 = score1, score2 = score2,
                    winner = winner, notes = notes)
            )
            repository.updateSlot(slot.copy(status = "Completed"))
            // Update win/loss records
            listOf(slot.bookedByTeam to slot.bookedByUid, slot.opponentTeam to "").forEach { (teamName, _) ->
                _rankings.value.find { it.name == teamName }?.let { t ->
                    repository.updateTeam(t.copy(
                        wins = if (winner == t.name) t.wins + 1 else t.wins,
                        losses = if (winner != t.name && winner != "Draw") t.losses + 1 else t.losses,
                        matchesPlayed = t.matchesPlayed + 1
                    ))
                }
            }
            snack("Score posted! Winner: $winner 🎉")
        }
    }

    // ── Notifications ─────────────────────────────────────────────────────────
    private fun addNotification(toUid: String, title: String, body: String, type: String) {
        viewModelScope.launch {
            repository.addNotification(AppNotification(toUid = toUid, title = title, body = body, type = type))
        }
    }

    fun markAllNotificationsRead() {
        val uid = _userTeam.value?.uid ?: return
        viewModelScope.launch { repository.markAllRead(uid) }
    }

    // ── AI (local, no API key) ────────────────────────────────────────────────
    fun askAI(query: String) {
        if (query.isBlank()) return
        _aiMessages.value = _aiMessages.value + ChatMsg(query, true)
        _aiLoading.value = true
        _pendingAiAction.value = null
        viewModelScope.launch {
            delay(350)
            val ctx = AIContext(
                today = todayDate, selectedDate = _selectedDate.value,
                team = _userTeam.value, slots = _slots.value,
                mySlots = _mySlots.value, allActive = _allActiveSlots.value,
                challenges = _challenges.value
            )
            val result = LocalAI.respond(query, ctx)
            _aiMessages.value = _aiMessages.value + ChatMsg(result.message, false)
            _pendingAiAction.value = result.action
            _aiLoading.value = false
        }
    }

    fun executeAiAction() {
        when (val action = _pendingAiAction.value) {
            is LocalAIAction.Book -> bookSlot(action.time, action.sport, action.date)
            is LocalAIAction.Cancel -> {
                _mySlots.value.find { it.id == action.slotId }?.let { cancelSlot(it) }
                    ?: snack("Slot not found.")
            }
            is LocalAIAction.Reschedule -> {
                _mySlots.value.find { it.id == action.slotId }?.let {
                    rescheduleSlot(it, action.newDate, action.newTime)
                } ?: snack("Slot not found.")
            }
            is LocalAIAction.AcceptChallenge -> {
                _challenges.value.find { it.id == action.challengeId }?.let { acceptChallenge(it) }
                    ?: snack("Challenge not found.")
            }
            null -> {}
        }
        _pendingAiAction.value = null
    }

    fun dismissAiAction() { _pendingAiAction.value = null }
    fun clearAiHistory() { _aiMessages.value = emptyList() }
    fun clearSnack() { _snackMessage.value = null }
    private fun snack(msg: String) { _snackMessage.value = msg }
}
