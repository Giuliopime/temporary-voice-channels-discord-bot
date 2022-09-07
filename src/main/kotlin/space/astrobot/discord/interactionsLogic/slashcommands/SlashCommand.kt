package space.astrobot.discord.interactionsLogic.slashcommands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.build.OptionData

interface ExecutableSlashCommand {
    suspend fun execute(ctx: SlashCommandCTX) {
        ctx.event.reply("Command outdated or not implemented!").queue()
    }
}

abstract class SlashCommand(
    val name: String,
    val description: String,

    val parentSlashCommand: SlashCommand? = null,

    val options: List<OptionData> = emptyList(),

    val requiredMemberPermissions: List<Permission> = parentSlashCommand?.requiredMemberPermissions ?: emptyList(),
    val requiredBotPermissions: List<Permission> = parentSlashCommand?.requiredBotPermissions ?: emptyList()
): ExecutableSlashCommand {
    val path = (parentSlashCommand?.name?.plus("/") ?: "") + name
}
