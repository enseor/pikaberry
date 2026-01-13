package dev.enriqueseor.pikaberry.ui.components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import dev.enriqueseor.pikaberry.core.GameEngine
import dev.enriqueseor.pikaberry.ui.components.Scoreboard

class GameCanvas(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var engine: GameEngine? = null
    private var scoreboard: Scoreboard? = null

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        engine?.initGame(w, h)
        scoreboard = Scoreboard(w, h, context)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
            engine?.movePikachu(event.x)
        }
        if (event.action == MotionEvent.ACTION_UP) performClick()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val game = engine ?: return

        game.pikachu.draw(canvas)

        game.berries.forEach { it.draw(canvas) }
        game.rocks.forEach { it.draw(canvas) }
        game.hearts.forEach { it.draw(canvas) }

        scoreboard?.updateScore(game.score, game.lives)
        scoreboard?.draw(canvas)
    }

    fun updateView() {
        invalidate()
    }
}