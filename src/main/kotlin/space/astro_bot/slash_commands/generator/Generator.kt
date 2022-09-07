package space.astro_bot.slash_commands.generator

import net.dv8tion.jda.api.Permission
import space.astro_bot.slash_commands.SlashCommand
import space.astro_bot.slash_commands.SlashCommandCTX

class Generator: SlashCommand(
    name = "generator",
    description = "Parent command for all generator commands",
    requiredBotPermissions = listOf(Permission.MANAGE_CHANNEL),
    requiredMemberPermissions = listOf(Permission.MANAGE_CHANNEL)
)