package org.n27.models.wrappers

import org.n27.models.Party
import kotlinx.serialization.Serializable

@Serializable
data class PartyList(val parties: List<Party>)