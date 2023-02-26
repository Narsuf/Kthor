package org.n27.models.wrappers

import org.n27.models.Result
import kotlinx.serialization.Serializable

@Serializable
data class ResultList(val results: List<Result>)