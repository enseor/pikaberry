package dev.enriqueseor.pikaberry.core

import android.content.Context
import android.graphics.RectF
import dev.enriqueseor.pikaberry.data.model.*

class GameEngine(private val context: Context, private val listener: GameEventListener?) {

    var score = 0
    var lives = 3
    var level = 2
    private val baseSpeed = 10
    private val spawnInterval = 900L
    private var lastSpawnTime = 0L

    lateinit var pikachu: Pikachu
    val berries = mutableListOf<Berry>()
    val rocks = mutableListOf<Rock>()
    lateinit var heart: Heart

    private var canvasWidth = 0
    private var canvasHeight = 0

    fun initGame(width: Int, height: Int) {
        canvasWidth = width
        canvasHeight = height
        pikachu = Pikachu(width / 2, height - 100, 100, context)
        heart = Heart((0..width).random(), (-20000..-15000).random() * level, context)
        berries.clear()
        rocks.clear()
    }

    fun update() {
        spawnObjects()

        // Actualizar Corazón
        heart.updatePosition(canvasWidth, canvasHeight, baseSpeed, level)
        checkHeartCollision()

        // Actualizar Berries
        val berryIterator = berries.iterator()
        while (berryIterator.hasNext()) {
            val berry = berryIterator.next()
            berry.updatePosition(canvasWidth, canvasHeight, baseSpeed, level)
            if (checkBerryCollision(berry) || berry.y > canvasHeight + 100) {
                berryIterator.remove()
            }
        }

        // Actualizar Rocks
        val rockIterator = rocks.iterator()
        while (rockIterator.hasNext()) {
            val rock = rockIterator.next()
            rock.updatePosition(canvasWidth, canvasHeight, baseSpeed, level)
            if (checkRockCollision(rock) || rock.y > canvasHeight + 100) {
                rockIterator.remove()
            }
        }
    }

    private fun spawnObjects() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastSpawnTime > spawnInterval) {
            lastSpawnTime = currentTime
            if ((0..100).random() < 70) {
                if (berries.size < 6) berries.add(
                    Berry(
                        (0..canvasWidth).random(),
                        0,
                        context.resources
                    )
                )
            } else {
                if (rocks.size < 6) rocks.add(Rock((0..canvasWidth).random(), 0, context))
            }
        }
    }

    private fun checkBerryCollision(berry: Berry): Boolean {
        if (RectF.intersects(pikachu.rect, berry.rect)) {
            val points = intArrayOf(1, 2, 3, 5, 10)
            score += points[berry.type]
            listener?.onBerryCollected()
            listener?.onScoreUpdated(score)
            return true
        }
        return false
    }

    private fun checkRockCollision(rock: Rock): Boolean {
        if (RectF.intersects(pikachu.rect, rock.rect)) {
            lives = (lives - 1).coerceAtLeast(0)
            listener?.onRockCollision()
            return true
        }
        return false
    }

    private fun checkHeartCollision() {
        if (RectF.intersects(pikachu.rect, heart.rect)) {
            heart.x = (0..canvasWidth).random()
            heart.y = (-17500..-12500).random() * level
            if (lives < 3) lives++
            listener?.onHeartCollected()
        }
    }

    fun movePikachu(x: Float) {
        pikachu.updatePosition(x.toInt(), canvasWidth)
    }
}