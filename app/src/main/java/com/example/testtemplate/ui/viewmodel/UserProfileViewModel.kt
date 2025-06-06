package com.example.testtemplate.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.testtemplate.data.model.GitHubUser
import com.example.testtemplate.data.model.Repository
import com.example.testtemplate.data.repository.GitHubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class UserProfileViewModel(
    private val repository: GitHubRepository
) : BaseViewModel<GitHubUser>() {

    private val _repositories = MutableStateFlow<List<Repository>>(emptyList())
    val repositories: StateFlow<List<Repository>> = _repositories.asStateFlow()

    private var currentPage = 1
    private var hasMoreRepos = true

    fun loadUserProfile(username: String) {
        launchWithLoading {
            val response = repository.getUser(username)
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    setSuccess(user)
                    Log.d("Gagan", "username--"+username +" &&  "+ response.body()!!)
                    loadUserRepositories(response.body()!!.login, response.body()!!.repos_url.substringAfter("/users/"))
                } ?: setError("User not found")
            } else {
                setError("Failed to load user profile: ${response.message()}")
            }
        }
    }

    private fun loadUserRepositories(username: String, repos: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUserRepositories(username, currentPage, repos)
                Log.d("Gagan-repo", " ${response.body()} ${response.message()}")
                if (response.isSuccessful) {
                    Log.d("Gagan-repo", "isZSuccessful ${response.body()} $response")

                    response.body()?.let { repo ->
                        hasMoreRepos = repo.size == 30 // Assuming per_page is 30
                        currentPage++
                        _repositories.value += repo
                    }
                } else{
                    Log.d("Gagan-repo", "isNotSuccessful - repsonse - $response ${response.message()}")
                    setError("Failed to load user profile: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d("Gagan-repo", "exception - $e")
            }

        }
    }
} 