# GitHub User Search App

This app allows users to search a user name and see the available profiles. Then click on a profile to see further details like list of repositories.

## Features

- Search GitHub users by username
- View user profiles with:
  - Profile picture
  - Bio
  - Follower count
  - Repository count
- Browse user repositories with:
  - Repository name
  - Description
  - Star count
  - Fork count
  - Programming language

## Technical Stack

- Kotlin
- Jetpack Compose for UI
- MVVM Architecture
- Retrofit for API calls
- Coroutines for async operations
- Material Design 3
- GitHub REST API


## Architecture

- **Data Layer**
  - Repository pattern
  - API service using Retrofit
  - Data models

- **UI Layer**
  - Jetpack Compose screens
  - ViewModels for state management
  - Material Design components

## Screens

1. **Search Screen**
   - Search input field
   - List of search results
   - User cards with basic info

2. **Profile Screen**
   - User profile details
   - Repository list
   - Back navigation
