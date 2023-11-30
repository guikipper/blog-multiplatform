package com.example.blogmultiplatform.util

import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.RandomJoke
import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.models.UserWithoutPassword
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.compose.http.http
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.outline
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

suspend fun checkUserExistence(user: User): UserWithoutPassword? {
    return try {
        val result = window.api.tryPost(
            apiPath = "usercheck",
            body = Json.encodeToString(user).encodeToByteArray()
        )
        result?.decodeToString()?.let { Json.decodeFromString<UserWithoutPassword>(it) }
    } catch (e: Exception) {
        println(e.message)
        null
    }
}

suspend fun checkUserId(id: String): Boolean {
    return try {
        println("Dentro de checkUserID em API FUNCTIONS, segue o ID: "+id)
        val result = window.api.tryPost(
            apiPath = "checkuserid",
            body = Json.encodeToString(id).encodeToByteArray()
        )
        result?.decodeToString()?.let { Json.decodeFromString<Boolean>(it) } ?: false //se algo atrás for null, retorne false

    } catch (e: Exception) {
        println("Erro na função checkUserId: $e")
        false
    }
}

suspend fun fetchRandomJoke(onComplete: (RandomJoke) -> Unit) {
    val date = localStorage["date"]
    if (date != null) {
        val difference = Date.now() - date.toDouble()
        val dayHasPassed = difference >= 82800000
        if (dayHasPassed) {
                try {
                    val result = window.http.get(Constants.HUMOR_API_URL).decodeToString()
                    onComplete(Json.decodeFromString(result))
                    localStorage["date"] = Date.now().toString()
                    localStorage["joke"] = result
                } catch (e: Exception) {
                    onComplete(RandomJoke(id = -1, joke = e.message.toString()))
                    println(e.message)
                }
        } else {
            try {

                localStorage["joke"]?.let { Json.decodeFromString<RandomJoke>(it) }
                    ?.let { onComplete(it) }

            } catch (e: Exception) {
                onComplete(RandomJoke(id = -1, joke = e.message.toString()))
                println(e.message)
            }
        }
    } else {
            try {
                val result = window.http.get(Constants.HUMOR_API_URL).decodeToString()
                onComplete(Json.decodeFromString(result))
                localStorage["date"] = Date.now().toString()
                localStorage["joke"] = result
            } catch (e: Exception) {
                onComplete(RandomJoke(id = -1, joke = e.message.toString()))
                println(e.message)
            }
    }
}

fun Modifier.noBorder(): Modifier {
    return this.
        border(
        width = 0.px,
        style = LineStyle.None,
        color = Colors.Transparent
    )
        .outline(
            width = 0.px,
            style = LineStyle.None,
            color = Colors.Transparent
        )
}

suspend fun createPost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "createpost",
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()
    } catch (e:Exception) {
        print(e.message.toString())
        false
    }
}

suspend fun fetchMyPosts(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readmyposts?skip=$skip&author=${localStorage["username"]}"
        )?.decodeToString()
        onSuccess(Json.decodeFromString(result.toString()))
    } catch (e: Exception) {
        onError(e)
    }
}