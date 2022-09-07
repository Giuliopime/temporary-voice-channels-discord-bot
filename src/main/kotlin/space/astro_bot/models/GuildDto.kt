package space.astro_bot.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuildDto(
    @SerialName("_id")
    val id: String,
    val generators: List<GeneratorDto> = emptyList()
)

@Serializable
data class GeneratorDto(
    val id: String,
)
