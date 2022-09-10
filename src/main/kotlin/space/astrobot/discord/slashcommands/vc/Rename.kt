package space.astrobot.discord.slashcommands.vc

import dev.minn.jda.ktx.coroutines.await
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommand
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandCTX

class Rename: SlashCommand(
    name = "rename",
    description = "Changes the name of your voice channel",
    parentSlashCommand = Vc(),
    options = listOf(
        OptionData(OptionType.STRING, "name", "The new name for the voice channel", true)
    )
) {
    override suspend fun execute(ctx: SlashCommandCTX) {
        val name = ctx.getOption<String>(options[0].name)!!

        ctx.getVoiceChannel().manager.setName(name.take(100)).await()

        ctx.reply("Your channel name has been changed!")
    }
}
