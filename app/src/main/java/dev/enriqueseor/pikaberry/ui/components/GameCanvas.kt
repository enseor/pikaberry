package dev.enriqueseor.pikaberry.ui.components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import dev.enriqueseor.pikaberry.core.GameEngine
import dev.enriqueseor.pikaberry.data.model.Scoreboard

class GameCanvas(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var engine: GameEngine? = null
    private var scoreboard: Scoreboard? = null

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        engine?.initGame(w, h)
        scoreboard = Scoreboard(w, h, 0, 3, context)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                engine?.movePikachu(event.x)
                invalidate()
                if (event.action == MotionEvent.ACTION_UP) performClick()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val game = engine ?: return

        // 1. Lógica: Le pedimos al motor que actualice posiciones
        game.update()

        // 2. Dibujo: El motor nos da las entidades y nosotros las pintamos
        game.pikachu.setBounds()
        game.pikachu.draw(canvas)

        game.berries.forEach { it.draw(canvas, 100) }
        game.rocks.forEach { it.draw(canvas, 100) }
        game.heart.draw(canvas, 100)

        // 3. UI
        scoreboard?.updateScore(game.score, game.lives)
        scoreboard?.draw(canvas)

        // 4. Bucle: Forzamos el siguiente frame
        invalidate()
    }
}