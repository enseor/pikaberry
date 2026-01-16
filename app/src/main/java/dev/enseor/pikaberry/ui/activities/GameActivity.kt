package dev.enseor.pikaberry.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import dev.enseor.pikaberry.R
import dev.enseor.pikaberry.core.GameEngine
import dev.enseor.pikaberry.utils.PlaylistManager
import dev.enseor.pikaberry.ui.components.GameCanvas
import dev.enseor.pikaberry.core.GameEventListener
import dev.enseor.pikaberry.utils.SoundManager

class GameActivity : AppCompatActivity(), GameEventListener {
    private var levelNumber = 1
    private var isGameFinished = false

    private lateinit var gameCanvas: GameCanvas
    private lateinit var gameEngine: GameEngine

    private lateinit var playerName: String
    private lateinit var levelName: String
    private lateinit var soundManager: SoundManager
    private var playlistManager: PlaylistManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        levelNumber = intent.getIntExtra("levelNumber", 1)
        playerName = intent.getStringExtra("playerName") ?: "PLAYER"
        levelName = intent.getStringExtra("levelName") ?: "EASY"

        gameCanvas = findViewById(R.id.Screen)
        gameEngine = GameEngine(this, this)
        gameEngine.level = levelNumber
        gameCanvas.engine = gameEngine

        soundManager = SoundManager(this)
        setupPlaylist()
    }

    override fun onTick() {
        gameCanvas.updateView()
    }

    override fun onBerryCollision() {
        soundManager.playBerrySound()
    }

    override fun onRockCollision() {
        soundManager.playRockSound()
        if (gameEngine.isGameOver()) onGameFinished()
    }

    override fun onHeartCollision() {
        soundManager.playHeartSound()
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
        soundManager.release()
    }

    private fun onGameFinished() {
        if (isGameFinished) return
        isGameFinished = true
        gameEngine.stop()

        Intent(this, ResultsActivity::class.java).apply {
            putExtra("playerScore", gameEngine.score)
            putExtra("levelName", levelName)
            startActivity(this)
        }
        finish()
    }

    private fun setupPlaylist() {
        playlistManager = PlaylistManager(this)
        val backgroundImageView = findViewById<ImageView>(R.id.backgroundImageView)
        playlistManager?.setOnSongChangeListener { data ->
            backgroundImageView.setImageResource(data.backgroundResId)
        }
        playlistManager?.start()
    }
}