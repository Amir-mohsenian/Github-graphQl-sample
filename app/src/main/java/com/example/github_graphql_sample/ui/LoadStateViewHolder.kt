package com.example.github_graphql_sample.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.github_graphql_sample.R

class LoadStateViewHolder(
    private val view: View,
    retry: () -> Unit): RecyclerView.ViewHolder(view) {

    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val retryButton = view.findViewById<Button>(R.id.retry_button)
    private val errorMessage = view.findViewById<TextView>(R.id.error_message)

    init {
        retryButton.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            errorMessage.text = loadState.error.localizedMessage
        }
        progressBar.isVisible = loadState is LoadState.Loading
        retryButton.isVisible = loadState is LoadState.Error
        errorMessage.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_footer_view_item, parent, false)
            return LoadStateViewHolder(view, retry)
        }
    }
}