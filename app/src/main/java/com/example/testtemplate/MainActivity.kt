package com.example.testtemplate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testtemplate.data.repository.GitHubRepository
import com.example.testtemplate.di.NetworkModule
import com.example.testtemplate.ui.screen.SearchScreen
import com.example.testtemplate.ui.screen.UserProfileScreen
import com.example.testtemplate.ui.theme.TestTemplateTheme
import com.example.testtemplate.ui.viewmodel.SearchViewModel
import com.example.testtemplate.ui.viewmodel.UserProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTemplateTheme {
                GitHubApp()
            }
        }
    }
}

@Composable
fun GitHubApp() {
    val repository = remember { GitHubRepository(NetworkModule.gitHubApiService) }
    var selectedUsername by remember { mutableStateOf<String?>(null) }

    if (selectedUsername == null) {
        val searchViewModel: SearchViewModel = viewModel(
            factory = SearchViewModelFactory(repository)
        )
        SearchScreen(
            viewModel = searchViewModel,
            onUserClick = { username -> selectedUsername = username }
        )
    } else {
        val profileViewModel: UserProfileViewModel = viewModel(
            factory = UserProfileViewModelFactory(repository)
        )


        UserProfileScreen(
            viewModel = profileViewModel,
            onBackClick = { selectedUsername = null},
            selectedUser = selectedUsername ?: ""
        )
    }
}

class SearchViewModelFactory(private val repository: GitHubRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class UserProfileViewModelFactory(private val repository: GitHubRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}