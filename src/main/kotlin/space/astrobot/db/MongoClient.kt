package space.astrobot.db

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import mu.KotlinLogging
import org.litote.kmongo.KMongo
import space.astrobot.Env
import space.astrobot.exceptions.DbException

private val logger = KotlinLogging.logger {  }

object MongoClient {
    private lateinit var client: MongoClient
    private lateinit var db: MongoDatabase

    fun connect() {
        try {
            client = KMongo.createClient(Env.MongoDb.connection_string)
            db = client.getDatabase(Env.MongoDb.db_name)
        } catch (e: Exception) {
            logger.error(e) { "Something went wrong when connecting to MongoDB, check exception message" }
            throw DbException("Something went wrong when connecting to MongoDB", e)
        }
    }

    fun getDb() = db
}
