package com.example.testtemplate.data.repository

import android.util.Log
import com.example.testtemplate.data.api.GitHubApiService
import com.example.testtemplate.data.api.SearchResponse
import com.example.testtemplate.data.model.GitHubUser
import com.example.testtemplate.data.model.Repository
import retrofit2.Response

class GitHubRepository(private val apiService: GitHubApiService) {
    
    suspend fun searchUsers(query: String, page: Int = 1): Response<SearchResponse> {
        return apiService.searchUsers(query, page)
    }

    suspend fun getUser(username: String): Response<GitHubUser> {
        return apiService.getUser(username)
    }

    suspend fun getUserRepositories(username: String, page: Int = 1, repos: String): Response<List<Repository>> {
        Log.d("Gagan", repos)
        return apiService.getUserRepositories(username, page) //apiService.getUserRepositories(repos)
    }
} 