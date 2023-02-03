package space.astrobot.discord.events

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import space.astrobot.db.interactors.GuildsDBI
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandCTX
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandCategory
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandsManager
import space.astrobot.redis.TempVoiceChannelsRI

suspend fun onSlashCommand(event: SlashCommandInteractionEvent) {
    // Do not listen to DMs
    if (!event.isFromGuild)
        return

    /*
    A command which is not present in the code can be still be present on Discord
    if the bot hasn't updated the commands yet
     */
    val slashCommand = SlashCommandsManager.get(event.fullCommandName) ?: run {
        event.reply("This command is not available anymore as it's outdated.")
            .setEphemeral(true)
            .queue()
        return
    }

    // Gets the guild settings from the database
    val guildDto = GuildsDBI.getOrCreate(event.guild!!.id)

    // Check if the bot has the required permissions
    if (!event.guild!!.selfMember.hasPermission(slashCommand.requiredBotPermissions)) {
        event.reply("I need to following permissions to be able to run this command:\n" +
                slashCommand.requiredBotPermissions.joinToString("\n") { it.getName() }
        ).queue()
        return
    }

    val ctx = if (slashCommand.category == SlashCommandCategory.VC) {
        val member = event.member!!
        val activeTempVoiceChannels = TempVoiceChannelsRI.getAllFromGuild(event.guild!!.id)
        val tempVoiceChannelIndex = activeTempVoiceChannels.indexOfFirst { it.id == member.voiceState!!.channel?.id }

        if (tempVoiceChannelIndex == -1) {
            event.reply("You need to be in a temporary voice channel to use this command!")
                .setEphemeral(true)
                .queue()
            return
        }

        if (activeTempVoiceChannels[tempVoiceChannelIndex].ownerId != event.user.id) {
            event.reply("You are not the owner of this temporary voice channel!" +
                    "\nThe owner is <@${activeTempVoiceChannels[tempVoiceChannelIndex].ownerId}>")
                .setEphemeral(true)
                .queue()
        }

        SlashCommandCTX(event, guildDto, activeTempVoiceChannels[tempVoiceChannelIndex])
    } else
        SlashCommandCTX(event, guildDto)

    slashCommand.execute(ctx)
}
