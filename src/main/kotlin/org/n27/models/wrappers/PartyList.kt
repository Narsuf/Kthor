package org.n27.models.wrappers

import kotlinx.serialization.Serializable
import org.n27.models.Party

@Serializable
data class PartyList(val parties: List<Party>)