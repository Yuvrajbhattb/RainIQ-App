package com.rainiq.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * Extension functions and utility helpers for the RainIQ app.
 */

// ═══════════════════════════════════════════════════════
// Dimension Conversions
// ═══════════════════════════════════════════════════════

/**
 * Converts dp (Float) to pixels.
 */
fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )
}

/**
 * Converts dp (Int) to pixels as Int.
 */
fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

/**
 * Converts sp to pixels.
 */
fun Float.spToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    )
}

/**
 * Quick dp-to-px using system resources (no context needed).
 */
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density

// ═══════════════════════════════════════════════════════
// View Animation Extensions
// ═══════════════════════════════════════════════════════

/**
 * Staggered card entrance animation.
 * Cards slide up 60dp and fade in over 350ms, staggered by 80ms per position.
 */
fun View.animateCardEntrance(position: Int) {
    translationY = 60f.dp
    alpha = 0f
    animate()
        .translationY(0f)
        .alpha(1f)
        .setDuration(350)
        .setStartDelay(position * 80L)
        .setInterpolator(DecelerateInterpolator(2.0f))
        .start()
}

/**
 * FAB spring bounce press animation.
 * Scale: 1.0 → 0.92 → 1.05 → 1.0 (250ms total)
 */
fun View.animateFabPress(onComplete: (() -> Unit)? = null) {
    animate()
        .scaleX(0.88f).scaleY(0.88f)
        .setDuration(100)
        .withEndAction {
            animate()
                .scaleX(1.06f).scaleY(1.06f)
                .setDuration(150)
                .withEndAction {
                    animate()
                        .scaleX(1f).scaleY(1f)
                        .setDuration(100)
                        .withEndAction { onComplete?.invoke() }
                        .start()
                }.start()
        }.start()
}

/**
 * Simple fade-in animation.
 */
fun View.fadeIn(duration: Long = 300) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .start()
}

/**
 * Simple fade-out animation.
 */
fun View.fadeOut(duration: Long = 300) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction { visibility = View.GONE }
        .start()
}

// ═══════════════════════════════════════════════════════
// Number Formatting
// ═══════════════════════════════════════════════════════

/**
 * Formats a number with comma separators (Indian style: 1,00,000)
 */
fun Number.formatIndian(): String {
    val number = this.toLong()
    if (number < 1000) return number.toString()
    val str = number.toString()
    val lastThree = str.takeLast(3)
    val remaining = str.dropLast(3)
    val formatted = remaining.reversed().chunked(2).joinToString(",").reversed()
    return "$formatted,$lastThree"
}

/**
 * Formats liters with one decimal place.
 */
fun Float.formatLiters(): String {
    return if (this % 1f == 0f) {
        this.toInt().formatIndian()
    } else {
        String.format("%.1f", this)
    }
}
