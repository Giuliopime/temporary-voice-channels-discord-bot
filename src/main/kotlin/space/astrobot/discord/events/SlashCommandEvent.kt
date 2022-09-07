package space.astrobot.discord.events

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import space.astrobot.db.interactors.GuildsDBI
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandCTX
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandsManager

suspend fun onSlashCommand(event: SlashCommandInteractionEvent) {
    if (!event.isFromGuild)
        return

    val slashCommand = SlashCommandsManager.get(event.commandPath) ?: run {
        event.reply("This command is not available anymore as it's outdated.")
            .setEphemeral(true)
            .queue()
        return
    }

    val guildDto = GuildsDBI.getOrCreate(event.guild!!.id)
    val ctx = SlashCommandCTX(event, guildDto)

    if (ctx.guild.selfMember.hasPermission(slashCommand.requiredBotPermissions)) {
        event.reply("I need to following permissions to be able to run this command:\n" +
                slashCommand.requiredBotPermissions.joinToString("\n") { it.getName() }
        )
        return
    }

    if (ctx.member.hasPermission(slashCommand.requiredMemberPermissions)) {
        event.reply("You need to following permissions to be able to run this command:\n" +
                slashCommand.requiredMemberPermissions.joinToString("\n") { it.getName() }
        )
    }

    slashCommand.execute(ctx)
}
