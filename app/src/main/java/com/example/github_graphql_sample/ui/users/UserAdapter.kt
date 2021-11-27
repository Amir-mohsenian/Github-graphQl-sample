package com.example.github_graphql_sample.ui.users

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.github_graphql_sample.GithubUsersQuery

class UserAdapter: PagingDataAdapter<GithubUsersQuery.AsUser, UserViewHolder>(USER_COMPARATOR) {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userItem = getItem(position)
        if (userItem != null) {
            holder.bind(userItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent)
    }

    companion object {
        private val USER_COMPARATOR =
            object : DiffUtil.ItemCallback<GithubUsersQuery.AsUser>() {
                override fun areItemsTheSame(
                    oldItem: GithubUsersQuery.AsUser,
                    newItem: GithubUsersQuery.AsUser
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: GithubUsersQuery.AsUser,
                    newItem: GithubUsersQuery.AsUser
                ): Boolean =
                    oldItem == newItem
            }
    }
}