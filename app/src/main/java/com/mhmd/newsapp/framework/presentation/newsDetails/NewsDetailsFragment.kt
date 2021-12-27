package com.mhmd.newsapp.framework.presentation.newsDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mhmd.newsapp.framework.presentation.components.NewsDetails
import com.mhmd.newsapp.framework.presentation.favoriteList.FavoriteListViewModel
import com.mhmd.newsapp.framework.presentation.newsList.NewsListViewModel
import com.mhmd.newsapp.framework.presentation.theme.NewsAppTheme
import com.mhmd.newsapp.framework.presentation.utils.ConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class NewsDetailsFragment : Fragment() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private lateinit var newsViewModel: NewsListViewModel
    private lateinit var favoriteViewModel: FavoriteListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        newsViewModel = ViewModelProvider(requireActivity()).get(NewsListViewModel::class.java)
        favoriteViewModel =
            ViewModelProvider(requireActivity()).get(FavoriteListViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {

                NewsAppTheme(
                    isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                ) {
                    Box {
                        Column(
                            modifier = Modifier.background(
                                MaterialTheme.colors.background
                            )
                        ) {
                            var isFavorite by remember {
                                mutableStateOf(

                                    if (favoriteViewModel.selectedNewsItem.value == null)
                                        newsViewModel.selectedNewsItem.value!!.isFavorite else favoriteViewModel.selectedNewsItem.value!!.isFavorite
                                )
                            }
                            NewsDetails(
                                news = favoriteViewModel.selectedNewsItem.value
                                    ?: newsViewModel.selectedNewsItem.value!!,
                                isFavorite = isFavorite,
                                onFavoriteClick = {
                                    if (favoriteViewModel.selectedNewsItem.value != null) {
                                        favoriteViewModel.onFavorite(favoriteViewModel.selectedNewsItem.value!!)
                                        newsViewModel.onRefresh(favoriteViewModel.selectedNewsItem.value!!.title!!)
                                    } else {
                                        favoriteViewModel.onFavorite(newsViewModel.selectedNewsItem.value!!)
                                        newsViewModel.onRefresh(newsViewModel.selectedNewsItem.value!!.title!!)
                                    }

                                    isFavorite =
                                        if (favoriteViewModel.selectedNewsItem.value == null)
                                            newsViewModel.selectedNewsItem.value!!.isFavorite else favoriteViewModel.selectedNewsItem.value!!.isFavorite
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
