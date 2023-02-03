package space.astrobot.discord.slashcommands.generator

import dev.minn.jda.ktx.coroutines.await
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.concrete.Category
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import space.astrobot.db.interactors.GuildsDBI
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommand
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandCTX
import space.astrobot.models.GeneratorDto

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

        GuildsDBI.pushValue(ctx.guildId, "generators", GeneratorDto(generator.id))

        ctx.reply("You can now create temporary voice channels by joining ${generator.asMention}!")
    }
}
