package com.mhmd.newsapp.framework.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.mhmd.newsapp.R
import com.mhmd.newsapp.business.domain.model.News
import com.mhmd.newsapp.utils.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun NewsCard(
    news: News,
    isFavorite: Boolean = false,
    index: Int,
    onClick: () -> Unit,
    onFavoriteClick: (Int) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp,
            )
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        elevation = 8.dp,
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(MaterialTheme.colors.background)

            ) {

                Image(
                    painter = rememberImagePainter(
                        data = if (news.urlToImage == null) "" else news.urlToImage,
                        builder = {
                            crossfade(1000)
                            error(R.drawable.app_logo)
                            // placeholder(R.drawable.app_logo)
                            // transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,

                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (news.title == null) "" else news.title.toString(),
                        maxLines = 2, overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .wrapContentWidth(Alignment.Start),
                        style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.onSurface)
                    )

                    news.publishedAt?.let { publishedAt ->

                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_access_time_24),
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(15.dp)
                                    .align(CenterVertically),
                                contentDescription = ""
                            )
                            Text(
                                text = DateUtils.dateToString(DateUtils.stringToDate(publishedAt)),
                                maxLines = 1, overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(CenterVertically)
                                    .padding(horizontal = 8.dp)
                                    .wrapContentWidth(Alignment.Start),
                                style = MaterialTheme.typography.body2.copy(color = Color.Gray)
                            )
                        }
                    }
                }
            }

            IconButton(
                modifier = Modifier
                    .align(BottomEnd)
                    .width(40.dp)
                    .height(30.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 10.dp
                        )
                    )
                    .background(MaterialTheme.colors.primary),
                onClick = { onFavoriteClick(index) },
            ) {
                Icon(if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, "", tint = Color.White)
            }
        }
    }
}
