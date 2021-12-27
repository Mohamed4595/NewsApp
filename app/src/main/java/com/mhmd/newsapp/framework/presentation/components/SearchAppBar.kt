package com.mhmd.newsapp.framework.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mhmd.newsapp.R
import com.mhmd.newsapp.framework.presentation.newsList.NewsCategory
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun SearchAppBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onExecuteSearch: () -> Unit,
    categories: List<NewsCategory>,
    selectedCategory: NewsCategory?,
    onSelectedCategoryChanged: (String) -> Unit,
    scrollPosition: Int,
    onChangeScrollPosition: (Int) -> Unit,
    onFavoriteList: () -> Unit,
    isDark: Boolean
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colors.primary,
        elevation = 8.dp,
        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(.85f)
                        .padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
                    value = query,
                    shape = RoundedCornerShape(50.dp),
                    onValueChange = {
                        onQueryChanged(it)
                    },
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search),
                            style = MaterialTheme.typography.body2.copy(color = Color.White)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onExecuteSearch()
                            keyboardController?.hide()
                        }
                    ),
                    leadingIcon = {
                        Icon(Icons.Filled.Search, "", tint = Color.White)
                    },
                    textStyle = MaterialTheme.typography.body2.copy(color = Color.White),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.White, // hide the indicator
                        unfocusedIndicatorColor = Color.White
                    )
                )
                IconButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(CenterVertically)
                        .padding(start = 8.dp, end = 16.dp),

                    onClick = onFavoriteList,
                ) {
                    Icon(Icons.Filled.Favorite, "", tint = Color.White)
                }
            }
            val listState = rememberLazyListState()
            // Remember a CoroutineScope to be able to launch
            val coroutineScope = rememberCoroutineScope()
            LazyRow(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                contentPadding = PaddingValues(
                    start = 8.dp,
                    end = 8.dp,
                ),
                state = listState,
            ) {
                coroutineScope.launch {
                    // Animate scroll to the first item
                    listState.animateScrollToItem(index = scrollPosition)
                }
                itemsIndexed(
                    items = categories
                ) { index, category ->
                    CategoryChip(
                        category = category.value,
                        isSelected = selectedCategory == category,
                        onSelectedCategoryChanged = {
                            onChangeScrollPosition(index)
                            onSelectedCategoryChanged(it)
                        },
                        onExecuteSearch = {
                            onExecuteSearch()
                        },
                    )
                }
            }
        }
    }
}
