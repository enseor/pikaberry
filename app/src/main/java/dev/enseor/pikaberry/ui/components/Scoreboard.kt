package dev.enseor.pikaberry.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.res.ResourcesCompat
import dev.enseor.pikaberry.R

class Scoreboard(
    private val canvasWidth: Int,
    private val canvasHeight: Int,
    context: Context
) {
    private var score: Int = 0
    private var lives: Int = 3

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
        textSize = canvasHeight * 0.06f
    }

    private val paintWhite = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val berryDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.berry, null)
    private val heartDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.heart, null)
    private val bgRect = RectF(0f, 0f, canvasWidth.toFloat(), canvasHeight * 0.1f)
    private val iconSize = canvasHeight * 0.06f
    private val berryRect = RectF(
        canvasWidth * 0.05f,
        canvasHeight * 0.02f,
        canvasWidth * 0.05f + iconSize,
        canvasHeight * 0.02f + iconSize
    )

    fun updateScore(score: Int, lives: Int) {
        this.score = score
        this.lives = lives
    }

    fun draw(canvas: Canvas) {

        canvas.drawRect(bgRect, paintWhite)

        berryDrawable?.let {
            it.setBounds(
                berryRect.left.toInt(),
                berryRect.top.toInt(),
                berryRect.right.toInt(),
                berryRect.bottom.toInt()
            )
            it.draw(canvas)
        }

        canvas.drawText(
            "$score",
            berryRect.right + 20f,
            berryRect.centerY() + (textPaint.textSize / 3),
            textPaint
        )

        val heartSize = canvasHeight * 0.06f
        val startX = canvasWidth - canvasWidth * 0.05f
        val centerY = bgRect.centerY()

        for (i in 0 until lives) {
            val right = startX - (i * (heartSize + 15f))
            val left = right - heartSize
            val top = centerY - (heartSize / 2)
            val bottom = centerY + (heartSize / 2)
            heartDrawable?.setBounds(
                left.toInt(),
                top.toInt(),
                right.toInt(),
                bottom.toInt()
            )
            heartDrawable?.draw(canvas)
        }
    }
}