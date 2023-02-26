package org.n27.models.wrappers

import kotlinx.serialization.Serializable
import org.n27.models.Result

@Serializable
data class ResultList(val results: List<Result>)