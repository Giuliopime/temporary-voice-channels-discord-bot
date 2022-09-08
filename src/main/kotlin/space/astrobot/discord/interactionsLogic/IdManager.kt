package space.astrobot.discord.interactionsLogic

import com.aventrix.jnanoid.jnanoid.NanoIdUtils

object IdManager {
    // Returns a 21 character pseudo-random string, useful to create Discord component IDs
    fun get(): String = NanoIdUtils.randomNanoId()
}
