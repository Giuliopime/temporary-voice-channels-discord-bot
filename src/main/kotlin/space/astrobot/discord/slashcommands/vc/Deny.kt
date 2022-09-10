package space.astrobot.discord.slashcommands.vc

import dev.minn.jda.ktx.coroutines.await
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommand
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandCTX

class Deny: SlashCommand(
    name = "deny",
    description = "Denies a user from joining your voice channel",
    parentSlashCommand = Vc(),
    options = listOf(
        OptionData(OptionType.USER, "user", "The user to deny", true)
    )
) {
    override suspend fun execute(ctx: SlashCommandCTX) {
        val user = ctx.getOption<User>(options[0].name)!!

        if(!ctx.guild.isMember(user)) {
            ctx.reply("The user is not from this server!")
            return
        }

        ctx.getVoiceChannel().manager.putMemberPermissionOverride(
            user.idLong,
            listOf(),
            listOf(Permission.VOICE_CONNECT)
        ).await()

        ctx.reply("<@${user.id}> cannot join your voice channel anymore!")
    }
}
