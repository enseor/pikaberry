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
    private var playerName: String? = null
    private var playerScore = 0
    private lateinit var resultTextView: TextView
    private lateinit var dbHelper: PlayerDatabaseHelper
    private var isScoreSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        dbHelper = PlayerDatabaseHelper(this)

        resultTextView = findViewById(R.id.resultTextView)
        val scoreRecyclerView: RecyclerView = findViewById(R.id.scoreRecyclerView)

        scoreRecyclerView.layoutManager = LinearLayoutManager(this)

        val levelName = intent.getStringExtra("levelName") ?: "EASY"
        playerName = intent.getStringExtra("playerName") ?: "PLAYER"
        playerScore = intent.getIntExtra("playerScore", 0)

        if (savedInstanceState != null) {
            isScoreSaved = savedInstanceState.getBoolean("isScoreSaved", false)
        }

        yourScore(levelName)

        val scores = dbHelper.getScoresByLevel(levelName)
        scoreRecyclerView.adapter = ScoreAdapter(scores)
    }

    private fun yourScore(levelName: String) {
        val resultText = "PLAYER: $playerName\nSCORE: $playerScore\nLEVEL: $levelName"
        resultTextView.text = resultText
        if (!isScoreSaved) {
            dbHelper.addScore(playerName!!, playerScore, levelName)
            isScoreSaved = true
        }
    }
}