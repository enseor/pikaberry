package dev.enseor.pikaberry.data.model

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import dev.enseor.pikaberry.R

class Berry(var x: Int, var y: Int, val type: Int, val size: Int, private val resources: Resources) {
    val rect = RectF()
    private var drawable: Drawable? = null

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
            x.toFloat(),
            y.toFloat(),
            (x + size).toFloat(),
            (y + size).toFloat()
        )
    }

    fun setPosition(newX: Int, newY: Int) {
        x = newX
        y = newY
        updateRect()
    }

    fun draw(canvas: Canvas) {
        drawable?.let {
            it.setBounds(rect.left.toInt(), rect.top.toInt(), rect.right.toInt(), rect.bottom.toInt())
            it.draw(canvas)
        }
    }
}