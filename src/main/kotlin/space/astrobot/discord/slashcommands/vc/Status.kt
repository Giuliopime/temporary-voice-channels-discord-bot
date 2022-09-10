package space.astrobot.discord.slashcommands.vc

import dev.minn.jda.ktx.coroutines.await
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommand
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandCTX

class Status: SlashCommand(
    name = "status",
    description = "Opens, closes, hides and un-hides your voice channel",
    parentSlashCommand = Vc(),
    options = listOf(
        OptionData(OptionType.STRING, "action", "Choose the status of the voice channel", true)
            .addChoices(
                Command.Choice("Open", "open"),
                Command.Choice("Close", "close"),
                Command.Choice("Hide", "hide"),
                Command.Choice("Un-hide", "un-hide")
            )
    )
) {
    override suspend fun execute(ctx: SlashCommandCTX) {
        val action = ctx.getOption<String>(options[0].name)!!

        var denyPermission = false
        var targetPermission = Permission.VIEW_CHANNEL
        var channelStatus = ""
        when (action) {
            "open" -> {
                targetPermission = Permission.VOICE_CONNECT
                channelStatus = "open"
            }
            "close" -> {
                denyPermission = true
                targetPermission = Permission.VOICE_CONNECT
                channelStatus = "closed"
            }
            "hide" -> {
                denyPermission = true
                targetPermission = Permission.VIEW_CHANNEL
                channelStatus = "hidden"
            }
            "un-hide" -> {
                targetPermission = Permission.VIEW_CHANNEL
                channelStatus = "un-hidden"
            }
        }

        ctx.getVoiceChannel().upsertPermissionOverride(ctx.guild.publicRole).let {
            if (denyPermission)
                it.deny(targetPermission)
            else
                it.grant(targetPermission)
        }.await()

        ctx.reply("Your channel is now **$channelStatus**!")
    }
}
