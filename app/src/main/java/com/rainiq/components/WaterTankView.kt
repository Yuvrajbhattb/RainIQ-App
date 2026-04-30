package com.rainiq.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.rainiq.R
import com.rainiq.utils.dpToPx
import kotlin.math.sin

/**
 * WaterTankView — Custom view for the animated hero water tank on the dashboard.
 * Features a glass tank outline and animated water filling with sine wave.
 */
class WaterTankView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val tankPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f.dpToPx(context)
        color = ContextCompat.getColor(context, R.color.border_glass_light)
        strokeCap = Paint.Cap.ROUND
    }

    private val waterPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.accent_teal)
        alpha = 210 // Semi-transparent teal — water identity
    }

    private val wavePath = Path()
    private val tankRect = RectF()

    private var fillPercentage: Float = 0f
    private var targetPercentage: Float = 0f
    private var waveOffset: Float = 0f

    private var waveAnimator: ValueAnimator? = null
    private var fillAnimator: ValueAnimator? = null

    init {
        startWaveAnimation()
    }

    fun setFillPercentage(percentage: Float, animate: Boolean = true) {
        targetPercentage = percentage.coerceIn(0f, 100f)
        if (animate) {
            fillAnimator?.cancel()
            fillAnimator = ValueAnimator.ofFloat(fillPercentage, targetPercentage).apply {
                duration = 1500
                addUpdateListener {
                    fillPercentage = it.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            fillPercentage = targetPercentage
            invalidate()
        }
    }

    private fun startWaveAnimation() {
        waveAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                waveOffset = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = tankPaint.strokeWidth
        tankRect.set(padding, padding, w - padding, h - padding)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw Tank Outline (Capsule shape)
        val cornerRadius = tankRect.width() / 2f
        canvas.drawRoundRect(tankRect, cornerRadius, cornerRadius, tankPaint)

        // Draw Water Fill
        if (fillPercentage > 0) {
            val fillHeight = tankRect.height() * (fillPercentage / 100f)
            val waterTop = tankRect.bottom - fillHeight

            wavePath.reset()
            
            // Start from bottom left
            wavePath.moveTo(tankRect.left, tankRect.bottom)
            wavePath.lineTo(tankRect.left, waterTop)

            // Draw Sine Wave
            val waveLength = tankRect.width()
            val waveAmplitude = 8f.dpToPx(context)
            val step = 2f.dpToPx(context)
            
            var x = tankRect.left
            while (x <= tankRect.right) {
                val progress = (x - tankRect.left) / waveLength
                val y = waterTop + sin((progress + waveOffset) * 2 * Math.PI) * waveAmplitude
                wavePath.lineTo(x, y.toFloat())
                x += step
            }

            // Finish path
            wavePath.lineTo(tankRect.right, tankRect.bottom)
            wavePath.close()

            // Clip to tank bounds so water doesn't spill outside the rounded corners
            canvas.save()
            val clipPath = Path()
            clipPath.addRoundRect(tankRect, cornerRadius, cornerRadius, Path.Direction.CW)
            canvas.clipPath(clipPath)
            
            canvas.drawPath(wavePath, waterPaint)
            canvas.restore()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        waveAnimator?.cancel()
        fillAnimator?.cancel()
    }
}
