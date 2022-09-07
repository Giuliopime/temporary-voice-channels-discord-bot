package space.astro_bot.db

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import mu.KotlinLogging
import org.litote.kmongo.KMongo
import space.astro_bot.Env

private val logger = KotlinLogging.logger {  }

object MongoClient {
    private lateinit var client: MongoClient
    private lateinit var db: MongoDatabase

    fun connect(): Boolean {
        return try {
            client = KMongo.createClient(Env.MongoDb.mongo_connection_string)
            db = client.getDatabase(Env.MongoDb.db_name)
            true
        } catch (e: Exception) {
            logger.error(e) { "Something went wrong when connecting to MongoDB, check exception message" }
            false
        }
    }

    fun getDb() = db
}
