package com.mhmd.newsapp.framework.presentation.newsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mhmd.newsapp.R
import com.mhmd.newsapp.framework.presentation.components.LoadingNewsListShimmer
import com.mhmd.newsapp.framework.presentation.components.NewsCard
import com.mhmd.newsapp.framework.presentation.components.NothingHere
import com.mhmd.newsapp.framework.presentation.components.SearchAppBar
import com.mhmd.newsapp.framework.presentation.favoriteList.FavoriteListViewModel
import com.mhmd.newsapp.framework.presentation.newsList.NewsListEvent.NextPageEvent
import com.mhmd.newsapp.framework.presentation.theme.NewsAppTheme
import com.mhmd.newsapp.framework.presentation.utils.ConnectivityManager
import com.mhmd.newsapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NewsListFragment : Fragment() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private lateinit var viewModel: NewsListViewModel
    private lateinit var favoriteViewModel: FavoriteListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(NewsListViewModel::class.java)
        favoriteViewModel =
            ViewModelProvider(requireActivity()).get(FavoriteListViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {

                val news = viewModel.news.value

                val query = viewModel.query.value

                val selectedCategory = viewModel.selectedCategory.value

                val refreshNews = viewModel.refreshNews.value

                val categoryScrollPosition = viewModel.categoryScrollPosition

                val loading = viewModel.loading.value

                val page = viewModel.page.value

                val dialogQueue = viewModel.dialogQueue

                NewsAppTheme(
                    isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                    dialogQueue = dialogQueue.queue.value,
                ) {
                    Box {
                        Column(
                            modifier = Modifier.background(
                                MaterialTheme.colors.background
                            )
                        ) {
                            SearchAppBar(
                                query = query,
                                onQueryChanged = viewModel::onQueryChanged,
                                onExecuteSearch = { viewModel.onTriggerEvent(NewsListEvent.SearchEvent) },
                                categories = getAllNewsCategories(),
                                selectedCategory = selectedCategory,
                                onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                                scrollPosition = categoryScrollPosition,
                                onChangeScrollPosition = viewModel::onChangeCategoryScrollPosition,
                                onFavoriteList = {
                                    findNavController().navigate(
                                        R.id.action_newsListFragment_to_favoriteListFragment
                                    )
                                },
                                isDark = false
                            )
                            if (loading && news.isEmpty()) {
                                LoadingNewsListShimmer(imageHeight = 100.dp)
                            } else if (news.isEmpty()) {
                                NothingHere()
                            } else {
                                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                                    itemsIndexed(
                                        items = news
                                    ) { index, newsItem ->
                                        viewModel.onChangeNewsScrollPosition(index)
                                        if ((index + 1) >= (page * Constants.NEWS_PAGINATION_PAGE_SIZE) && !loading) {
                                            viewModel.onTriggerEvent(NextPageEvent)
                                        }
                                        NewsCard(
                                            news = newsItem,
                                            index = index,
                                            isFavorite = newsItem.isFavorite,
                                            onClick = {
                                                viewModel.setSelectedNewsItem(newsItem)
                                                favoriteViewModel.setSelectedNewsItem(null)
                                                findNavController().navigate(
                                                    R.id.action_newsListFragment_to_newsDetailsFragment,
                                                )
                                            },
                                            onFavoriteClick = viewModel::onFavorite
                                        )
                                    }
                                }
                            }
                        }

                        if (loading && news.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colors.surface)
                                    .align(alignment = Alignment.BottomCenter)
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colors.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
