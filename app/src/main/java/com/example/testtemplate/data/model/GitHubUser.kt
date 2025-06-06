package com.example.testtemplate.data.model

data class GitHubUser(
    val id: Int,
    val login: String,
    val avatar_url: String,
    val bio: String?,
    val followers: Int,
    val public_repos: Int,
    val name: String?,
    val company: String?,
    val location: String?,
    val blog: String?,
    val repos_url: String
) 