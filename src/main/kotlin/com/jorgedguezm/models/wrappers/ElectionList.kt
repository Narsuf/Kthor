package com.jorgedguezm.models.wrappers

import com.jorgedguezm.models.Election
import kotlinx.serialization.Serializable

@Serializable
data class ElectionList(val elections: List<Election>)