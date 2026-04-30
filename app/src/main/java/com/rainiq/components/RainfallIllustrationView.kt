package com.rainiq.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.rainiq.utils.dpToPx
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * RainfallIllustrationView — Animated rain streaks + rooftop silhouette + filling tank.
 * Used on Onboarding Screen 1.
 */
class RainfallIllustrationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Rain streak paint
    private val rainPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.5f.dpToPx(context)
        strokeCap = Paint.Cap.ROUND
    }

    // House glass fill paint
    private val housePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(30, 255, 255, 255)
    }

    // House outline paint
    private val houseBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.dpToPx(context)
        color = Color.argb(90, 255, 255, 255)
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    // Tank fill paint (teal)
    private val tankFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(180, 78, 205, 196) // accent_teal
    }

    // Tank outline paint
    private val tankOutlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.5f.dpToPx(context)
        color = Color.argb(80, 255, 255, 255)
    }

    private data class RainStreak(
        var x: Float,
        var y: Float,
        val speed: Float,
        val length: Float,
        val alpha: Float
    )
    private val rainAngleDx = 0.26f // tan(15 degrees)

    private val streaks = mutableListOf<RainStreak>()
    private var rainAnimator: ValueAnimator? = null
    private var tankFillLevel: Float = 0f
    private var tankFillAnimator: ValueAnimator? = null

    init {
        post { initStreaks() }
        startRainAnimation()
        startTankFillAnimation()
    }

    private fun initStreaks() {
        streaks.clear()
        val count = 28
        repeat(count) {
            streaks.add(
                RainStreak(
                    x = Random.nextFloat() * (width * 1.5f),
                    y = Random.nextFloat() * height - height,
                    speed = 6f.dpToPx(context) + Random.nextFloat() * 4f.dpToPx(context),
                    length = 12f.dpToPx(context) + Random.nextFloat() * 14f.dpToPx(context),
                    alpha = 0.25f + Random.nextFloat() * 0.35f
                )
            )
        }
    }

    private fun startRainAnimation() {
        rainAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 50L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                streaks.forEach { streak ->
                    streak.y += streak.speed
                    streak.x -= streak.speed * rainAngleDx
                    if (streak.y > height + streak.length || streak.x < -streak.length) {
                        streak.y = -streak.length
                        streak.x = if (width > 0) Random.nextFloat() * (width * 1.5f) else 0f
                    }
                }
                invalidate()
            }
            start()
        }
    }

    private fun startTankFillAnimation() {
        // Tank slowly fills over 3 seconds to show the collection concept
        tankFillAnimator = ValueAnimator.ofFloat(0f, 65f).apply {
            duration = 3000L
            startDelay = 800L
            addUpdateListener {
                tankFillLevel = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2f

        // ── Draw Rain Streaks ─────────────────────────────────
        streaks.forEach { streak ->
            rainPaint.color = Color.argb(
                (streak.alpha * 255).toInt(), 78, 205, 196  // accent_teal — water identity
            )
            val dx = streak.length * rainAngleDx
            canvas.drawLine(
                streak.x, streak.y,
                streak.x - dx, streak.y + streak.length,
                rainPaint
            )
        }

        // ── Draw House/Rooftop Silhouette ─────────────────────
        val houseBottom = h * 0.72f
        val houseLeft = cx - w * 0.28f
        val houseRight = cx + w * 0.28f
        val houseTop = h * 0.32f
        val roofPeak = h * 0.18f

        val housePath = Path().apply {
            // Roof triangle
            moveTo(cx - w * 0.34f, houseTop)
            lineTo(cx, roofPeak)
            lineTo(cx + w * 0.34f, houseTop)
            // Right wall
            lineTo(houseRight, houseTop)
            lineTo(houseRight, houseBottom)
            // Left wall
            lineTo(houseLeft, houseBottom)
            lineTo(houseLeft, houseTop)
            close()
        }
        canvas.drawPath(housePath, housePaint)
        canvas.drawPath(housePath, houseBorderPaint)

        // ── Draw Mini Tank (bottom centre of house) ───────────
        val tankW = w * 0.18f
        val tankH = h * 0.16f
        val tankLeft = cx - tankW / 2f
        val tankTop = houseBottom - tankH - 4f.dpToPx(context)
        val tankRight = cx + tankW / 2f
        val tankBottom = houseBottom - 4f.dpToPx(context)
        val tankCorner = tankW * 0.3f

        // Fill
        if (tankFillLevel > 0) {
            val fillHeight = tankH * (tankFillLevel / 100f)
            canvas.save()
            val clipPath = Path()
            clipPath.addRoundRect(
                tankLeft, tankTop, tankRight, tankBottom,
                tankCorner, tankCorner, Path.Direction.CW
            )
            canvas.clipPath(clipPath)
            canvas.drawRect(tankLeft, tankBottom - fillHeight, tankRight, tankBottom, tankFillPaint)
            canvas.restore()
        }

        // Outline
        val outlineRect = android.graphics.RectF(tankLeft, tankTop, tankRight, tankBottom)
        canvas.drawRoundRect(outlineRect, tankCorner, tankCorner, tankOutlinePaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        rainAnimator?.cancel()
        tankFillAnimator?.cancel()
    }
}
