package dev.enseor.pikaberry.ui.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.enseor.pikaberry.R
import dev.enseor.pikaberry.data.database.PlayerDatabaseHelper
import dev.enseor.pikaberry.ui.adapters.ScoreAdapter

class ResultsActivity : AppCompatActivity() {
    private lateinit var dbHelper: PlayerDatabaseHelper
    private var isScoreSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        dbHelper = PlayerDatabaseHelper(this)

        isScoreSaved = savedInstanceState?.getBoolean("isScoreSaved", false) ?: false

        val levelName = intent.getStringExtra("levelName") ?: "EASY"
        val playerName = intent.getStringExtra("playerName") ?: "PLAYER"
        val playerScore = intent.getIntExtra("playerScore", 0)

        setupTextView(playerName, playerScore, levelName)
        setupRecyclerView(levelName)

        if (!isScoreSaved) {
            saveScore(playerName, playerScore, levelName)
        }
    }

    private fun setupTextView(name: String, score: Int, level: String) {
        val resultTextView: TextView = findViewById(R.id.resultTextView)
        resultTextView.text = """
            PLAYER: $name
            SCORE: $score
            LEVEL: $level
        """.trimIndent()
    }

    private fun setupRecyclerView(levelName: String) {
        val scoreRecyclerView: RecyclerView = findViewById(R.id.scoreRecyclerView)
        scoreRecyclerView.layoutManager = LinearLayoutManager(this)

        val scores = dbHelper.getScoresByLevel(levelName)
        scoreRecyclerView.adapter = ScoreAdapter(scores)
    }

    private fun saveScore(name: String, score: Int, level: String) {
        dbHelper.addScore(name, score, level)
        isScoreSaved = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isScoreSaved", isScoreSaved)
    }
}