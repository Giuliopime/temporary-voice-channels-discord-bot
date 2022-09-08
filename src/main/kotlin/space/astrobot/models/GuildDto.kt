package space.astrobot.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Contains all the guild settings needed by Astro to work
 */
@Serializable
data class GuildDto(
    @SerialName("_id")
    val id: String,
    val generators: MutableList<GeneratorDto> = mutableListOf()
)

@Serializable
data class GeneratorDto(
    val id: String,
)
