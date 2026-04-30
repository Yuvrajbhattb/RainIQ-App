package com.rainiq.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.rainiq.utils.dpToPx
import kotlin.math.sin
import kotlin.random.Random

/**
 * WaterConversionView — Animated cloud → arrow → tank filling illustration.
 * Used on Onboarding Screen 2 to visualise roof-to-harvest conversion.
 */
class WaterConversionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Cloud paint
    private val cloudPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(60, 255, 255, 255)
    }
    private val cloudStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.5f.dpToPx(context)
        color = Color.argb(120, 255, 255, 255)
    }

    // Arrow paint — teal = water flow identity
    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.dpToPx(context)
        color = Color.argb(200, 78, 205, 196) // accent_teal — water flow
        strokeCap = Paint.Cap.ROUND
    }

    // Raindrops from cloud
    private val dropPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(160, 78, 205, 196) // teal
    }

    // Tank fill
    private val tankFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(180, 78, 205, 196)
    }
    private val tankOutlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.dpToPx(context)
        color = Color.argb(100, 255, 255, 255)
    }
    private val tankLimePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(180, 200, 230, 50) // lime shimmer at water surface — achievement
    }

    // Arrow path tracing
    private var arrowProgress = 0f
    private var arrowAnimator: ValueAnimator? = null
    private var arrowPath = Path()
    private val arrowMeasure = PathMeasure()
    private val tracedArrow = Path()

    // Cloud float
    private var cloudOffset = 0f
    private var cloudAnimator: ValueAnimator? = null

    // Raindrops
    private data class Drop(var x: Float, var y: Float, var alpha: Float, val speed: Float)
    private val drops = mutableListOf<Drop>()
    private var dropAnimator: ValueAnimator? = null

    // Tank fill
    private var tankFill = 0f
    private var tankFillAnimator: ValueAnimator? = null

    init {
        startAnimations()
    }

    private fun startAnimations() {
        cloudAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2500L
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = android.view.animation.AccelerateDecelerateInterpolator()
            addUpdateListener {
                cloudOffset = (it.animatedValue as Float) * 6f.dpToPx(context)
                invalidate()
            }
            start()
        }

        // Arrow traces itself
        arrowAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1200L
            startDelay = 400L
            interpolator = DecelerateInterpolator(1.5f)
            addUpdateListener {
                arrowProgress = it.animatedValue as Float
                invalidate()
            }
            start()
        }

        // Raindrops fall from cloud
        dropAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 60L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                drops.forEach { d ->
                    d.y += d.speed
                    if (d.y > height * 0.7f) {
                        d.y = height * 0.18f
                        d.alpha = 0.4f + Random.nextFloat() * 0.5f
                    }
                }
                invalidate()
            }
            start()
        }

        // Tank fills after arrow animation
        tankFillAnimator = ValueAnimator.ofFloat(0f, 75f).apply {
            duration = 2500L
            startDelay = 1600L
            interpolator = DecelerateInterpolator(2f)
            addUpdateListener {
                tankFill = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Prepare 5 drops under the cloud (left third of screen)
        drops.clear()
        val cloudCx = w * 0.22f
        val baseCy = h * 0.50f
        repeat(5) { i ->
            drops.add(Drop(
                x = cloudCx - 12f.dpToPx(context) + i * 6f.dpToPx(context),
                y = baseCy + 20f.dpToPx(context) + i * 8f.dpToPx(context),
                alpha = 0.5f + Random.nextFloat() * 0.4f,
                speed = 2.5f.dpToPx(context) + Random.nextFloat() * 1.5f.dpToPx(context)
            ))
        }

        // Arrow from cloud to tank
        val arrowStartX = w * 0.38f
        val arrowY = h * 0.50f
        val arrowEndX = w * 0.62f
        arrowPath = Path().apply {
            moveTo(arrowStartX, arrowY)
            lineTo(arrowEndX, arrowY)
        }
        arrowMeasure.setPath(arrowPath, false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()

        // ── Cloud (left ~25%) ─────────────────────────────────
        drawCloud(canvas, w * 0.22f, h * 0.50f + cloudOffset, w * 0.28f)

        // ── Raindrops from Cloud ──────────────────────────────
        drops.forEach { d ->
            dropPaint.alpha = (d.alpha * 255).toInt()
            canvas.drawCircle(d.x, d.y, 3f.dpToPx(context), dropPaint)
        }

        // ── Arrow (centre, traced) ────────────────────────────
        if (arrowProgress > 0) {
            val totalLen = arrowMeasure.length
            arrowMeasure.getSegment(0f, totalLen * arrowProgress, tracedArrow, true)

            arrowPaint.pathEffect = null
            canvas.drawPath(tracedArrow, arrowPaint)

            // Arrow head
            if (arrowProgress > 0.85f) {
                val endX = w * 0.62f
                val endY = h * 0.50f
                val size = 8f.dpToPx(context)
                arrowPaint.pathEffect = null
                val headPath = Path().apply {
                    moveTo(endX, endY)
                    lineTo(endX - size, endY - size * 0.6f)
                    moveTo(endX, endY)
                    lineTo(endX - size, endY + size * 0.6f)
                }
                canvas.drawPath(headPath, arrowPaint)
            }
        }

        // ── Tank (right ~30%) ─────────────────────────────────
        val tankCx = w * 0.78f
        val tankW = w * 0.26f
        val tankH = h * 0.40f
        val tankLeft = tankCx - tankW / 2f
        val tankTop = h * 0.30f
        val tankRight = tankCx + tankW / 2f
        val tankBottom = tankTop + tankH
        val corner = 12f.dpToPx(context)

        // Fill
        if (tankFill > 0) {
            val fillH = tankH * (tankFill / 100f)
            canvas.save()
            val clip = Path()
            clip.addRoundRect(tankLeft, tankTop, tankRight, tankBottom, corner, corner, Path.Direction.CW)
            canvas.clipPath(clip)
            canvas.drawRect(tankLeft, tankBottom - fillH, tankRight, tankBottom, tankFillPaint)
            // Lime top shimmer line
            tankLimePaint.alpha = (tankFill / 100f * 200).toInt()
            canvas.drawRect(tankLeft, tankBottom - fillH, tankRight, tankBottom - fillH + 3f.dpToPx(context), tankLimePaint)
            canvas.restore()
        }

        // Tank outline
        val tankRect = android.graphics.RectF(tankLeft, tankTop, tankRight, tankBottom)
        canvas.drawRoundRect(tankRect, corner, corner, tankOutlinePaint)

        // Tank label "2,340 L"
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb((255 * (tankFill / 100f)).toInt().coerceAtLeast(0), 255, 255, 255)
            textSize = 14f.dpToPx(context)
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
        if (tankFill > 10f) {
            canvas.drawText("2,340 L", tankCx, tankTop - 8f.dpToPx(context), labelPaint)
        }
    }

    private fun drawCloud(canvas: Canvas, cx: Float, cy: Float, width: Float) {
        val r1 = width * 0.28f
        val r2 = width * 0.22f
        val r3 = width * 0.18f
        // Three overlapping circles
        canvas.drawCircle(cx, cy, r1, cloudPaint)
        canvas.drawCircle(cx - r1 * 0.7f, cy + r1 * 0.3f, r2, cloudPaint)
        canvas.drawCircle(cx + r1 * 0.7f, cy + r1 * 0.2f, r3, cloudPaint)
        canvas.drawCircle(cx, cy, r1, cloudStrokePaint)
        canvas.drawCircle(cx - r1 * 0.7f, cy + r1 * 0.3f, r2, cloudStrokePaint)
        canvas.drawCircle(cx + r1 * 0.7f, cy + r1 * 0.2f, r3, cloudStrokePaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cloudAnimator?.cancel()
        arrowAnimator?.cancel()
        dropAnimator?.cancel()
        tankFillAnimator?.cancel()
    }
}
