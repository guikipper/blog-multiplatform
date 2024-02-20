package com.example.blogmultiplatform.models
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

expect sealed class ApiListResponse {
    object Idle

    class Success

    class Error
}

