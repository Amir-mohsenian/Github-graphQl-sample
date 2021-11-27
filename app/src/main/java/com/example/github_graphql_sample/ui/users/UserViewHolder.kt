package com.example.github_graphql_sample.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.loadAny
import coil.transform.CircleCropTransformation
import com.example.github_graphql_sample.GithubUsersQuery
import com.example.github_graphql_sample.R

class UserViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

    private val name: TextView = view.findViewById(R.id.text_user_name)
    private val email: TextView = view.findViewById(R.id.text_user_email)
    private val bio: TextView = view.findViewById(R.id.text_user_bio)
    private val createdAt: TextView = view.findViewById(R.id.text_user_created_at)
    private val avatar: ImageView = view.findViewById(R.id.image_avatar)

    fun bind(user: GithubUsersQuery.AsUser) {
        name.text = user.name
        email.text = user.email
        if (user.bio == null || user.bio == "") {
            bio.visibility = View.GONE
        } else {
            bio.visibility = View.VISIBLE
            bio.text = user.bio
        }

        val strings = user.createdAt.toString().split("-")
        createdAt.text =
            view.context.getString(R.string.date_format,
                Integer.parseInt(strings[0]),
                Integer.parseInt(strings[1]))
        avatar.loadAny(user.avatarUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
            transformations(CircleCropTransformation())
        }
    }

    companion object {
        fun create(parent: ViewGroup): UserViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_view_item, parent, false)
            return UserViewHolder(view)
        }
    }

}