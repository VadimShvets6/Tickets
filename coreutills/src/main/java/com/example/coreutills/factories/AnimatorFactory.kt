package com.example.coreutills.factories

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class AnimatorFactory<T : Any, V : View>(
    val view: V,
    val component: AnimatorComponent<T>,
    val type: AnimatorType<T>,
    val instruction: AnimationInstruction<T, V>,
    val configuration: AnimationConfiguration
) {

    /**
     * Use this class as an argument for [type] to define Argb animator.
     */
    object RGB


    class AnimatorComponent<T>(vararg val animatorValues: T)

    class AnimatorType<T : Any>(val type: KClass<T>, val elevator: TypeEvaluator<T>? = null)

    class AnimationInstruction<T : Any, V : View>(val onChange: (V, T) -> Unit)

    class AnimationConfiguration(
        val duration: Duration,
        val interpolator: Interpolator,
        val postConfiguration: ValueAnimator.() -> Unit = {}
    ) {
        constructor(
            duration: Int,
            interpolator: Interpolator = LinearInterpolator(),
            postConfiguration: ValueAnimator.() -> Unit = {}
        ) : this(
            duration.milliseconds,
            interpolator,
            postConfiguration
        )
    }

    fun buildAndStart() {
        build().start()
    }

    @Suppress("UNCHECKED_CAST")
    fun build(): ValueAnimator {
        val valueAnimator = when (type.type) {
            Int::class -> ValueAnimator.ofInt(*(component.animatorValues as Array<Int>).toIntArray())
            Float::class -> ValueAnimator.ofFloat(*(component.animatorValues as Array<Float>).toFloatArray())
            RGB::class -> ValueAnimator.ofArgb(*(component.animatorValues as Array<Int>).toIntArray())
            else -> ValueAnimator.ofObject(type.elevator, *(component.animatorValues))
        }
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as T
            instruction.onChange(view, value)
        }
        valueAnimator.apply {
            interpolator = configuration.interpolator
            duration = configuration.duration.inWholeMilliseconds
            configuration.postConfiguration(this)
        }
        return valueAnimator
    }

    companion object {
        fun <V : View> createSimpleFactory(
            view: V,
            component: AnimatorComponent<Int>,
            duration: Int,
            interpolator: Interpolator = LinearInterpolator(),
            postConfiguration: ValueAnimator.() -> Unit = {},
            onChange: (V, Int) -> Unit,
        ): AnimatorFactory<Int, V> {
            return AnimatorFactory(
                view = view,
                component = component,
                type = AnimatorType(type = Int::class, elevator = null),
                instruction = AnimationInstruction(onChange),
                configuration = AnimationConfiguration(
                    duration = duration.milliseconds,
                    interpolator = interpolator,
                    postConfiguration = postConfiguration
                )
            )
        }

        fun <V : View> startSimpleFloatFactory(
            view: V,
            component: AnimatorComponent<Float>,
            duration: Int,
            interpolator: Interpolator = LinearInterpolator(),
            postConfiguration: ValueAnimator.() -> Unit = {},
            onChange: (V, Float) -> Unit
        ) {
            createSimpleFloatFactory(
                view,
                component,
                duration,
                interpolator,
                postConfiguration,
                onChange
            ).buildAndStart()
        }

        fun <V : View> startSimpleFactory(
            view: V,
            component: AnimatorComponent<Int>,
            duration: Int,
            interpolator: Interpolator = LinearInterpolator(),
            postConfiguration: ValueAnimator.() -> Unit = {},
            onChange: (V, Int) -> Unit
        ) {
            createSimpleFactory(view, component, duration, interpolator, postConfiguration, onChange).buildAndStart()
        }

        fun <V : View> createSimpleFloatFactory(
            view: V,
            component: AnimatorComponent<Float>,
            duration: Int,
            interpolator: Interpolator = LinearInterpolator(),
            postConfiguration: ValueAnimator.() -> Unit = {},
            onChange: (V, Float) -> Unit
        ): AnimatorFactory<Float, V> {
            return AnimatorFactory(
                view = view,
                component = component,
                type = AnimatorType(type = Float::class, elevator = null),
                instruction = AnimationInstruction(onChange),
                configuration = AnimationConfiguration(
                    duration = duration.milliseconds,
                    interpolator = interpolator,
                    postConfiguration = postConfiguration
                )
            )
        }

        val percentAnimator get() = AnimatorComponent(0f, 1f)
        val inversePercentAnimator get() = AnimatorComponent(1f, 0f)
    }
}