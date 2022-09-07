package space.astro_bot.db.managers

import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import mu.KotlinLogging
import org.litote.kmongo.*
import space.astro_bot.db.MongoClient
import space.astro_bot.exceptions.DbException
import space.astro_bot.models.GuildDto

private val logger = KotlinLogging.logger {}

object GuildsDbManager {
    val collectionName = "guilds"
    val collection = MongoClient.getDb().getCollection<GuildDto>(collectionName)

    private fun create(id: String): GuildDto {
        val document = GuildDto(id)
        collection.save(document)
        return document
    }

    fun get(id: String) = collection.findOne(GuildDto::id eq id)

    fun getOrCreate(id: String) = get(id) ?: create(id)

    fun update(guildDto: GuildDto) = collection.findOneAndReplace(
        GuildDto::id eq guildDto.id,
        guildDto,
        FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER)
    )

    fun updateValue(id: String, route: String, value: Any?, operator: MongoOperator = MongoOperator.set): GuildDto {
        return collection.findOneAndUpdate(
            "{ guildID: ${id.json} }",
            "{ ${operator}: { \"$route\": ${value?.json} }",
            FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        ) ?: throw DbException("`$collectionName` document with ID $id not found while trying to update it")
    }

    fun updateValues(id: String, routeValuePairs: List<Pair<String, Any?>>, operator: MongoOperator = MongoOperator.set): GuildDto {
        val itemsString = routeValuePairs.joinToString(",") { "\"${it.first}\": ${it.second?.json}" }

        return collection.findOneAndUpdate(
            "{ guildID: ${id.json} }",
            "{ ${operator}: { $itemsString } }",
            FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        ) ?: throw DbException("`$collectionName` document with ID $id not found while trying to update it")

    }

    fun pushValue(id: String, route: String, value: Any?): GuildDto {
        return updateValue(id, route, value, MongoOperator.push)
    }

    fun delete(id: String) {
        try {
            val deleted = collection.deleteOne(GuildDto::id eq id).deletedCount > 0
            if (!deleted)
                throw DbException("`$collectionName` document with ID $id not found while trying to delete it")
        } catch (e: Exception) {
            throw DbException("Error occurred when trying to delete `$collectionName` document with id $id", e)
        }
    }
}
