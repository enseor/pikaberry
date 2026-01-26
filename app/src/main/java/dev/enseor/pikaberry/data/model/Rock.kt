package dev.enseor.pikaberry.data.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import dev.enseor.pikaberry.R

class Rock(var x: Int, var y: Int, val size: Int, context: Context) {
    val rect = RectF()
    private val drawable: Drawable? =
        ResourcesCompat.getDrawable(context.resources, R.drawable.golem, null)

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