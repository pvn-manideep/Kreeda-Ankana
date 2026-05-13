package com.kreeda.ankana.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "teams")
data class Team(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val sport: String = "",
    val motto: String = "Valor and Victory.",
    val wins: Int = 0,
    val losses: Int = 0,
    val matchesPlayed: Int = 0,
    val email: String = "",
    val uid: String = "",
    val role: String = "user",   // "user" | "admin"
    val status: String = "active",
    val village: String = "",
    val playerCount: Int = 0,
    val phone: String = "",
    val firebaseUid: String = ""  // Firebase Auth UID
)

@Entity(tableName = "slots")
data class Slot(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val time: String = "",
    val date: String = "",
    val status: String = "Vacant",   // Vacant | Booked | Playing | Completed
    val bookedByEmail: String = "",
    val bookedByTeam: String = "",
    val bookedByUid: String = "",
    val sport: String = "",
    val opponentTeam: String = "",
    val challengeId: String = "",
    val rescheduleRequestedTo: String = "",   // new date if reschedule pending
    val rescheduleStatus: String = ""          // "" | "pending" | "approved" | "rejected"
)

@Entity(tableName = "challenges")
data class Challenge(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val fromTeam: String = "",
    val fromTeamUid: String = "",
    val toSlotId: String = "",          // the booked slot being challenged
    val toTeam: String = "",
    val sport: String = "",
    val time: String = "",
    val date: String = "",
    val message: String = "",
    val status: String = "pending"      // pending | accepted | declined
)

@Entity(tableName = "match_scores")
data class MatchScore(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val slotId: String = "",
    val date: String = "",
    val team1: String = "",
    val team2: String = "",
    val sport: String = "",
    val score1: Int = 0,
    val score2: Int = 0,
    val winner: String = "",
    val notes: String = ""
)

@Entity(tableName = "notifications")
data class AppNotification(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val toUid: String = "",
    val title: String = "",
    val body: String = "",
    val type: String = "",   // challenge | reschedule | score | system
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val refId: String = ""   // slotId or challengeId
)
