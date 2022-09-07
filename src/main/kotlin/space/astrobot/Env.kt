package space.astrobot

import io.github.cdimascio.dotenv.dotenv
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

object Env {
    private val dotenv = dotenv()
    object Discord {
        lateinit var token: String
        lateinit var activity: String
        var activity_type_key: Int = 3
        var update_slash_commands: Boolean = true
    }


    object MongoDb {
        lateinit var connection_string: String
        lateinit var db_name: String
    }

    init {
        loadEnv()
    }

    fun loadEnv() {
        Discord.token = get("discord_token")
        Discord.activity = get("discord_activity")
        Discord.activity_type_key = getInt("discord_activity_type_key")
        Discord.update_slash_commands = getBoolean("discord_update_slash_commands")

        MongoDb.connection_string = get("mongo_connection_string")
        MongoDb.db_name = get("mongo_db_name")
    }

    private fun get(path: String): String {
        val value = dotenv[path.uppercase()]

        if (value == null) {
            logger.error { "Couldn't find any $path key in .env file." }
            throw NoSuchElementException("Couldn't find any $path key in .env file")
        }

        return value
    }

    private fun getInt(path: String): Int = try {
        get(path).toInt()
    } catch (e: NumberFormatException) {
        logger.error { "$path in .env file is not a valid INTEGER value!" }
        throw NoSuchElementException("$path in .env file is not a valid INTEGER value!")
    }

    private fun getBoolean(path: String) = get(path).toBoolean()
}
