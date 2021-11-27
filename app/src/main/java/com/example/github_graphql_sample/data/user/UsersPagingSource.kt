package com.example.github_graphql_sample.data.user

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.github_graphql_sample.GithubUsersQuery

private val GITHUB_USERS_STARTING_CURSOR = null

class UsersPagingSource(
    private val apolloClient: ApolloClient
): PagingSource<String, GithubUsersQuery.AsUser>() {
    override fun getRefreshKey(state: PagingState<String, GithubUsersQuery.AsUser>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.nextKey ?: state.closestPageToPosition(
                anchorPosition
            )?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, GithubUsersQuery.AsUser> {
        val endCursor = params.key ?: GITHUB_USERS_STARTING_CURSOR
        return try {

            val response =
                apolloClient.query(
                    GithubUsersQuery(Input.fromNullable(endCursor))
                ).await()

            if(response.hasErrors()) {
                return LoadResult.Error(Exception())
            }

            val pageInfo = response.data?.search?.pageInfo
            val data =
                response.data?.search?.edges?.mapNotNull { edge -> edge?.node?.asUser }
            val nextKey = if (pageInfo!!.hasNextPage) {
                pageInfo.endCursor
            } else {
                endCursor + (params.loadSize / 50)
            }

            LoadResult.Page(
                data = data!!,
                prevKey = if (endCursor == GITHUB_USERS_STARTING_CURSOR) null else pageInfo.startCursor,
                nextKey = nextKey
            )

        } catch (exception: ApolloException) {
            return LoadResult.Error(exception)
        }
    }
}