package dev.enriqueseor.pikaberry.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import dev.enriqueseor.pikaberry.model.Berry
import dev.enriqueseor.pikaberry.model.Heart
import dev.enriqueseor.pikaberry.model.Pikachu
import dev.enriqueseor.pikaberry.model.Rock
import dev.enriqueseor.pikaberry.model.Scoreboard
import dev.enriqueseor.pikaberry.view.util.GameEventListener

class GameCanvas(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var canvasWidth: Int = 0
    private var canvasHeight: Int = 0
    private var radius: Int = 100
    private var baseSpeed: Int = 10
    private var level: Int = 2
    private var score: Int = 0
    private var lives: Int = 3
    private var lastSpawnTime: Long = 0
    private val spawnInterval = 900L

    private lateinit var pikachu: Pikachu
    private val berries = mutableListOf<Berry>()
    private val rocks = mutableListOf<Rock>()
    private val MAX_BERRIES_ON_SCREEN = 6
    private val MAX_ROCKS_ON_SCREEN = 6
    private lateinit var heart: Heart
    private lateinit var scoreboard: Scoreboard

    private var gameEventListener: GameEventListener? = null

    private fun initObjects() {
        val pikachuRadius = 100
        pikachu = Pikachu(canvasWidth / 2, canvasHeight - 100, pikachuRadius, context)

        berries.clear()
        rocks.clear()

        heart = Heart(
            x = (0..canvasWidth).random(),
            y = (-20000..-15000).random() * level,
            context = context
        )

        scoreboard = Scoreboard(canvasWidth, canvasHeight, score, lives, context)
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)

        canvasWidth = w
        canvasHeight = h

        initObjects()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                pikachu.updatePosition(event.x.toInt(), canvasWidth)
                this.invalidate()
            }
            MotionEvent.ACTION_UP -> {
                pikachu.updatePosition(event.x.toInt(), canvasWidth)
                this.invalidate()
                performClick()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /******************************************************
        *                       PIKACHU                       *
        *******************************************************/
        pikachu.setBounds()
        pikachu.draw(canvas)

        /******************************************************
         *                       BERRIES                      *
         ******************************************************/
        spawnObjects()

        val iteratorBerries = berries.iterator()
        while (iteratorBerries.hasNext()) {
            val berry = iteratorBerries.next()
            berry.draw(canvas, radius)
            berry.updatePosition(canvasWidth, canvasHeight, baseSpeed, level)

            if (checkBerryCollisionAndRemove(berry)) {
                iteratorBerries.remove()
            } else if (berry.y > canvasHeight + radius) {
                iteratorBerries.remove()
            }
        }

        /*****************************************************
         *                        ROCKS                      *
         *****************************************************/
        val iteratorRocks = rocks.iterator()
        while (iteratorRocks.hasNext()) {
            val rock = iteratorRocks.next()
            rock.draw(canvas, radius)
            rock.updatePosition(canvasWidth, canvasHeight, baseSpeed, level)

            if (checkRockCollisionAndRemove(rock)) {
                iteratorRocks.remove()
            } else if (rock.y > canvasHeight + radius) {
                iteratorRocks.remove()
            }
        }

        /*****************************************************
         *                        HEART                      *
         *****************************************************/
        heart.draw(canvas, radius)
        heart.updatePosition(canvasWidth, canvasHeight, baseSpeed, level)
        onHeartCollision()

        /*****************************************************
         *                     SCOREBOARD                    *
         *****************************************************/
        scoreboard.draw(canvas)
        scoreboard.updateScore(score, lives)
    }

    private fun checkBerryCollisionAndRemove(berry: Berry): Boolean {
        val berryPointsArray = intArrayOf(1, 2, 3, 5, 10)
        if (RectF.intersects(pikachu.rect, berry.rect)) {
            val berryPoints = berryPointsArray[berry.type]
            score += berryPoints
            gameEventListener?.onBerryCollected()
            gameEventListener?.onScoreUpdated(score)
            return true
        }
        return false
    }

    private fun checkRockCollisionAndRemove(rock: Rock): Boolean {
        if (RectF.intersects(pikachu.rect, rock.rect)) {
            gameEventListener?.onRockCollision()
            if (lives > 0) {
                lives -= 1
            }
            return true
        }
        return false
    }

    private fun spawnObjects() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastSpawnTime > spawnInterval) {
            lastSpawnTime = currentTime

            val spawnRoll = (0..100).random()

            if (spawnRoll < 70) {
                if (berries.size < MAX_BERRIES_ON_SCREEN) {
                    val newBerry = Berry((0..canvasWidth).random(), 0, resources)
                    berries.add(newBerry)
                }
            } else {
                if (rocks.size < MAX_ROCKS_ON_SCREEN) {
                    val newRock = Rock((0..canvasWidth).random(), 0, context)
                    rocks.add(newRock)
                }
            }
        }
    }

    private fun onHeartCollision() {
        if (RectF.intersects(pikachu.rect, heart.rect)) {
            heart.x = (0..canvasWidth).random()
            heart.y = (-17500..-12500).random() * level
            gameEventListener?.onHeartCollected()
            if (lives < 3) {
                lives += 1
            }
        }
    }

    fun setGameEventListener(listener: GameEventListener?) {
        this.gameEventListener = listener
    }

    fun setDifficultyLevel(level: Int) {
        this.level = level
    }
}