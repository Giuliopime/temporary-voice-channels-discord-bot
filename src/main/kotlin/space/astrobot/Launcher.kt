package space.astrobot

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import mu.KotlinLogging
import org.slf4j.LoggerFactory
import space.astrobot.db.MongoClient
import space.astrobot.exceptions.DbException
import kotlin.system.exitProcess

suspend fun main() {
    // Disables debug logs for the mongodb driver library
    val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    loggerContext.getLogger("org.mongodb.driver").level = Level.WARN

    try {
        // Initializes the environment by reading from the .env file
        Env
        // Connects to the database
        MongoClient.connect()
        // Connects to Discord and runs the bot
        Bot.start()
    } catch (e: Exception) {
        // Exceptions are already logged where they happen
        exitProcess(1)
    }
}
