package com.example.github_graphql_sample.ui.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.github_graphql_sample.R
import com.example.github_graphql_sample.ui.GithubLoadStateAdapter
import com.example.github_graphql_sample.ui.GithubViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inflater = inflater.inflate(R.layout.fragment_repositories, container, false)

        val viewModel: GithubViewModel by hiltNavGraphViewModels(R.id.nav_graph)

        val pagingAdapter=  RepositoryAdapter()
        val recyclerView = inflater.findViewById<RecyclerView>(R.id.recyclerview)
        val progressBar = inflater.findViewById<ProgressBar>(R.id.progress_bar)
        val retryButton = inflater.findViewById<Button>(R.id.retry_button)

        pagingAdapter.addLoadStateListener { loadState ->
            progressBar.isVisible = loadState.refresh is LoadState.Loading
            retryButton.isVisible = loadState.refresh is LoadState.Error
        }

        retryButton.setOnClickListener {
            pagingAdapter.retry()
        }

        recyclerView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = GithubLoadStateAdapter { pagingAdapter.retry() },
            footer = GithubLoadStateAdapter { pagingAdapter.retry() }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getRepositoriesData().collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        return inflater
    }
}