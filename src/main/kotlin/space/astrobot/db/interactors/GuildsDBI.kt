package space.astrobot.db.interactors

import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import mu.KotlinLogging
import org.litote.kmongo.*
import space.astrobot.db.MongoClient
import space.astrobot.exceptions.DbException
import space.astrobot.models.GuildDto

object GuildsDBI {
    private const val collectionName = "guilds"
    private val collection = MongoClient.getDb().getCollection<GuildDto>(collectionName)

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

    fun updateValue(id: String, route: String, value: Any?, operator: MongoOperator = MongoOperator.set) = updateValues(id, Pair(route, value), operator = operator)

    fun updateValues(id: String, vararg routeValuePair: Pair<String, Any?>, operator: MongoOperator = MongoOperator.set): GuildDto {
        val itemsString = routeValuePair.asList().joinToString(",") { "\"${it.first}\": ${it.second?.json}" }

        return collection.findOneAndUpdate(
            "{ _id: ${id.json} }",
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
