package dev.enriqueseor.pikaberry.data.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import androidx.core.content.res.ResourcesCompat
import dev.enriqueseor.pikaberry.R

class Pikachu(var x: Int, var y: Int, val radius: Int, context: Context) {
    val rect = RectF()
    private val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.pikachu, null)

    init {
        updateRect()
    }

    fun updatePosition(newX: Int, canvasWidth: Int) {
        x = newX.coerceIn(radius, canvasWidth - radius)
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