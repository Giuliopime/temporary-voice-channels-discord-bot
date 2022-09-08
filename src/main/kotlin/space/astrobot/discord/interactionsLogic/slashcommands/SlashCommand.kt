package space.astrobot.discord.interactionsLogic.slashcommands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.build.OptionData

// Interface made to have a default execute action for all slash commands
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

    // Required permissions are inherited from the parent command if it exists
    val requiredMemberPermissions: List<Permission> = parentSlashCommand?.requiredMemberPermissions ?: emptyList(),
    val requiredBotPermissions: List<Permission> = parentSlashCommand?.requiredBotPermissions ?: emptyList()
): ExecutableSlashCommand {
    // Compiled path of the slash command (parent name if existing + self name)
    val path = (parentSlashCommand?.name?.plus("/") ?: "") + name
}
