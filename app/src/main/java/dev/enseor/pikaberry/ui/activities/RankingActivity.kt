package dev.enseor.pikaberry.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.enseor.pikaberry.R
import dev.enseor.pikaberry.data.database.PlayerDatabaseHelper
import dev.enseor.pikaberry.ui.adapters.ScoreAdapter

class RankingActivity : AppCompatActivity() {
    private lateinit var dbHelper: PlayerDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        dbHelper = PlayerDatabaseHelper(this)

        setupRecyclerView(R.id.easyRecyclerView, "EASY")
        setupRecyclerView(R.id.mediumRecyclerView, "MEDIUM")
        setupRecyclerView(R.id.hardRecyclerView, "HARD")
    }

    private fun setupRecyclerView(resId: Int, level: String) {
        val recyclerView: RecyclerView = findViewById(resId)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val scores = dbHelper.getScoresByLevel(level)
        recyclerView.adapter = ScoreAdapter(scores)
    }
}