package com.jorgedguezm.models.wrappers

import com.jorgedguezm.models.Party
import kotlinx.serialization.Serializable

@Serializable
data class PartyList(val parties: List<Party>)