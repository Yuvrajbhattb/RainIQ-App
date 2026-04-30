package com.rainiq.components

import android.content.Context
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.rainiq.utils.dpToPx

/**
 * GlassCardView — Custom MaterialCardView implementing the L2 Glass effect
 * from the Liquid Nature Glass design system.
 *
 * Properties:
 * - Background: rgba(15, 28, 12, 0.62) — deep green at 62% opacity
 * - Border: 1dp, rgba(255, 255, 255, 0.14) — subtle white shimmer
 * - Corner radius: 24dp
 * - Elevation: 0dp (glass doesn't cast Material shadows)
 * - Backdrop blur: 40px on API 31+ via RenderEffect
 */
class GlassCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    init {
        // Zero elevation — glass absorbs light, no material shadows
        cardElevation = 0f
        maxCardElevation = 0f

        // L2 Glass background: rgba(15, 28, 12, 0.62)
        // #9E = 158/255 ≈ 62% opacity
        setCardBackgroundColor(Color.parseColor("#9E0F1C0C"))

        // Border: 1dp, rgba(255, 255, 255, 0.14)
        strokeWidth = 1.dpToPx(context)
        strokeColor = Color.parseColor("#24FFFFFF")

        // Corner radius: 24dp
        radius = 24f.dpToPx(context)

        // Prevent corner overlap for proper clipping
        preventCornerOverlap = true

        // No ripple on card itself (children handle interaction)
        isClickable = false
        isFocusable = false
    }

    /**
     * Adjusts the glass level for different component types.
     * Call this to switch between L1-L5 glass variants.
     */
    fun setGlassLevel(level: GlassLevel) {
        when (level) {
            GlassLevel.BASE -> {
                setCardBackgroundColor(Color.parseColor("#800F1C0C"))
                radius = 20f.dpToPx(context)
            }
            GlassLevel.CARD -> {
                setCardBackgroundColor(Color.parseColor("#9E0F1C0C"))
                radius = 24f.dpToPx(context)
            }
            GlassLevel.FLOATING -> {
                setCardBackgroundColor(Color.parseColor("#C70C180A"))
                radius = 24f.dpToPx(context)
            }
            GlassLevel.NAV -> {
                setCardBackgroundColor(Color.parseColor("#CC0A140A"))
                radius = 36f.dpToPx(context)
            }
            GlassLevel.STAT -> {
                setCardBackgroundColor(Color.parseColor("#9E0F1C0C"))
                radius = 20f.dpToPx(context)
            }
        }
    }

    enum class GlassLevel {
        BASE,       // L1 — Section dividers, 50% opacity
        CARD,       // L2 — Main content cards, 62% opacity (default)
        FLOATING,   // L3 — Popovers, tooltips, dialogs, 78% opacity
        NAV,        // L4 — Bottom nav bar, 80% opacity
        STAT        // Stat metric cards — same as L2 but 20dp radius
    }
}
