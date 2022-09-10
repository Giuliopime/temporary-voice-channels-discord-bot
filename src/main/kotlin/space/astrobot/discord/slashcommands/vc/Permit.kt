package space.astrobot.discord.slashcommands.vc

import dev.minn.jda.ktx.coroutines.await
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommand
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandCTX

class Permit: SlashCommand(
    name = "permit",
    description = "Permits a user to join your voice channel",
    parentSlashCommand = Vc(),
    options = listOf(
        OptionData(OptionType.USER, "user", "The user to permit", true)
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
            listOf(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT),
            listOf()
        ).await()

        ctx.reply("<@${user.id}> can now join your voice channel!")
    }
}
