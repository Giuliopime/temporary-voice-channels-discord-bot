package space.astrobot.redis

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import space.astrobot.models.TempVCDto

object TempVoiceChannelsRI {
    private val client = RedisClient.getClient()
    private const val hashName = "temporary_voice_channels"

    fun create(guildId: String, vc: TempVCDto) {
        val vcs = getAllFromGuild(guildId)
        vcs.add(vc)
        client.hset(hashName, guildId, Json.encodeToString(vcs))
    }

    fun getAllFromGuild(guildId: String): MutableList<TempVCDto> {
        val jsonList = client.hget(hashName, guildId) ?: "[]"
        return Json.decodeFromString(jsonList)
    }

    @Throws(NoSuchElementException::class)
    fun updateAllForGuild(guildId: String, vcs: MutableList<TempVCDto>) {
        client.hset(hashName, guildId, Json.encodeToString(vcs))
    }
}
