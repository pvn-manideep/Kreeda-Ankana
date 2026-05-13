package com.kreeda.ankana.repository

import com.kreeda.ankana.db.*
import com.kreeda.ankana.model.*
import kotlinx.coroutines.flow.Flow

class LocalRepository(
    val teamDao: TeamDao,
    val slotDao: SlotDao,
    val challengeDao: ChallengeDao,
    val matchScoreDao: MatchScoreDao,
    val notificationDao: NotificationDao
) {
    // Teams
    fun getTeamsRanked(): Flow<List<Team>> = teamDao.getTeamsRanked()
    fun getUserTeam(fuid: String): Flow<Team?> = teamDao.getTeamByFirebaseUid(fuid)
    suspend fun getTeamByEmail(email: String): Team? = teamDao.getTeamByEmail(email)
    suspend fun registerTeam(team: Team) = runCatching { teamDao.insertTeam(team) }
    suspend fun updateTeam(team: Team) = runCatching { teamDao.updateTeam(team) }
    suspend fun grantAdmin(email: String) = runCatching { teamDao.grantAdmin(email) }

    // Slots
    fun getSlots(date: String): Flow<List<Slot>> = slotDao.getSlotsByDate(date)
    fun getUserSlots(uid: String): Flow<List<Slot>> = slotDao.getSlotsByUser(uid)
    fun getAllActiveSlots(): Flow<List<Slot>> = slotDao.getAllActiveSlots()
    suspend fun getSlotById(id: String): Slot? = slotDao.getSlotById(id)
    suspend fun canBookOnDate(date: String, uid: String): Boolean =
        slotDao.countUserBookingsOnDate(date, uid) == 0
    suspend fun bookSlot(slot: Slot) = runCatching { slotDao.insertSlot(slot) }
    suspend fun cancelSlot(slot: Slot) = runCatching { slotDao.deleteSlot(slot) }
    suspend fun updateSlot(slot: Slot) = runCatching { slotDao.updateSlot(slot) }

    // Challenges
    fun getPendingChallenges(): Flow<List<Challenge>> = challengeDao.getPendingChallenges()
    fun getChallengesForSlot(slotId: String): Flow<List<Challenge>> = challengeDao.getChallengesForSlot(slotId)
    fun getMyChallenges(uid: String): Flow<List<Challenge>> = challengeDao.getMyChallenges(uid)
    suspend fun postChallenge(c: Challenge) = runCatching { challengeDao.insertChallenge(c) }
    suspend fun updateChallenge(c: Challenge) = runCatching { challengeDao.updateChallenge(c) }

    // Scores
    fun getAllScores(): Flow<List<MatchScore>> = matchScoreDao.getAllScores()
    suspend fun postScore(score: MatchScore) = runCatching { matchScoreDao.insertScore(score) }

    // Notifications
    fun getNotifications(uid: String): Flow<List<AppNotification>> = notificationDao.getNotificationsForUser(uid)
    fun getUnreadCount(uid: String): Flow<Int> = notificationDao.getUnreadCount(uid)
    suspend fun addNotification(n: AppNotification) = runCatching { notificationDao.insert(n) }
    suspend fun markAllRead(uid: String) = runCatching { notificationDao.markAllRead(uid) }
}
