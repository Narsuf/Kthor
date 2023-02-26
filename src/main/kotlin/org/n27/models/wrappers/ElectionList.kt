package org.n27.models.wrappers

import org.n27.models.Election
import kotlinx.serialization.Serializable

@Serializable
data class ElectionList(val elections: List<Election>)