package space.astrobot.models

import kotlinx.serialization.Serializable

@Serializable
data class TempVCDto(
    val id: String,
    var ownerId: String
)
