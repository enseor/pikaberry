package dev.enriqueseor.pikaberry.core

import android.content.Context
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import dev.enriqueseor.pikaberry.data.model.*

class GameEngine(private val context: Context, private val listener: GameEventListener?) {

    var score = 0
    var lives = 3
    var level = 2
    private val baseSpeed = 10
    private var lastSpawnTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private val gameRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                update()
                listener?.onTick()
                handler.postDelayed(this, 16)
            }
        }
    }

    lateinit var pikachu: Pikachu
    val berries = mutableListOf<Berry>()
    val rocks = mutableListOf<Rock>()
    val hearts = mutableListOf<Heart>()
    lateinit var heart: Heart

    private var canvasWidth = 0
    private var canvasHeight = 0

    fun start() {
        isRunning = true
        handler.post(gameRunnable)
    }

    fun stop() {
        isRunning = false
        handler.removeCallbacks(gameRunnable)
    }

    fun isGameOver() = lives <= 0

    fun initGame(width: Int, height: Int) {
        canvasWidth = width
        canvasHeight = height
        pikachu = Pikachu(width / 2, height - 100, 100, context)
        heart = Heart((0..width).random(), (-17500..-12500).random(), context)
        berries.clear()
        rocks.clear()
    }

    fun update() {
        spawnObjects()
        val currentSpeed = baseSpeed * level

        // UPDATE HEART
        heart.setPosition(heart.x, heart.y + currentSpeed)
        checkHeartCollision()

        // UPDATE BERRIES
        val berryIterator = berries.iterator()
        while (berryIterator.hasNext()) {
            val berry = berryIterator.next()
            berry.setPosition(berry.x, berry.y + currentSpeed)
            if (checkBerryCollision(berry) || berry.y > canvasHeight + 100) {
                berryIterator.remove()
            }
        }

        // UPDATE ROCKS
        val rockIterator = rocks.iterator()
        while (rockIterator.hasNext()) {
            val rock = rockIterator.next()
            val rockSpeed = baseSpeed * level
            rock.setPosition(rock.x, rock.y + rockSpeed)
            if (checkRockCollision(rock) || rock.y > canvasHeight + 100) {
                rockIterator.remove()
            }
        }
    }

    private fun getRandomBerryType(): Int {
        val probabilities = doubleArrayOf(0.60, 0.20, 0.10, 0.05, 0.05)
        val rand = Math.random()
        var cumulative = 0.0
        for (i in probabilities.indices) {
            cumulative += probabilities[i]
            if (rand <= cumulative) return i
        }
        return 0
    }

    private fun spawnObjects() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastSpawnTime > getSpawnInterval()) {
            lastSpawnTime = currentTime
            val chance = (1..100).random()
            when {
                chance <= 1 -> {
                    heart.setPosition((0..canvasWidth).random(), -100)
                }
                chance <= 50 -> {
                    if (berries.size < 6) {
                        val berryType = getRandomBerryType()
                        berries.add(Berry((0..canvasWidth).random(), -100, berryType, context.resources))
                    }
                }
                else -> {
                    if (rocks.size < 6) {
                        rocks.add(Rock((0..canvasWidth).random(), -100, context))
                    }
                }
            }
        }
    }

    private fun getSpawnInterval(): Long {
        return when (level) {
            1 -> 500L
            2 -> 800L
            3 -> 1200L
            else -> 800L
        }
    }

    private fun checkBerryCollision(berry: Berry): Boolean {
        if (RectF.intersects(pikachu.rect, berry.rect)) {
            val points = intArrayOf(1, 2, 3, 5, 10)
            score += points[berry.type]
            listener?.onBerryCollision()
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
        if (heart.y > canvasHeight) {
            heart.x = (0..canvasWidth).random()
            heart.y = (-17500..-12500).random()
            return
        }
        if (RectF.intersects(pikachu.rect, heart.rect)) {
            heart.x = (0..canvasWidth).random()
            heart.y = (-17500..-12500).random()
            if (lives < 3) lives++
            listener?.onHeartCollision()
        }
    }

    fun movePikachu(x: Float) {
        pikachu.updatePosition(x.toInt(), canvasWidth)
    }
}