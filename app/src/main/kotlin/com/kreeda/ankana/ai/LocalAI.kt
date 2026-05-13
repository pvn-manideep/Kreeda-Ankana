package com.kreeda.ankana.ai

import com.kreeda.ankana.model.Challenge
import com.kreeda.ankana.model.Slot
import com.kreeda.ankana.model.Team
import java.text.SimpleDateFormat
import java.util.*

data class AIResult(
    val message: String,
    val action: LocalAIAction? = null
)

sealed class LocalAIAction {
    data class Book(val time: String, val date: String, val sport: String) : LocalAIAction()
    data class Cancel(val slotId: String) : LocalAIAction()
    data class Reschedule(val slotId: String, val newDate: String, val newTime: String) : LocalAIAction()
    data class AcceptChallenge(val challengeId: String) : LocalAIAction()
}

data class AIContext(
    val today: String,
    val selectedDate: String,
    val team: Team?,
    val slots: List<Slot>,          // selected date
    val mySlots: List<Slot>,        // user's bookings
    val allActive: List<Slot>,
    val challenges: List<Challenge>
)

object LocalAI {
    private val TIMES = listOf("6 AM","8 AM","10 AM","12 PM","2 PM","4 PM","6 PM","8 PM")
    private val SPORTS = listOf("Volleyball","Cricket","Football","Kabaddi","Badminton","Kho-Kho")
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displaySdf = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    fun respond(query: String, ctx: AIContext): AIResult {
        val q = query.lowercase().trim()
        val team = ctx.team

        // ── Greetings ──────────────────────────────────────────────────────
        if (q.matches(Regex("(hi|hello|hey|namaste|sup|yo|hii|helo).*"))) {
            val name = team?.name ?: "champ"
            return AIResult("Hey $name! 👋 I'm KreedaBot. Tell me — want to book a slot, check what's available, or something else?")
        }

        // ── Help / What can you do ─────────────────────────────────────────
        if (q.contains("help") || q.contains("what can you") || q.contains("how do")) {
            return AIResult("""
Here's what I can do for you:
• 📅 **Book a slot** — "Book 4 PM tomorrow for Volleyball"
• ❌ **Cancel** — "Cancel my booking"
• 🔄 **Reschedule** — "Reschedule my slot to 6 PM"
• ⚔️ **Accept challenge** — "Accept the Volleyball challenge"
• 📊 **Check availability** — "What slots are free today?"
• 🔴 **Who's playing** — "Who's live right now?"
Just tell me naturally — I'll figure it out!
            """.trimIndent())
        }

        // ── Available slots ────────────────────────────────────────────────
        if (q.contains("available") || q.contains("free") || q.contains("open slot") ||
            q.contains("vacant") || q.contains("which slot") || q.contains("any slot")) {
            val booked = ctx.slots.map { it.time }.toSet()
            val free = TIMES.filter { it !in booked }
            val dateDisplay = displayDate(ctx.selectedDate)
            return if (free.isEmpty()) {
                AIResult("All slots are taken on $dateDisplay! 😅 Try checking a different date — tap the calendar strip to switch.")
            } else {
                AIResult("On $dateDisplay, these slots are FREE: **${free.joinToString(", ")}** 🟢\n\nWant me to book one? Just say which time and sport!")
            }
        }

        // ── Who is playing / live ──────────────────────────────────────────
        if (q.contains("playing") || q.contains("live") || q.contains("happening") || q.contains("right now")) {
            val playing = ctx.allActive.filter { it.status == "Playing" }
            val upcoming = ctx.allActive.filter { it.status == "Booked" }.take(3)
            return if (playing.isEmpty() && upcoming.isEmpty()) {
                AIResult("No matches happening right now. The ground is all yours! 🏟")
            } else {
                val sb = StringBuilder()
                if (playing.isNotEmpty()) {
                    sb.appendLine("🔴 **Live right now:**")
                    playing.forEach { sb.appendLine("  • ${it.bookedByTeam} vs ${it.opponentTeam.ifBlank{"???"}} — ${it.sport} (${it.time})") }
                }
                if (upcoming.isNotEmpty()) {
                    sb.appendLine("📅 **Coming up:**")
                    upcoming.forEach { sb.appendLine("  • ${it.bookedByTeam} — ${it.sport} at ${it.time} on ${it.date}") }
                }
                AIResult(sb.toString().trim())
            }
        }

        // ── My bookings ────────────────────────────────────────────────────
        if (q.contains("my booking") || q.contains("my slot") || q.contains("i booked") || q.contains("when am i")) {
            val active = ctx.mySlots.filter { it.status in listOf("Booked","Playing") }
            return if (active.isEmpty()) {
                AIResult("You don't have any upcoming bookings. Want me to book a slot? Just say the time, date and sport!")
            } else {
                val sb = StringBuilder("Your upcoming bookings:\n")
                active.forEach { sb.appendLine("  📅 ${it.sport} at ${it.time} on ${it.date} — ${it.status}") }
                AIResult(sb.toString().trim())
            }
        }

        // ── BOOK a slot ────────────────────────────────────────────────────
        if (q.contains("book") || q.contains("reserve") || q.contains("slot") && (q.contains("want") || q.contains("need") || q.contains("get"))) {
            if (team == null) return AIResult("You need to register your team first before booking. Head to the Profile tab!")
            val time = extractTime(q)
            val sport = extractSport(q) ?: team.sport.ifBlank { "Volleyball" }
            val date = extractDate(q, ctx.today)
            val dateDisplay = displayDate(date)
            val booked = ctx.slots.find { it.time == time && it.date == date }
            return when {
                time == null -> AIResult("Which time slot do you want? Available times: ${TIMES.joinToString(", ")}. Try: \"Book 4 PM today for Cricket\"")
                booked != null -> AIResult("Oops! ${time} on $dateDisplay is already taken by ${booked.bookedByTeam}. Pick another time — free slots: ${getFreeSlots(ctx, date)}")
                else -> AIResult(
                    "Got it! I'll book **$time on $dateDisplay** for **$sport** for team **${team.name}**. Tap Confirm to lock it in! 🔒",
                    LocalAIAction.Book(time, date, sport)
                )
            }
        }

        // ── CANCEL ─────────────────────────────────────────────────────────
        if (q.contains("cancel") || q.contains("remove booking") || q.contains("delete slot")) {
            if (team == null) return AIResult("Register your team first!")
            val active = ctx.mySlots.filter { it.status in listOf("Booked","Playing") }
            return when {
                active.isEmpty() -> AIResult("You don't have any active bookings to cancel.")
                active.size == 1 -> {
                    val slot = active[0]
                    AIResult(
                        "I'll cancel your **${slot.sport}** slot at **${slot.time} on ${slot.date}**. Confirm?",
                        LocalAIAction.Cancel(slot.id)
                    )
                }
                else -> {
                    val listing = active.mapIndexed { i, s -> "${i+1}. ${s.sport} at ${s.time} on ${s.date}" }.joinToString("\n")
                    AIResult("You have multiple bookings:\n$listing\n\nTell me which one — e.g. \"Cancel my 4 PM slot\" or \"Cancel Thursday's booking\".")
                }
            }
        }

        // ── RESCHEDULE ─────────────────────────────────────────────────────
        if (q.contains("reschedule") || q.contains("move") && q.contains("slot") || q.contains("change") && q.contains("time")) {
            if (team == null) return AIResult("Register your team first!")
            val active = ctx.mySlots.filter { it.status == "Booked" }
            if (active.isEmpty()) return AIResult("No bookings to reschedule. Want to book a new slot?")
            val slot = active.first()
            val newTime = extractTime(q)
            val newDate = extractDate(q, ctx.today)
            return if (newTime != null) {
                AIResult(
                    "I'll reschedule your **${slot.sport}** booking from ${slot.time} on ${slot.date} → **$newTime on ${displayDate(newDate)}**. Confirm?",
                    LocalAIAction.Reschedule(slot.id, newDate, newTime)
                )
            } else {
                AIResult("What time should I reschedule to? Your current booking: ${slot.sport} at ${slot.time} on ${slot.date}. Say e.g. \"Reschedule to 6 PM tomorrow\".")
            }
        }

        // ── CHALLENGES ─────────────────────────────────────────────────────
        if (q.contains("challenge") || q.contains("opponent") || q.contains("compete") || q.contains("match")) {
            val open = ctx.challenges.filter { it.status == "pending" && it.fromTeamUid != team?.uid }
            return if (open.isNotEmpty()) {
                val ch = open.first()
                val sport = extractSport(q)
                val match = if (sport != null) open.firstOrNull { it.sport.equals(sport, ignoreCase = true) } else open.first()
                if (match != null) {
                    AIResult(
                        "Found a **${match.sport}** challenge from **${match.fromTeam}** at ${match.time} on ${match.date}! \"${match.message.ifBlank{"Let's play!"}}\"\n\nShould I accept it for you?",
                        LocalAIAction.AcceptChallenge(match.id)
                    )
                } else {
                    AIResult("There are ${open.size} open challenges right now. Check the Challenges tab to view and accept them!")
                }
            } else {
                AIResult("No open challenges right now. You can post one from your booked slot in the Booking tab — tap your slot and hit 'Post Challenge'!")
            }
        }

        // ── Scoreboard / Rankings ──────────────────────────────────────────
        if (q.contains("score") || q.contains("rank") || q.contains("leaderboard") || q.contains("winner") || q.contains("who won")) {
            return AIResult("Check the Scores tab for the full leaderboard and match results! 🏆 The top teams are ranked by wins.")
        }

        // ── Admin questions ────────────────────────────────────────────────
        if (q.contains("admin") || q.contains("manideep")) {
            return AIResult("The admin is 6manideep@gmail.com. They can post scores, manage all bookings, and grant admin access to others.")
        }

        // ── Time / Date info ───────────────────────────────────────────────
        if (q.contains("today") && (q.contains("date") || q.contains("day"))) {
            return AIResult("Today is ${displayDate(ctx.today)} 📅. You're viewing ${displayDate(ctx.selectedDate)}.")
        }

        // ── Default smart fallback ─────────────────────────────────────────
        val freeCount = TIMES.count { t -> ctx.slots.none { it.time == t } }
        val liveCount = ctx.allActive.count { it.status == "Playing" }
        return AIResult(
            "Hmm, I'm not sure I got that! 🤔 Here's what I know right now:\n" +
            "• $freeCount free slots available on ${displayDate(ctx.selectedDate)}\n" +
            "• $liveCount matches currently live\n" +
            "• ${ctx.challenges.count { it.status == "pending" }} open challenges\n\n" +
            "Try: \"Book 4 PM today\", \"What's free?\", \"Cancel my slot\", or \"Show challenges\""
        )
    }

