package space.astrobot.discord.interactionsLogic.slashcommands

import dev.minn.jda.ktx.coroutines.await
import mu.KotlinLogging
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.reflections.Reflections
import space.astrobot.Bot

private val logger = KotlinLogging.logger {}

object SlashCommandsManager {
    private var commands: Set<SlashCommand> = setOf()
    private var commandsMap: Map<String, SlashCommand> = mapOf()

    init {
        // Read and instantiate all slash commands
        val reflection = Reflections("space.astrobot.discord.slashcommands")

        commands = reflection.getSubTypesOf(SlashCommand::class.java)
            .map { it.getDeclaredConstructor().newInstance() }
            .toSet()

        // Check if they all have a valid name and description
        val exceededLimits = commands.filter {
            it.name.isEmpty()
                    || it.description.isEmpty()
                    || it.name.length > 32
                    || it.description.length > 100
        }

        if (exceededLimits.isNotEmpty()) {
            logger.error { "One or more commands have invalid name / description: ${exceededLimits.joinToString(", ") { it.path }}" }
            throw IllegalArgumentException("Invalid slash commands parameters")
        }

        // Check for duplicated slash command paths
        val duplicatePaths = commands.groupingBy { it.path }.eachCount().filter { it.value > 1 }
        if (duplicatePaths.isNotEmpty()) {
            logger.error { "Found duplicate command paths: ${duplicatePaths.toList().joinToString(", ") { "${it.second} duplicates ${it.first}" }}" }
            throw IllegalArgumentException("Duplicate slash command names")
        }

        commandsMap = commands.associateBy({ it.path }, { it })

        logger.info { "Found and initialized ${commands.size} slash commands" }
    }

    suspend fun updateOnDiscord() {
        Bot.jda.updateCommands().addCommands(
            // First find all top-level commands
            commands.filter { it.parentSlashCommand == null }.map { slashCommand ->
                val slashCommandData = Commands.slash(slashCommand.name, slashCommand.description)
                if (slashCommand.options.isNotEmpty())
                    slashCommandData.addOptions(slashCommand.options)
                else {
                    // Then add sub commands to them
                    slashCommandData.addSubcommands(
                        commands.filter { it.parentSlashCommand != null && it.parentSlashCommand::class == slashCommand::class }.map { subCommand ->
                            SubcommandData(
                                subCommand.name,
                                subCommand.description
                            ).addOptions(subCommand.options)
                        }
                    )
                }
            }
        ).await()

        logger.info { "Published slash commands to Discord" }
    }

    fun get(path: String) = commandsMap[path]
}
