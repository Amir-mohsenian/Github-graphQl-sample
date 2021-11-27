package com.example.github_graphql_sample.ui.repositories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.github_graphql_sample.GithubRepositoriesQuery
import com.example.github_graphql_sample.R

class RepositoryViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

    private val title: TextView = view.findViewById(R.id.text_repo_title)
    private val description: TextView = view.findViewById(R.id.text_repo_description)
    private val lockImage: ImageView = view.findViewById(R.id.image_lock)
    private val createdAt: TextView = view.findViewById(R.id.text_repo_created_at)
    private val forkCount: TextView = view.findViewById(R.id.text_fork_count)

    fun bind(repository: GithubRepositoriesQuery.AsRepository?) {
        if (repository != null) {
            title.text = repository.name
            description.text = repository.description
            if (repository.isPrivate) {
                lockImage.setImageResource(R.drawable.ic_private)
            } else {
                lockImage.setImageResource(R.drawable.ic_public)

            }
            forkCount.text = repository.forkCount.toString()
            val strings = repository.createdAt.toString().split("-")
            createdAt.text =
                view.context.getString(R.string.date_format,
                Integer.parseInt(strings[0]),
                Integer.parseInt(strings[1]))
        }
    }

    companion object {
        fun create(parent: ViewGroup): RepositoryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.repository_view_item, parent, false)
            return RepositoryViewHolder(view)
        }
    }
}