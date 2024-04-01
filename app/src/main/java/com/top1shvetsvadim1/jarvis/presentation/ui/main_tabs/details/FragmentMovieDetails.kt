package com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.Insets
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.coreutills.extentions.constrainUp
import com.example.coreutills.factories.AnimatorFactory
import com.example.coreutills.managers.ScreenManager
import com.example.coreutills.wrappers.JobWrapper
import com.top1shvetsvadim1.jarvis.R
import com.top1shvetsvadim1.jarvis.databinding.FragmentMovieDetailsBinding
import com.top1shvetsvadim1.jarvis.domain.models.MovieDetails
import com.top1shvetsvadim1.jarvis.presentation.base.FragmentBaseMVI
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemDescriptionDelegate
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemGeneralInformationDelegate
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemMoviesCastsDelegate
import com.top1shvetsvadim1.jarvis.presentation.ui.main_tabs.details.ui_items.ItemRatingDelegate
import com.top1shvetsvadim1.jarvis.presentation.utils.custo_views.SpaceDecorator
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.capitalizeFirst
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.dp
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.getColorCompat
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.intDimen
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.isDarkMode
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchIO
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.launchUI
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.roundToTwoDecimal
import com.top1shvetsvadim1.jarvis.presentation.utils.extentions.switchToUI
import com.top1shvetsvadim1.jarvis.presentation.utils.recycler_utils.DelegateAdapter
import com.top1shvetsvadim1.jarvis.presentation.utils.special.ConditionalSuspendBlock
import com.top1shvetsvadim1.jarvis.presentation.utils.special.PerformanceManager
import com.top1shvetsvadim1.jarvis.presentation.utils.storage.PropertiesStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class FragmentMovieDetails :
    FragmentBaseMVI<FragmentMovieDetailsBinding, MovieDetailsState, MovieDetailsEvent, MovieDetailsViewModel>() {
    override val viewModel: MovieDetailsViewModel by viewModels()

    private val args: FragmentMovieDetailsArgs by navArgs()

    private val choreographer by lazy {
        Choreographer.getInstance()
    }

    private val devicePerf by lazy { PerformanceManager.create(requireContext()) }

    private val renderConditionalSuspendBlock by lazy {
        ConditionalSuspendBlock(
            isOneTime = true,
            lifecycleScope
        )
    }


    private val renderConditionalSuspendBlock2 by lazy {
        ConditionalSuspendBlock(
            isOneTime = true,
            lifecycleScope
        )
    }

    private var isInBackStack: Boolean = false

    private val clueWrapper = JobWrapper()
    private var clueAnimator: ValueAnimator? = null

    private val adapter = DelegateAdapter.Builder()
        .setDelegates(
            ItemDescriptionDelegate(),
            ItemGeneralInformationDelegate(),
            ItemMoviesCastsDelegate(),
            ItemRatingDelegate()
        )
        .build()

    private val transitionListener by lazy {
        object : MotionLayout.TransitionListener, Transitioner {

            private var elevation = 0f

            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                if (endId == R.id.start) {
                    applyOnBinding {
                        buttonCmb.isVisible = true
                    }
                }
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                applyOnBinding {
                    lifecycleScope.launchUI {
                        if (endId == R.id.FullyExpanded) {
                            val alpha = if (progress < 0.66f) {
                                0f
                            } else {
                                (3.03f * (progress - 0.66f)) constrainUp 1f
                            }
                            val color = context?.let {
                                ColorStateList.valueOf(
                                    ColorUtils.blendARGB(
                                        it.getColorCompat(R.color.c_white_ffffff),
                                        it.getColorCompat(R.color.c_black_000000_ffffff),
                                        alpha
                                    )
                                )
                            } ?: return@launchUI
                            elevation = 4.dp * progress
                            activity?.let {
                                if (devicePerf.isHighPerformance()) {
                                    if (!it.isDarkMode()) {
                                        ScreenManager.setStatusBarContrast(it, progress >= 0.88f)
                                    } else {
                                        ScreenManager.setStatusBarContrast(it, false)
                                    }
                                }
                            }
                            choreographer.postFrameCallback {
                                if (devicePerf.isHighPerformance()) {
                                    if (kotlin.math.abs(topBg.elevation - elevation) > 1 || elevation == 4.dp || elevation == 0f) {
                                        topBg.elevation = elevation
                                        mainImage.elevation = elevation
                                    }
                                }
                                moviesDetails.alpha = 1 - progress * 4

                                val step = when (devicePerf.devicePerformanceClass) {
                                    PerformanceManager.PerformanceClass.HIGH -> 0.001f
                                    PerformanceManager.PerformanceClass.LOW -> 0.035f
                                }
                                if (kotlin.math.abs(moviesDetails.alpha - alpha) > step || alpha == 1f || alpha == 0f) {
                                    //toolbarBg.alpha = 1f - alpha
                                    description.alpha = 0f
                                    share.imageTintList = color
                                    more.imageTintList = color
                                    back.imageTintList = color
                                }
                            }

                        } else if (endId == R.id.end && startId == R.id.start) {
                            Log.d("deb", "start to end")
                            applyOnContext {
                                val bgColor = ColorUtils.blendARGB(
                                    getColorCompat(R.color.c_bg2_fffffff_2d2d2d),
                                    getColorCompat(R.color.c_bg_business_F6F6F6_212121),
                                    progress
                                )
                                choreographer.postFrameCallback {
                                    applyOnBinding {
                                        root.setBackgroundColor(bgColor)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @SuppressLint("ClickableViewAccessibility")
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                applyOnBinding {
                    if (devicePerf.devicePerformanceClass == PerformanceManager.PerformanceClass.LOW) {
                        ValueAnimator.ofFloat(topBg.elevation, elevation).apply {
                            duration = 500
                            addUpdateListener { valueAnimator ->
                                val animatedValue = valueAnimator.animatedValue as Float
                                topBg.elevation = animatedValue
                                mainImage.elevation = animatedValue
                            }
                            start()
                        }
                    }
                    when (currentId) {
                        R.id.start -> {
                            onTransitionChange(motionLayout, R.id.start, R.id.end, 0f)
                        }

                        R.id.end -> {
                            motionLayout?.setTransition(R.id.end, R.id.FullyExpanded)
                            motionLayout?.progress = 0.0001f
                            motionLayout?.progress = 0f
                            mainImageSrc.alpha = 1f
                            description.post {
                                description.alpha = 0f
                            }
                            clueAnimator?.cancel()
                            clueWrapper.cancel()

                            recycler.setOnTouchListener { v, _ ->
                                v.performClick()
                                false
                            }
                            motionLayout?.enableTransition(R.id.from_start_to_end, false)
                            PropertiesStorage.setDropAsync(
                                PropertiesStorage.Properties.ReadMoreClue,
                                false
                            )
                            onTransitionChange(motionLayout, R.id.end, R.id.FullyExpanded, 0f)
                        }

                        R.id.FullyExpanded -> {
                            Log.d("deb", "Fully expanded")
                            onTransitionChange(motionLayout, R.id.end, R.id.FullyExpanded, 1f)
                        }
                    }
                }
            }

            @SuppressLint("ClickableViewAccessibility")
            override fun onTransitionRestored(motionLayout: MotionLayout) {
                applyOnBinding {
                    recycler.setOnTouchListener { v, _ ->
                        v.performClick()
                        false
                    }
                    description.invalidate()
                    description.alpha = 1f
                    recycler.alpha = 1f
                    mainImageSrc.alpha = 1f
                    if (motionLayout.endState == R.id.FullyExpanded) {
                        motionLayout.enableTransition(R.id.from_start_to_end, false)
                    }
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onCreate(savedInstanceState)
        viewModel.handleIntent(MovieDetailsIntent.Load(args.movieId))
    }

    override fun setupInset(inset: Insets) {
        applyOnBinding {
            insets = inset
            statusBar.post {
                statusBar.updateLayoutParams {
                    height = inset.top
                }
            }
            recycler.updatePadding(bottom = inset.bottom)
            val context = root.context
            root.getConstraintSet(R.id.start).apply {
                getConstraint(R.id.button).apply {
                    layout.bottomMargin = insets.bottom + context.intDimen(R.dimen._6sdp)
                }
                /*if (ScreenManager.isShort(context)) {
                    getConstraint(R.id.barrier).apply {
                        layout.verticalBias = 0.60f
                    }
                } else if (ScreenManager.hasButton) {
                    getConstraint(R.id.barrier).apply {
                        layout.verticalBias = 0.66f
                    }
                }*/
            }.let {
                root.updateState(R.id.start, it)
                root.rebuildScene()
            }
        }
    }

    override fun render(state: MovieDetailsState) {
        applyOnBinding {
            state.movieDetails?.let {
                setMoviesDetails(it.details)
            }

            renderConditionalSuspendBlock.action({ insets != Insets.NONE }) {
                root.getConstraintSet(R.id.end).apply {
                    getConstraint(R.id.barrier).apply {}
                }.let {
                    root.updateState(R.id.end, it)
                    root.rebuildScene()
                }
            }
            adapter.submitList(state.items) {
                if (state.items.isNotEmpty()) {
                    startPostponedEnterTransition()
                }
            }
        }
    }

    private fun setMoviesDetails(details: MovieDetails) {
        applyOnBinding {
            mainImageSrc.load(details.backdropPath) {
                placeholder(R.drawable.movie_poster_placeholder)
            }
            roundImageView.load(details.posterPath ?: R.drawable.movie_poster_placeholder) {
                placeholder(R.drawable.movie_poster_placeholder)
            }
            mainTitle.text = details.title
            description.text = details.overview
            mutableListOf(ratingDetails, rating).forEach { view ->
                view.text =
                    String.format("%s/10 IMbd", details.voteAverage.roundToTwoDecimal())
            }
            mutableListOf(genres, genresDetails).forEach { view ->
                view.text = details.genres.joinToString(", ").capitalizeFirst()
            }
            mutableListOf(dataRelease, dataReleaseDetails).forEach { view ->
                view.text = details.releaseDate
            }
            mutableListOf(time, timeDetails).forEach { view ->
                view.text =
                    String.format("%02d:%02d", details.runtime / 60, details.runtime % 60)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun applyOnViews(): FragmentMovieDetailsBinding.() -> Unit {
        return {
            ScreenManager.setStatusBarContrast(requireActivity(), false)
            recycler.adapter = adapter
            recycler.addItemDecoration(SpaceDecorator(requireContext(), R.dimen.default_margin))
            root.setTransitionListener(transitionListener)
            buttonCmb.setOnClickListener {
                root.transitionToState(R.id.end, 500)
            }
            clueWrapper reassign viewLifecycleOwner.lifecycleScope.launchIO {
                delay(5000)
                val isClueNeed =
                    PropertiesStorage.get<Boolean>(PropertiesStorage.Properties.ReadMoreClue)
                if (isClueNeed) {
                    switchToUI {
                        clueAnimator = AnimatorFactory.createSimpleFloatFactory(
                            root,
                            component = AnimatorFactory.AnimatorComponent(0f, 0.35f),
                            duration = 1200,
                            interpolator = AccelerateDecelerateInterpolator(),
                            postConfiguration = {
                                repeatCount = ValueAnimator.INFINITE
                                repeatMode = ValueAnimator.REVERSE
                            }
                        ) { view, value ->
                            view.progress = value
                        }.build()
                        clueAnimator?.start()
                    }
                }
            }
            popUpContainer.setOnTouchListener { _, event ->
                // PropertiesStorage.set(PropertiesStorage.Properties.ReadMoreClue, false)
                clueAnimator?.cancel()
                root.onTouchEvent(event)
                false
            }
            if (root.currentState != R.id.FullyExpanded) {
                recycler.setOnTouchListener { v, _ ->
                    v.performClick()
                    true
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        val motion = binding?.motionBase ?: return
        transitionListener.onTransitionChange(
            motion,
            motion.startState,
            motion.endState,
            motion.progress
        )
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDetailsBinding {
        return FragmentMovieDetailsBinding.inflate(inflater)
    }

    override fun beforeDestroyView() {
        renderConditionalSuspendBlock.forceUnlock()
        renderConditionalSuspendBlock2.forceUnlock()
        isInBackStack = true
        clueAnimator = null
        clueWrapper.cancel()
        PropertiesStorage.set(PropertiesStorage.Properties.ReadMoreClue, true)
    }
}

interface Transitioner {
    fun onTransitionRestored(motionLayout: MotionLayout)
}