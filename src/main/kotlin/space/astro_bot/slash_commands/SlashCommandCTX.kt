package space.astro_bot.slash_commands

import dev.minn.jda.ktx.interactions.components.getOption
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import kotlin.reflect.KClass

class SlashCommandCTX(
    val event: SlashCommandInteractionEvent
) {
    val guild = event.guild!!
    val channel = event.channel

    inline fun <reified T> getOption(name: String): T? = event.getOption<T>(name)
}
