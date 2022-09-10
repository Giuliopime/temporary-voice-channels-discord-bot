package space.astrobot.discord.events

import dev.minn.jda.ktx.coroutines.await
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import space.astrobot.db.interactors.GuildsDBI
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandCTX
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandsManager

suspend fun onSlashCommand(event: SlashCommandInteractionEvent) {
    // Do not listen to DMs
    if (!event.isFromGuild)
        return

    /*
    A command which is not present in the code can be still be present on Discord
    if the bot hasn't updated the commands yet
     */
    val slashCommand = SlashCommandsManager.get(event.commandPath) ?: run {
        event.reply("This command is not available anymore as it's outdated.")
            .setEphemeral(true)
            .queue()
        return
    }

    // Gets the guild settings from the database
    val guildDto = GuildsDBI.getOrCreate(event.guild!!.id)
    val ctx = SlashCommandCTX(event, guildDto)

    // Check if the bot has the required permissions
    if (!ctx.guild.selfMember.hasPermission(slashCommand.requiredBotPermissions)) {
        event.reply("I need to following permissions to be able to run this command:\n" +
                slashCommand.requiredBotPermissions.joinToString("\n") { it.getName() }
        ).queue()
        return
    }

    slashCommand.execute(ctx)
}
