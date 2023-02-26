package org.n27.models.wrappers

import kotlinx.serialization.Serializable
import org.n27.models.Election

@Serializable
data class ElectionList(val elections: List<Election>)