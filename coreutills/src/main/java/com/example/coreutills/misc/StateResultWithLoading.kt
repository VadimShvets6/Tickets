package com.example.coreutills.misc

data class StateResultWithLoading<SR>(
    val stateResult: SR,
    val loadingPairs: Map<String, Boolean>
)

data class LoadingStateMapWrapper(
    val map: Map<String, Boolean>
)