package com.example.blogmultiplatform.data

import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.User

interface MongoRepository {
    suspend fun checkUserExistence(user: User): User? //aqui retornamos um user do tipo User
    suspend fun checkUserId(id: String) : Boolean
    suspend fun createPost(post: Post) : Boolean
    suspend fun readMyPosts(skip: Int, author: String) : List<PostWithoutDetails>
}