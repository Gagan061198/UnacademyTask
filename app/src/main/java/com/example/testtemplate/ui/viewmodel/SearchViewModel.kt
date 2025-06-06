package com.example.testtemplate.ui.viewmodel

import com.example.testtemplate.data.model.GitHubUser
import com.example.testtemplate.data.repository.GitHubRepository

class SearchViewModel(
    private val repository: GitHubRepository
) : BaseViewModel<List<GitHubUser>>() {

    private var currentPage = 1
    private var currentQuery = ""
    private var hasMorePages = true

    fun searchUsers(query: String, isNewSearch: Boolean = true) {
        if (isNewSearch) {
            currentPage = 1
            currentQuery = query
            hasMorePages = true
        }

        if (!hasMorePages) return

        launchWithLoading {
            val response = repository.searchUsers(currentQuery, currentPage)
            if (response.isSuccessful) {
                response.body()?.let { searchResponse ->
                    val users = searchResponse.items
                    hasMorePages = users.size == 50 //Assuming per_page is 50
                    currentPage++
                    setSuccess(users)
                } ?: setError("No results found")
            } else {
                setError("Failed to search users: ${response.message()}")
            }
        }
    }


} 