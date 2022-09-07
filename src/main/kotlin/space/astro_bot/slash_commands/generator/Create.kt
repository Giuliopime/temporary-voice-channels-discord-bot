package space.astro_bot.slash_commands.generator

import dev.minn.jda.ktx.coroutines.await
import net.dv8tion.jda.api.entities.Category
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import space.astro_bot.slash_commands.SlashCommand
import space.astro_bot.slash_commands.SlashCommandCTX

class Create: SlashCommand(
    name = "create",
    description = "Creates a generator for temporary voice channels",
    parentSlashCommand = Generator(),
    options = listOf(
        OptionData(OptionType.CHANNEL, "category", "The category where the generator will be created")
            .setChannelTypes(ChannelType.CATEGORY)
    )
) {
    override suspend fun execute(ctx: SlashCommandCTX) {
        val action = ctx.guild.createVoiceChannel("VC Generator")

        ctx.getOption<Category>(options[0].name)?.let {
            action.setParent(it)
        }

        val generator = action.await()


    }
}
