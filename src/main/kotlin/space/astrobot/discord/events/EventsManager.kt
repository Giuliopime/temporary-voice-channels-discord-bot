package space.astrobot.discord.events

import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object EventsManager {
    fun manage(jda: JDA) {
        jda.listener<GenericEvent> {
            when(it) {
                is SlashCommandInteractionEvent -> onSlashCommand(it)
                is GuildVoiceUpdateEvent -> onGuildVoiceUpdate(it)
            }
        }
    }
}
