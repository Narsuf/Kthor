package com.jorgedguezm.models.wrappers

import com.jorgedguezm.models.Result
import kotlinx.serialization.Serializable

@Serializable
data class ResultList(val results: List<Result>)