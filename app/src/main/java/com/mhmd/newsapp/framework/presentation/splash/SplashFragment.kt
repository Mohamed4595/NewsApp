package com.mhmd.newsapp.framework.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.mhmd.newsapp.R
import com.mhmd.newsapp.framework.presentation.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                NewsAppTheme(
                    isNetworkAvailable = true,
                    isNoStatusBar = true
                ) {
                    val scale = remember {
                        androidx.compose.animation.core.Animatable(0f)
                    }
                    // Animation
                    LaunchedEffect(key1 = true) {
                        scale.animateTo(
                            targetValue = 0.3f,
                            // tween Animation
                            animationSpec = tween(
                                durationMillis = 800,
                                easing = {
                                    OvershootInterpolator(2f).getInterpolation(it)
                                }
                            )
                        )
                        // Customize the delay time
                        delay(1000L)
                        findNavController().navigate(
                            R.id.action_splashFragment_to_newsListFragment
                        )
                    }

                    // Image
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Change the logo
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "",
                            alignment = Alignment.Center,
                            modifier = Modifier.scale(scale.value)
                        )
                    }
                }
            }
        }
    }
}
