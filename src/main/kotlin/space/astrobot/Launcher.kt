package space.astrobot

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import mu.KotlinLogging
import org.slf4j.LoggerFactory
import space.astrobot.db.MongoClient
import space.astrobot.redis.RedisClient
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

suspend fun main() {
    // Disables debug logs for the mongodb driver library
    val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    loggerContext.getLogger("org.mongodb.driver").level = Level.WARN

    try {
        // Initializes the environment by reading from the .env file
        Env
        // Connects to the database
        MongoClient.connect()
        // Connects to Redis cache
        RedisClient.connect()
        // Connects to Discord and runs the bot
        Bot.start()
    } catch (e: Exception) {
        logger.error(e) { "Shutting down" }
        exitProcess(1)
    }
}
