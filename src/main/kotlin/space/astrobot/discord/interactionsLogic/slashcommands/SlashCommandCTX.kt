package space.astrobot.discord.interactionsLogic.slashcommands

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.interactions.components.getOption
import dev.minn.jda.ktx.messages.Embed
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.InteractionHook
import net.dv8tion.jda.api.interactions.components.ActionRow
import space.astrobot.models.GuildDto
import space.astrobot.models.TempVCDto

class SlashCommandCTX(
    val event: SlashCommandInteractionEvent,
    val guildDto: GuildDto,
    val tempVCDto: TempVCDto? = null
) {
    val guild = event.guild!!
    val guildId = guild.id

    val channel = event.channel

    val user = event.user
    val userId = user.id
    val member = event.member!!

    // Needed to be able to send one reply and then edit it as needed instead of sending new ones each time
    private var interactionHook: InteractionHook? = null

    inline fun <reified T> getOption(name: String): T? = event.getOption<T>(name)

    suspend fun reply(message: String, vararg actionRows: ActionRow, ephemeral: Boolean = true) {
        val embed = Embed {
            color = guild.selfMember.colorRaw
            description = message
        }

        val actionRowsList = actionRows.asList()

        // If the bot still haven't replied to this command then reply normally
        if (interactionHook == null) {
            val action = event.replyEmbeds(embed).setEphemeral(ephemeral)
            if (actionRowsList.isNotEmpty())
                action.setComponents(actionRowsList)
            // Save the hook to later edit the reply if needed
            interactionHook = action.await()
        } else {
            // If a reply has already been sent it needs to be edited instead
            interactionHook?.editOriginalEmbeds(embed)?.setComponents(actionRowsList)?.await()
        }
    }

    // Only use if in SlashCommandCategory.VC
    fun getVoiceChannel() = member.voiceState!!.channel!! as VoiceChannel
}
