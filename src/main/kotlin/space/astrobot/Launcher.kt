package space.astrobot

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import mu.KotlinLogging
import org.slf4j.LoggerFactory
import space.astrobot.db.MongoClient
import space.astrobot.exceptions.DbException
import kotlin.system.exitProcess

suspend fun main() {
    val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
    loggerContext.getLogger("org.mongodb.driver").level = Level.WARN

    try {
        Env
        MongoClient.connect()
        Bot.start()
    } catch (e: Exception) {
        exitProcess(1)
    }
}
