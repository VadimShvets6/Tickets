package com.example.coreutills.utils

import kotlin.reflect.KClass

/**
 * Util class to simplify Int/Long -> Double and vice versa math operations
 */
@JvmInline
value class ArithmeticWrapper(val value: Double) {

    inline fun <reified N : Number> operate(operation: (Double) -> Number): N {
        val result = operation(value)
        return when (N::class) {
            Int::class -> result.toInt()
            Float::class -> result.toFloat()
            Double::class -> result.toDouble()
            Long::class -> result.toFloat()
            else -> result
        } as N
    }

    operator fun plus(plus: Number): ArithmeticWrapper {
        return ArithmeticWrapper(value + plus.toDouble())
    }

    operator fun minus(plus: Number): ArithmeticWrapper {
        return ArithmeticWrapper(value - plus.toDouble())
    }

    operator fun div(plus: Number): ArithmeticWrapper {
        return ArithmeticWrapper(value / plus.toDouble())
    }

    operator fun times(plus: Number): ArithmeticWrapper {
        return ArithmeticWrapper(value * plus.toDouble())
    }

    operator fun compareTo(other: Number): Int {
        return value.compareTo(other.toDouble())
    }

    inline fun <reified N : Number> get(): N {
        return operate { value }
    }

    constructor(value: Number) : this(value.toDouble())

    companion object {
        fun isClassSupported(target: KClass<*>): Boolean {
            return target in listOf(
                Int::class,
                Float::class,
                Double::class,
                Long::class
            )
        }
    }

}

fun Number.toArithmetic() = ArithmeticWrapper(this)