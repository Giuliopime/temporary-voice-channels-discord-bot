package space.astro_bot

import io.github.cdimascio.dotenv.dotenv
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import space.astro_bot.events.EventsManager

fun main() {
    val dotenv = dotenv()

    val jda = JDABuilder.createDefault(dotenv["DISCORD_TOKEN"])
        .setActivity(Activity.watching(" your voice channels!"))
        .build()

    EventsManager.manage(jda)
}