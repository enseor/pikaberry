package dev.enriqueseor.pikaberry.data.model

import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import android.content.Context
import dev.enriqueseor.pikaberry.R

class Heart(var x: Int, var y: Int, context: Context) {
    val rect = RectF()
    private val drawable: Drawable? =
        ResourcesCompat.getDrawable(context.resources, R.drawable.heart, null)
    private val radius = 100

    init {
        updateRect()
    }

    fun setPosition(newX: Int, newY: Int) {
        x = newX
        y = newY
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