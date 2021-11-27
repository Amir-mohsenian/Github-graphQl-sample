package com.example.github_graphql_sample.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo.ApolloClient
import com.example.github_graphql_sample.GithubRepositoriesQuery
import com.example.github_graphql_sample.GithubUsersQuery
import com.example.github_graphql_sample.data.repository.GithubPagingSource
import com.example.github_graphql_sample.data.user.UsersPagingSource
import kotlinx.coroutines.flow.Flow

class GithubRepository(private val apolloClient: ApolloClient) {

    fun getRepositories():
            Flow<PagingData<GithubRepositoriesQuery.AsRepository>> {
        return Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            pagingSourceFactory = { GithubPagingSource(apolloClient) }
        ).flow
    }

    fun getUsers():
            Flow<PagingData<GithubUsersQuery.AsUser>> {
        return Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            pagingSourceFactory = { UsersPagingSource(apolloClient) }
        ).flow
    }
}
