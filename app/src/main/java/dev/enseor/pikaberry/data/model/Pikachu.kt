package dev.enseor.pikaberry.data.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import androidx.core.content.res.ResourcesCompat
import dev.enseor.pikaberry.R

class Pikachu(var x: Int, var y: Int, val size: Int, context: Context) {
    val rect = RectF()
    private val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.pikachu, null)

    init {
        updateRect()
    }

    fun updatePosition(centerX: Int, canvasWidth: Int) {
        val targetX = centerX - (size / 2)
        x = targetX.coerceIn(0, canvasWidth - size)
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