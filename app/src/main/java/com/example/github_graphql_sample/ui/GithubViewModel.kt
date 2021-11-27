package com.example.github_graphql_sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.github_graphql_sample.GithubRepositoriesQuery
import com.example.github_graphql_sample.GithubUsersQuery
import com.example.github_graphql_sample.data.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GithubViewModel @Inject constructor(private val repository: GithubRepository): ViewModel()  {

    fun getRepositoriesData(): Flow<PagingData<GithubRepositoriesQuery.AsRepository>> {
        return repository.getRepositories().cachedIn(viewModelScope)
    }

    fun getUserData(): Flow<PagingData<GithubUsersQuery.AsUser>> {
        return repository.getUsers().cachedIn(viewModelScope)
    }
}