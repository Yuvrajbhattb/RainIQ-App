package com.rainiq.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.rainiq.utils.dpToPx

/**
 * ImpactCounterView — Displays animated triple stat counters + badge row.
 * Used on Onboarding Screen 4.
 *
 * Counters: household days, trees supported, money saved.
 * Badges pop in with spring/overshoot effect.
 */
class ImpactCounterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Card paint
    private val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(80, 15, 28, 12) // glass_primary
    }
    private val cardBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f.dpToPx(context)
        color = Color.argb(35, 255, 255, 255)
    }

    // Graph paints — dual tone: lime stroke, teal area fill
    private val graphStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.dpToPx(context)
        color = Color.argb(160, 200, 230, 50) // lime stroke
    }
    private val graphFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.argb(30, 78, 205, 196) // teal area fill — water identity
    }

    // Value text paints — semantic per metric type
    private val valuePaintLime = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(255, 200, 230, 50) // accent_lime — achievement/reward
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }
    private val valuePaintTeal = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(255, 78, 205, 196) // accent_teal — water metric
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    // Label text paint (white secondary)
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(185, 255, 255, 255) // text_secondary
        textAlign = Paint.Align.CENTER
    }

    // Emoji paint
    private val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    // Badge emojis
    private val badges = listOf("🏆", "🔥", "💧", "🌱", "⭐")
    private val badgeScales = FloatArray(5) { 0f }
    private var badgeAnimators = mutableListOf<ValueAnimator>()

    // Counter values (animated)
    private var householdDays = 0
    private var trees = 0
    private var moneySaved = 0
    private var graphProgress = 0f

    private var counterAnimator: ValueAnimator? = null

    init {
        startAnimations()
    }

    private fun startAnimations() {
        // Counter animation (0 to target)
        counterAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1400L
            interpolator = DecelerateInterpolator(1.5f)
            addUpdateListener { anim ->
                val p = anim.animatedValue as Float
                householdDays = (47 * p).toInt()
                trees = (18 * p).toInt()
                moneySaved = (1200 * p).toInt()
                graphProgress = p
                invalidate()
            }
            start()
        }

        // Badge pop-in with staggered overshoot
        badges.indices.forEach { i ->
            val anim = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 450L
                startDelay = 600L + i * 120L
                interpolator = OvershootInterpolator(2.5f)
                addUpdateListener {
                    badgeScales[i] = it.animatedValue as Float
                    invalidate()
                }
            }
            badgeAnimators.add(anim)
            anim.start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        val margin = 12f.dpToPx(context)
        val cardH = h * 0.26f
        val cardRadius = 16f.dpToPx(context)

        val statData = listOf(
            // Water metric → teal, Achievement/money → lime
            Triple("🏠", "${householdDays}d", Pair("Household Days", false)),  // water → teal
            Triple("🌳", "$trees", Pair("Trees Supported", true)),             // sustainability → lime
            Triple("💰", "₹$moneySaved", Pair("Money Saved", true))             // reward → lime
        )

        // Draw 3 glass stat cards vertically stacked in the top ~80% of the view
        statData.forEachIndexed { i, (emoji, value, labelAndType) ->
            val (label, isLime) = labelAndType
            val top = i * (cardH + margin)
            val cardRect = RectF(margin, top, w - margin, top + cardH)
            canvas.drawRoundRect(cardRect, cardRadius, cardRadius, cardPaint)
            canvas.drawRoundRect(cardRect, cardRadius, cardRadius, cardBorderPaint)

            val cx = w / 2f
            val cy = top + cardH / 2f

            // Mini Area Graph
            val cardInnerW = w - margin * 2
            val pX1 = margin
            val pY1 = top + cardH * 0.8f
            val pX2 = margin + cardInnerW * 0.33f
            val pY2 = top + cardH * 0.5f
            val pX3 = margin + cardInnerW * 0.66f
            val pY3 = top + cardH * 0.65f
            val pX4 = margin + cardInnerW
            val pY4 = top + cardH * 0.2f

            val clipW = margin + cardInnerW * graphProgress

            canvas.save()
            canvas.clipRect(margin, top, clipW, top + cardH)

            val path = Path()
            path.moveTo(pX1, pY1)
            path.cubicTo(pX1 + (pX2 - pX1) / 2, pY1, pX2 - (pX2 - pX1) / 2, pY2, pX2, pY2)
            path.cubicTo(pX2 + (pX3 - pX2) / 2, pY2, pX3 - (pX3 - pX2) / 2, pY3, pX3, pY3)
            path.cubicTo(pX3 + (pX4 - pX3) / 2, pY3, pX4 - (pX4 - pX3) / 2, pY4, pX4, pY4)

            val fillPath = Path(path)
            fillPath.lineTo(pX4, top + cardH)
            fillPath.lineTo(pX1, top + cardH)
            fillPath.close()

            canvas.drawPath(fillPath, graphFillPaint)
            canvas.drawPath(path, graphStrokePaint)
            canvas.restore()

            // Emoji
            emojiPaint.textSize = 20f.dpToPx(context)
            canvas.drawText(emoji, cx - w * 0.20f, cy + 7f.dpToPx(context), emojiPaint)

            // Value — semantic color: teal for water, lime for achievement
            val vPaint = if (isLime) valuePaintLime else valuePaintTeal
            vPaint.textSize = 24f.dpToPx(context)
            canvas.drawText(value, cx + w * 0.08f, cy + 4f.dpToPx(context), vPaint)

            // Label
            labelPaint.textSize = 11f.dpToPx(context)
            canvas.drawText(label, cx + w * 0.08f, cy + 18f.dpToPx(context), labelPaint)
        }

        // ── Badge Row ─────────────────────────────────────────
        val badgeAreaTop = h * 0.82f
        val badgeSize = 36f.dpToPx(context)
        val totalBadgeW = badges.size * badgeSize + (badges.size - 1) * 8f.dpToPx(context)
        var bx = (w - totalBadgeW) / 2f + badgeSize / 2f

        badges.forEachIndexed { i, emoji ->
            val scale = badgeScales[i]
            if (scale > 0f) {
                canvas.save()
                canvas.scale(scale, scale, bx, badgeAreaTop + badgeSize / 2f)
                emojiPaint.textSize = 26f.dpToPx(context)
                canvas.drawText(emoji, bx, badgeAreaTop + badgeSize * 0.75f, emojiPaint)
                canvas.restore()
            }
            bx += badgeSize + 8f.dpToPx(context)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        counterAnimator?.cancel()
        badgeAnimators.forEach { it.cancel() }
    }
}
