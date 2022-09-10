package space.astrobot.discord.slashcommands.vc

import net.dv8tion.jda.api.Permission
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommand
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandCategory

class Vc: SlashCommand(
    name = "vc",
    description = "Parent of all vc commands",
    category = SlashCommandCategory.VC,
    requiredBotPermissions = listOf(Permission.MANAGE_CHANNEL, Permission.MANAGE_ROLES)
) {
}