    // ── Extractors ────────────────────────────────────────────────────────────
    private fun extractTime(q: String): String? {
        val patterns = mapOf(
            Regex("6\\s*am|six\\s*am|morning\\s*6") to "6 AM",
            Regex("8\\s*am|eight\\s*am|morning\\s*8") to "8 AM",
            Regex("10\\s*am|ten\\s*am") to "10 AM",
            Regex("12\\s*pm|noon|lunch") to "12 PM",
            Regex("2\\s*pm|14:00|two\\s*pm") to "2 PM",
            Regex("4\\s*pm|16:00|four\\s*pm|evening") to "4 PM",
            Regex("6\\s*pm|18:00|six\\s*pm") to "6 PM",
            Regex("8\\s*pm|20:00|eight\\s*pm|night") to "8 PM"
        )
        return patterns.entries.firstOrNull { it.key.containsMatchIn(q) }?.value
    }

    private fun extractSport(q: String): String? {
        return SPORTS.firstOrNull { q.contains(it.lowercase()) }
    }

    private fun extractDate(q: String, today: String): String {
        val cal = Calendar.getInstance()
        return when {
            q.contains("tomorrow") -> { cal.add(Calendar.DATE, 1); sdf.format(cal.time) }
            q.contains("day after") -> { cal.add(Calendar.DATE, 2); sdf.format(cal.time) }
            q.contains("monday") -> nextWeekday(cal, Calendar.MONDAY)
            q.contains("tuesday") -> nextWeekday(cal, Calendar.TUESDAY)
            q.contains("wednesday") -> nextWeekday(cal, Calendar.WEDNESDAY)
            q.contains("thursday") -> nextWeekday(cal, Calendar.THURSDAY)
            q.contains("friday") -> nextWeekday(cal, Calendar.FRIDAY)
            q.contains("saturday") -> nextWeekday(cal, Calendar.SATURDAY)
            q.contains("sunday") -> nextWeekday(cal, Calendar.SUNDAY)
            else -> today
        }
    }

    private fun nextWeekday(cal: Calendar, dayOfWeek: Int): String {
        val c = cal.clone() as Calendar
        while (c.get(Calendar.DAY_OF_WEEK) != dayOfWeek) c.add(Calendar.DATE, 1)
        return sdf.format(c.time)
    }

    private fun displayDate(date: String): String = try {
        displaySdf.format(sdf.parse(date)!!)
    } catch (e: Exception) { date }

    private fun getFreeSlots(ctx: AIContext, date: String): String {
        val booked = ctx.slots.filter { it.date == date }.map { it.time }.toSet()
        return TIMES.filter { it !in booked }.joinToString(", ").ifEmpty { "none" }
    }
}
