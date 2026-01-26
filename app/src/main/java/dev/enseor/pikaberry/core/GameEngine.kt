package dev.enseor.pikaberry.core

import android.content.Context
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import dev.enseor.pikaberry.data.model.*

class GameEngine(private val context: Context, private val listener: GameEventListener?) {

    var score = 0
    var lives = 3
    var level = 1
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
        val pikaSize = (width * 0.20f).toInt()
        val startX = (width / 2) - (pikaSize / 2)
        val startY = height - pikaSize - 50
        pikachu = Pikachu(startX, startY, pikaSize, context)
        berries.clear()
        rocks.clear()
        hearts.clear()
    }

    fun update() {
        spawnObjects()
        val currentSpeed = baseSpeed * level

        // UPDATE HEART
        val heartIterator = hearts.iterator()
        while (heartIterator.hasNext()) {
            val heart = heartIterator.next()
            heart.setPosition(heart.x, heart.y + currentSpeed)
            if (checkHeartCollision(heart) || heart.y > canvasHeight + 100) {
                heartIterator.remove()
            }
        }

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
        val probabilities = doubleArrayOf(0.45, 0.25, 0.15, 0.10, 0.5)
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

            val spriteSize = (canvasWidth * 0.20f).toInt()
            val randomX = (0..(canvasWidth - spriteSize)).random()
            val type = getRandomBerryType()
            val chance = (1..100).random()

            when {
                chance <= 1 -> hearts.add(Heart(randomX, -spriteSize, spriteSize, context))
                chance <= 50 -> berries.add(Berry(randomX, -spriteSize, type, spriteSize, context.resources))
                else -> rocks.add(Rock(randomX, -spriteSize, spriteSize, context))
            }
        }
    }

    private fun getSpawnInterval(): Long {
        return when (level) {
            1 -> 600L
            2 -> 400L
            3 -> 200L
            else -> 600L
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

    private fun checkHeartCollision(heart: Heart): Boolean {
        if (RectF.intersects(pikachu.rect, heart.rect)) {
            if (lives < 3) {
                lives++
            }
            listener?.onHeartCollision()
            return true
        }
        return false
    }

    fun movePikachu(x: Float) {
        pikachu.updatePosition(x.toInt(), canvasWidth)
    }
}