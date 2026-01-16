package dev.enseor.pikaberry.data.model

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import dev.enseor.pikaberry.R

class Berry(var x: Int, var y: Int, val type: Int, private val resources: Resources) {
    val rect = RectF()
    private var drawable: Drawable? = null
    private val radius = 100

    init {
        val berriesDrawable = arrayOf(
            R.drawable.razz_berry,
            R.drawable.pinap_berry,
            R.drawable.nanap_berry,
            R.drawable.pinap_berry_silver,
            R.drawable.razz_berry_golden
        )
        drawable = ResourcesCompat.getDrawable(resources, berriesDrawable[type], null)
        updateRect()
    }

    private fun updateRect() {
        rect.set(
            (x - radius).toFloat(),
            (y - radius).toFloat(),
            (x + radius).toFloat(),
            (y + radius).toFloat()
        )
    }

    fun setPosition(newX: Int, newY: Int) {
        x = newX
        y = newY
        updateRect()
    }

    fun draw(canvas: Canvas) {
        drawable?.let {
            it.setBounds(
                rect.left.toInt(),
                rect.top.toInt(),
                rect.right.toInt(),
                rect.bottom.toInt()
            )
            it.draw(canvas)
        }
    }
}