package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.top1shvetsvadim1.jarvis.databinding.FragmentMovieDetailsBinding
import com.top1shvetsvadim1.jarvis.presentation.base.FragmentBaseMVI
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class FragmentMovieDetails :
    FragmentBaseMVI<FragmentMovieDetailsBinding, MovieDetailsState, MovieDetailsEvent, MovieDetailsViewModel>() {
    override val viewModel: MovieDetailsViewModel by viewModels()

    private val args: FragmentMovieDetailsArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Deb", "MovieID: ${args.movieId}")
    }

    override fun render(state: MovieDetailsState) {
       requireBinding().root.doOnPreDraw {
           startPostponedEnterTransition()
       }
    }

    override fun applyOnViews(): FragmentMovieDetailsBinding.() -> Unit {
        return {
            postponeEnterTransition()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMovieDetailsBinding {
        return FragmentMovieDetailsBinding.inflate(inflater)
    }
}