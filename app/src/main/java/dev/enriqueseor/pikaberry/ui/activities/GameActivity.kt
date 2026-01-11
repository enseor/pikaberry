package dev.enriqueseor.pikaberry.ui.activities

import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import dev.enriqueseor.pikaberry.R
import dev.enriqueseor.pikaberry.core.GameEngine
import dev.enriqueseor.pikaberry.utils.PlaylistManager
import dev.enriqueseor.pikaberry.ui.components.GameCanvas
import dev.enriqueseor.pikaberry.core.GameEventListener

class GameActivity : AppCompatActivity(), GameEventListener {
    private var levelNumber = 2
    private var isGameFinished = false

    private lateinit var gameCanvas: GameCanvas
    private lateinit var gameEngine: GameEngine

    private lateinit var playerName: String
    private lateinit var soundPool: SoundPool
    private lateinit var soundMap: Map<Int, Int>

    private var playlistManager: PlaylistManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        levelNumber = intent.getIntExtra("levelNumber", 2)
        playerName = intent.getStringExtra("playerName") ?: "Unknown"

        gameCanvas = findViewById(R.id.Screen)
        gameEngine = GameEngine(this, this)
        gameEngine.level = levelNumber
        gameCanvas.engine = gameEngine

        initializeSoundPool()
        playlistManager()
    }

    private fun initializeSoundPool() {
        soundPool = SoundPool.Builder().setMaxStreams(5).build()
        soundMap = mapOf(
            R.raw.geodude to soundPool.load(this, R.raw.geodude, 2),
            R.raw.heart to soundPool.load(this, R.raw.heart, 3),
            R.raw.berry to soundPool.load(this, R.raw.berry, 1)
        )
    }

    private fun playSound(soundResource: Int) {
        soundMap[soundResource]?.let { soundId ->
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    override fun onTick() {
        gameCanvas.invalidate()
    }

    override fun onBerryCollected() {
        playSound(R.raw.berry)
    }

    override fun onRockCollision() {
        playSound(R.raw.geodude)
        if (gameEngine.isGameOver()) {
            onGameFinished()
        }
    }

    override fun onHeartCollected() {
        playSound(R.raw.heart)
    }

    override fun onScoreUpdated(newScore: Int) {}

    override fun onPause() {
        super.onPause()
        gameEngine.stop()
        playlistManager?.pause()
    }

    override fun onResume() {
        super.onResume()
        gameEngine.start()
        playlistManager?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        playlistManager?.release()
        soundPool.release()
    }

    private fun onGameFinished() {
        if (isGameFinished) return
        isGameFinished = true
        gameEngine.stop()

        Intent(this, ResultsActivity::class.java).apply {
            putExtra("playerScore", gameEngine.score)
            startActivity(this)
        }
        finish()
    }

    private fun playlistManager() {
        playlistManager = PlaylistManager(this)
        val backgroundImageView = findViewById<ImageView>(R.id.backgroundImageView)
        playlistManager?.setOnSongChangeListener { data ->
            backgroundImageView.setImageResource(data.backgroundResId)
        }
        playlistManager?.start()
    }
}