package com.example.github_graphql_sample.ui.repositories

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.github_graphql_sample.GithubRepositoriesQuery

class RepositoryAdapter :
    PagingDataAdapter<GithubRepositoriesQuery.AsRepository, RepositoryViewHolder>(
        REPOSITORY_COMPARATOR) {

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repositoryItem = getItem(position)
        if (repositoryItem != null) {
            holder.bind(repositoryItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder.create(parent)
    }

    companion object {
        private val REPOSITORY_COMPARATOR =
            object : DiffUtil.ItemCallback<GithubRepositoriesQuery.AsRepository>() {
                override fun areItemsTheSame(
                    oldItem: GithubRepositoriesQuery.AsRepository,
                    newItem: GithubRepositoriesQuery.AsRepository
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: GithubRepositoriesQuery.AsRepository,
                    newItem: GithubRepositoriesQuery.AsRepository
                ): Boolean =
                    oldItem == newItem
            }
    }
}