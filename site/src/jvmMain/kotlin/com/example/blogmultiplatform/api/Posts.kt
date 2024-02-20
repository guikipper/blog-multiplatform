package com.example.blogmultiplatform.api

import com.example.blogmultiplatform.data.MongoDB
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.Post
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.litote.kmongo.id.ObjectIdGenerator

@Api(routeOverride = "createpost")
suspend fun createPost(context: ApiContext) {
    try {
        val post = context.req.body?.decodeToString()?.let { Json.decodeFromString<Post>(it) }
        val newPost = post?.copy(id = ObjectIdGenerator.newObjectId<String>().id.toHexString())
        context.res.setBodyText(
            newPost?.let {
                context.data.getValue<MongoDB>().createPost(it).toString()
            } ?: false.toString()
        )
    } catch (e: Exception) {
        context.logger.info("API EXCEPTION: $e")
        context.res.setBodyText(Json.encodeToString(e.message))
    }
}

@Api(routeOverride = "readmyposts")
suspend fun readMyPosts(context: ApiContext) {
    try {
        val skip = context.req.params["skip"]?.toInt() ?: 0 //se vier null o valor padrão será 0
        val author = context.req.params["author"] ?: ""
        val myPosts = context.data.getValue<MongoDB>().readMyPosts(skip = skip, author = author)
        context.res.setBodyText(
            Json.encodeToString(ApiListResponse.Success(data = myPosts).toString())
        )
    }
    catch (e: Exception) {
        context.logger.info("APIEXPECTION $e")
        context.res.setBodyText(
            Json.encodeToString(ApiListResponse.Error(message = e.message.toString()))
        )
    }
}

@Api(routeOverride = "testposts")
fun testPosts(context: ApiContext) {
    try {
        val id = context.req.params["id"] ?: ""
        println("The ID at the Endpoint: " + id)
    }
    catch (e: Exception) {
        context.logger.info("APIEXPECTION $e")
        context.res.setBodyText(
            Json.encodeToString(ApiListResponse.Error(message = e.message.toString()))
        )
    }
}


