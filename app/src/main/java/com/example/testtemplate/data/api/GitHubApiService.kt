package com.example.testtemplate.data.api

import com.example.testtemplate.data.model.GitHubUser
import com.example.testtemplate.data.model.Repository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): Response<SearchResponse>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<GitHubUser>

    @GET("users/{username}/repos")
    suspend fun getUserRepositories(
        @Path("username") username: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): Response<List<Repository>>

    @GET("users/{username}/repos")
        suspend fun getUserRepositories( @Path("username") username: String,): Response<List<Repository>>
    }


data class SearchResponse(
    val total_count: Int,
    val items: List<GitHubUser>
) 