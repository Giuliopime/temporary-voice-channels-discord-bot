package space.astrobot.discord.slashcommands.generator

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.await
import dev.minn.jda.ktx.interactions.components.SelectMenu
import dev.minn.jda.ktx.interactions.components.SelectOption
import kotlinx.coroutines.withTimeoutOrNull
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent
import net.dv8tion.jda.api.interactions.components.ActionRow
import space.astrobot.Bot
import space.astrobot.db.interactors.GuildsDBI
import space.astrobot.discord.interactionsLogic.IdManager
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommand
import space.astrobot.discord.interactionsLogic.slashcommands.SlashCommandCTX

class Delete: SlashCommand(
    name = "delete",
    description = "Deletes a generator",
    parentSlashCommand = Generator()
) {
    override suspend fun execute(ctx: SlashCommandCTX) {
        val generators = ctx.guildDto.generators
        val menu = SelectMenu(
            customId = IdManager.get(),
            placeholder = "Select the generator to delete",
            options = generators.mapIndexed { index, generatorDto -> SelectOption(
                label = "${index + 1}) <@${generatorDto.id}>",
                value = index.toString()
            )}
        )

        ctx.reply("Select the generator to delete with the menu below\n(You have 60 seconds)", ActionRow.of(menu))

        /*
        Waits for a selection on the menu created above.
        If no selectiong gets made after 60 seconds, the elvis operator will get triggered
        thanks to the withTimeoutOrNull method.
         */
        withTimeoutOrNull(60000) {
            val event = Bot.jda.await<SelectMenuInteractionEvent> {
                it.componentId == menu.id
            }

            return@withTimeoutOrNull event.values.first().toIntOrNull()
        }?.let { index ->
            // If the user selected a valid option delete the related generator both from Discord and the database
            val generatorDeleted = generators.removeAt(index)
            ctx.guild.getVoiceChannelById(generatorDeleted.id)?.delete()?.await()
            GuildsDBI.updateValue(ctx.guildId, "generators", generators)
            ctx.reply("Generator deleted!")
        } ?: ctx.reply("Action canceled") // Otherwise don't delete anything
    }
}
