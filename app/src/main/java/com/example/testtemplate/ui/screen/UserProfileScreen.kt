package com.example.testtemplate.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.testtemplate.data.model.GitHubUser
import com.example.testtemplate.data.model.Repository
import com.example.testtemplate.ui.components.EmptyStateMessage
import com.example.testtemplate.ui.components.ErrorMessage
import com.example.testtemplate.ui.components.LoadingIndicator
import com.example.testtemplate.ui.viewmodel.UserProfileViewModel
import com.example.testtemplate.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel,
    onBackClick: () -> Unit,
    selectedUser : String
) {
    val uiState by viewModel.uiState.collectAsState()
    val repositories by viewModel.repositories.collectAsState()

   LaunchedEffect(selectedUser)  {
        viewModel.loadUserProfile(selectedUser)
   }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Profile") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is UiState.Initial -> {
                EmptyStateMessage("Loading user profile...")
            }
            is UiState.Loading -> {
                LoadingIndicator()
            }
            is UiState.Error -> {
                ErrorMessage((uiState as UiState.Error).message)
            }
            is UiState.Success -> {
                val user = (uiState as UiState.Success<GitHubUser>).data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        UserProfileHeader(user = user)
                    }

                    item {
                        Text(
                            text = "Repositories",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    if (repositories.isEmpty()) {
                        item {
                            EmptyStateMessage("No repositories found")
                        }
                    } else {
                        items(repositories) { repo ->
                            RepositoryItem(repository = repo)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileHeader(user: GitHubUser) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = user.avatar_url,
                contentDescription = "User avatar",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user.login,
                style = MaterialTheme.typography.headlineMedium
            )

            if (!user.name.isNullOrEmpty()) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!user.bio.isNullOrEmpty()) {
                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Followers", user.followers.toString())
                StatItem("Repositories", user.public_repos.toString())
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryItem(repository: Repository) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = repository.name,
                style = MaterialTheme.typography.titleMedium
            )

            if (!repository.description.isNullOrEmpty()) {
                Text(
                    text = repository.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Stars",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = repository.stargazers_count.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "${repository.forks_count} forks",
                    style = MaterialTheme.typography.bodySmall
                )

                if (!repository.language.isNullOrEmpty()) {
                    Text(
                        text = repository.language,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}