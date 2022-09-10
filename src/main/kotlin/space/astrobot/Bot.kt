package space.astrobot

import dev.minn.jda.ktx.jdabuilder.injectKTX
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity.ActivityType
import net.dv8tion.jda.api.entities.Activity.of
import net.dv8tion.jda.api.utils.MemberCachePolicy
import space.astrobot.discord.events.EventsManager
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandsManager

object Bot {
    lateinit var jda: JDA

    suspend fun start() {
        jda = JDABuilder.createDefault(Env.Discord.token)
            .setMemberCachePolicy(MemberCachePolicy.VOICE)
            .setActivity(of(ActivityType.fromKey(Env.Discord.activity_type_key), Env.Discord.activity))
            .injectKTX() // Injects JDA-KTX library
            .build()
            .awaitReady() // Waits until JDA finishes loading

        EventsManager.manage(jda)

        if (Env.Discord.update_slash_commands)
            SlashCommandsManager.updateOnDiscord()
    }
}
