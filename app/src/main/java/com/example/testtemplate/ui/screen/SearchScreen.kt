package com.example.testtemplate.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.testtemplate.data.model.GitHubUser
import com.example.testtemplate.ui.components.EmptyStateMessage
import com.example.testtemplate.ui.components.ErrorMessage
import com.example.testtemplate.ui.components.LoadingIndicator
import com.example.testtemplate.ui.viewmodel.SearchViewModel
import com.example.testtemplate.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onUserClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                if (it.length >= 3) {
                    viewModel.searchUsers(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search GitHub users...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is UiState.Initial -> {
                EmptyStateMessage("Enter a username to search")
            }
            is UiState.Loading -> {
                LoadingIndicator()
            }
            is UiState.Error -> {
                ErrorMessage((uiState as UiState.Error).message)
            }
            is UiState.Success -> {
                val users = (uiState as UiState.Success<List<GitHubUser>>).data
                if (users.isEmpty()) {
                    EmptyStateMessage("No users found")
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(users) { user ->
                            UserItem(user = user, onClick = { onUserClick(user.login) })
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserItem(
    user: GitHubUser,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.avatar_url,
                contentDescription = "User avatar",
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = user.login,
                    style = MaterialTheme.typography.titleMedium
                )
                if (!user.bio.isNullOrEmpty()) {
                    Text(
                        text = user.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
} 