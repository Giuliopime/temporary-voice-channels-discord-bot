package space.astrobot.discord.interactionsLogic.slashcommands

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.interactions.components.getOption
import dev.minn.jda.ktx.messages.Embed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.InteractionHook
import net.dv8tion.jda.api.interactions.components.ActionRow
import space.astrobot.models.GuildDto

class SlashCommandCTX(
    val event: SlashCommandInteractionEvent,
    val guildDto: GuildDto
) {
    val guild = event.guild!!
    val guildId = guild.id

    val channel = event.channel

    val user = event.user
    val userId = user.id
    val member = event.member!!

    private var interactionHook: InteractionHook? = null

    inline fun <reified T> getOption(name: String): T? = event.getOption<T>(name)

    suspend fun reply(message: String, vararg actionRows: ActionRow, ephemeral: Boolean = true) {
        val embed = Embed {
            color = guild.selfMember.colorRaw
            description = message
            footer {
                name = "Astro Devlog YouTube serie"
            }
        }

        val actionRowsList = actionRows.asList()

        if (interactionHook == null) {
            val action = event.replyEmbeds(embed).setEphemeral(ephemeral)
            if (actionRowsList.isNotEmpty())
                action.setComponents(actionRowsList)
            interactionHook = action.await()
        } else {
            interactionHook?.editOriginalEmbeds(embed)?.setComponents(actionRowsList)?.await()
        }
    }
}
