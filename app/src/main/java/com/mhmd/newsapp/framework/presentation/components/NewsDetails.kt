package com.mhmd.newsapp.framework.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.mhmd.newsapp.R
import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.utils.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun NewsDetails(
    news: News,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        item {
            news.urlToImage?.let { url ->
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp),

                    ) {
                    Image(
                        painter = rememberImagePainter(
                            data = url,
                            builder = {
                                crossfade(1000)
                                // placeholder(R.drawable.app_logo)
                                // transformations(CircleCropTransformation())
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = if (news.title == null) "" else news.title!!,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentWidth(Alignment.Start),
                        style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.onBackground)
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    IconButton(
                        modifier = Modifier
                            .width(40.dp)
                            .height(50.dp)
                            .clip(
                                RoundedCornerShape(
                                    8.dp
                                )
                            )
                            .background(MaterialTheme.colors.primary),

                        onClick = { onFavoriteClick() },
                    ) {
                        Icon(
                            if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            "",
                            tint = Color.White
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    news.publishedAt?.let { publishedAt ->
                        Text(
                            text =
                            "${stringResource(R.string.updated)} ${
                                DateUtils.dateToString(
                                    DateUtils.stringToDate(
                                        publishedAt
                                    )
                                )
                            } ",
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onBackground)
                        )
                    }

                    news.author?.let { author ->
                        Text(
                            text =
                            "${stringResource(R.string.by)} $author",
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onBackground)
                        )
                    }
                }

                news.description?.let { description ->
                    if (description != "N/A") {
                        Text(
                            text = description,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            style = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.onBackground)
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(16.dp))
                news.content?.let { content ->
                    Text(
                        text = content,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onBackground)
                    )
                }
                news.url?.let { url ->
                    LinkifyText(
                        text = stringResource(R.string.seeMore),
                        linkColor = MaterialTheme.colors.primary,
                        linkEntire = true,
                        clickable = true, // false for view-only links
                        onClickLink = {
                            uriHandler.openUri(url)
                        },
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onBackground)
                    )
                }
            }
        }
    }
}
