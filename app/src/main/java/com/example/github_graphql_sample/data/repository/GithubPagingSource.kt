package com.example.github_graphql_sample.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.github_graphql_sample.GithubRepositoriesQuery

private val GITHUB_STARTING_CURSOR = null

class GithubPagingSource(
    private val apolloClient: ApolloClient
) : PagingSource<String, GithubRepositoriesQuery.AsRepository>() {

    override fun getRefreshKey(state: PagingState<String, GithubRepositoriesQuery.AsRepository>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.nextKey ?: state.closestPageToPosition(
                anchorPosition
            )?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>):
            LoadResult<String, GithubRepositoriesQuery.AsRepository> {
        val endCursor = params.key ?: GITHUB_STARTING_CURSOR
        return try {
            val response =
                apolloClient.query(
                    GithubRepositoriesQuery(Input.fromNullable(endCursor))
                ).await()

            if(response.hasErrors()) {
                return LoadResult.Error(Exception())
            }

            val pageInfo = response.data?.search?.pageInfo
            val data =
                response.data?.search?.edges?.mapNotNull { edge -> edge?.node?.asRepository }
            val nextKey = if (pageInfo!!.hasNextPage) {
                pageInfo.endCursor
            } else {
                endCursor + (params.loadSize / 50)
            }

            LoadResult.Page(
                data = data!!,
                prevKey = if (endCursor == GITHUB_STARTING_CURSOR) null else pageInfo.startCursor,
                nextKey = nextKey
            )

        } catch (exception: ApolloException) {
            return LoadResult.Error(exception)
        }
    }
}