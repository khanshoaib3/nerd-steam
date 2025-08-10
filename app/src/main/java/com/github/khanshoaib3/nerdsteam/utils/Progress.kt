package com.github.khanshoaib3.nerdsteam.utils

sealed interface Progress {
    data object NOT_QUEUED: Progress
    data object LOADING: Progress
    data object LOADED: Progress
    data class FAILED(val reason: String?): Progress
}
