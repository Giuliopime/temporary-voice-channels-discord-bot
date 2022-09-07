package space.astrobot.discord.interactionsLogic

import com.aventrix.jnanoid.jnanoid.NanoIdUtils

object IdManager {
    fun get(): String = NanoIdUtils.randomNanoId()
}
