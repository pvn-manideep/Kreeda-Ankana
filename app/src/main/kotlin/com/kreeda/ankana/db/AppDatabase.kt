package com.kreeda.ankana.db

import androidx.room.*
import com.kreeda.ankana.model.*
import kotlinx.coroutines.flow.Flow

@Dao interface TeamDao {
    @Query("SELECT * FROM teams ORDER BY wins DESC LIMIT 50")
    fun getTeamsRanked(): Flow<List<Team>>
    @Query("SELECT * FROM teams WHERE firebaseUid = :fuid LIMIT 1")
    fun getTeamByFirebaseUid(fuid: String): Flow<Team?>
    @Query("SELECT * FROM teams WHERE uid = :uid LIMIT 1")
    fun getTeamByUid(uid: String): Flow<Team?>
    @Query("SELECT * FROM teams WHERE email = :email LIMIT 1")
    suspend fun getTeamByEmail(email: String): Team?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertTeam(team: Team)
    @Update suspend fun updateTeam(team: Team)
    @Query("UPDATE teams SET role = 'admin' WHERE email = :email")
    suspend fun grantAdmin(email: String)
}

@Dao interface SlotDao {
    @Query("SELECT * FROM slots WHERE date = :date ORDER BY time")
    fun getSlotsByDate(date: String): Flow<List<Slot>>
    @Query("SELECT * FROM slots WHERE bookedByUid = :uid ORDER BY date, time")
    fun getSlotsByUser(uid: String): Flow<List<Slot>>
    @Query("SELECT * FROM slots WHERE status IN ('Booked','Playing') ORDER BY date, time")
    fun getAllActiveSlots(): Flow<List<Slot>>
    @Query("SELECT COUNT(*) FROM slots WHERE date = :date AND bookedByUid = :uid AND status != 'Completed'")
    suspend fun countUserBookingsOnDate(date: String, uid: String): Int
    @Query("SELECT * FROM slots WHERE id = :id LIMIT 1")
    suspend fun getSlotById(id: String): Slot?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertSlot(slot: Slot)
    @Update suspend fun updateSlot(slot: Slot)
    @Delete suspend fun deleteSlot(slot: Slot)
}

@Dao interface ChallengeDao {
    @Query("SELECT * FROM challenges WHERE status = 'pending' ORDER BY rowid DESC")
    fun getPendingChallenges(): Flow<List<Challenge>>
    @Query("SELECT * FROM challenges WHERE toSlotId = :slotId AND status = 'pending'")
    fun getChallengesForSlot(slotId: String): Flow<List<Challenge>>
    @Query("SELECT * FROM challenges WHERE fromTeamUid = :uid ORDER BY rowid DESC")
    fun getMyChallenges(uid: String): Flow<List<Challenge>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertChallenge(challenge: Challenge)
    @Update suspend fun updateChallenge(challenge: Challenge)
    @Delete suspend fun deleteChallenge(challenge: Challenge)
}

@Dao interface MatchScoreDao {
    @Query("SELECT * FROM match_scores ORDER BY rowid DESC LIMIT 100")
    fun getAllScores(): Flow<List<MatchScore>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertScore(score: MatchScore)
}

@Dao interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE toUid = :uid ORDER BY timestamp DESC LIMIT 50")
    fun getNotificationsForUser(uid: String): Flow<List<AppNotification>>
    @Query("SELECT COUNT(*) FROM notifications WHERE toUid = :uid AND isRead = 0")
    fun getUnreadCount(uid: String): Flow<Int>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(n: AppNotification)
    @Update suspend fun update(n: AppNotification)
    @Query("UPDATE notifications SET isRead = 1 WHERE toUid = :uid")
    suspend fun markAllRead(uid: String)
}

@Database(
    entities = [Team::class, Slot::class, Challenge::class, MatchScore::class, AppNotification::class],
    version = 6, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teamDao(): TeamDao
    abstract fun slotDao(): SlotDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun matchScoreDao(): MatchScoreDao
    abstract fun notificationDao(): NotificationDao
}
