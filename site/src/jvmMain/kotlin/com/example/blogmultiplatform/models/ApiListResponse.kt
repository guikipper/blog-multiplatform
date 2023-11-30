package com.example.blogmultiplatform.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable()
actual sealed class ApiListResponse {
    @Serializable
    @SerialName("idle")
    actual object Idle: ApiListResponse()
    @Serializable
    @SerialName("success")
    actual class Success(val data: List<PostWithoutDetails>): ApiListResponse() {
        companion object
    }

    @Serializable
    @SerialName("error")
    actual class Error(val message: String): ApiListResponse()
}

