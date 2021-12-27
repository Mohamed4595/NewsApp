package com.mhmd.newsapp.framework.presentation.favoriteList

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mhmd.newsapp.R
import com.mhmd.newsapp.framework.presentation.components.AppBar
import com.mhmd.newsapp.framework.presentation.components.NewsCard
import com.mhmd.newsapp.framework.presentation.components.NothingHere
import com.mhmd.newsapp.framework.presentation.newsList.NewsListEvent
import com.mhmd.newsapp.framework.presentation.newsList.NewsListEvent.NextPageEvent
import com.mhmd.newsapp.framework.presentation.newsList.NewsListViewModel
import com.mhmd.newsapp.framework.presentation.theme.NewsAppTheme
import com.mhmd.newsapp.framework.presentation.utils.ConnectivityManager
import com.mhmd.newsapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoriteListFragment : Fragment() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private lateinit var favoriteViewModel: FavoriteListViewModel
    private lateinit var newsViewModel: NewsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favoriteViewModel =
            ViewModelProvider(requireActivity()).get(FavoriteListViewModel::class.java)

        newsViewModel = ViewModelProvider(requireActivity()).get(NewsListViewModel::class.java)

        favoriteViewModel.onTriggerEventFavorite(NewsListEvent.SearchEvent)

        return ComposeView(requireContext()).apply {
            setContent {

                val news = favoriteViewModel.newsFavorite.value

                val loading = favoriteViewModel.loading.value

                val page = favoriteViewModel.pageFavorite.value

                val dialogQueue = favoriteViewModel.dialogQueue

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
                            AppBar(
                                title = stringResource(R.string.favorites)
                            )
                            if (!loading && news.isEmpty()) {
                                NothingHere()
                            } else {
                                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                                    itemsIndexed(
                                        items = news
                                    ) { index, newsItem ->
                                        favoriteViewModel.onChangeNewsScrollPositionFavorite(index)
                                        if ((index + 1) >= (page * Constants.NEWS_PAGINATION_PAGE_SIZE) && !loading) {
                                            favoriteViewModel.onTriggerEventFavorite(NextPageEvent)
                                        }
                                        NewsCard(
                                            news = newsItem,
                                            index = index,
                                            isFavorite = newsItem.isFavorite,
                                            onClick = {
                                                favoriteViewModel.setSelectedNewsItem(newsItem)
                                                newsViewModel.setSelectedNewsItem(null)
                                                findNavController().navigate(
                                                    R.id.action_favoriteListFragment_to_newsDetailsFragment,
                                                )
                                            },
                                            onFavoriteClick = {

                                                newsViewModel.onRefresh(news[it].title!!)
                                                favoriteViewModel.onFavorite(news[it])
                                            }

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
