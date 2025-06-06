package com.example.testtemplate.data.model

data class Repository(
    val id: Int,
    val name: String,
    val description: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val language: String?,
    val html_url: String,
    val updated_at: String
) 