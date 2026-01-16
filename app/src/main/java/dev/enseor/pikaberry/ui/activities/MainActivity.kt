package dev.enseor.pikaberry.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import dev.enseor.pikaberry.R

class MainActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var rbMedium: RadioButton
    private lateinit var rbHard: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        rbMedium = findViewById(R.id.rbMedium)
        rbHard = findViewById(R.id.rbHard)

        findViewById<Button>(R.id.btnInfo).setOnClickListener { info() }
        findViewById<Button>(R.id.btnPlay).setOnClickListener { play() }
        findViewById<Button>(R.id.btnRanking).setOnClickListener { ranking() }
    }

    private fun info() {
        startActivity(Intent(this, InfoActivity::class.java))
    }

    private fun play() {
        val (levelNumber, levelName) = when {
            rbMedium.isChecked -> 2 to "MEDIUM"
            rbHard.isChecked -> 3 to "HARD"
            else -> 1 to "EASY"
        }

        val playerName = etName.text.toString().ifEmpty { "PLAYER" }

        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("levelNumber", levelNumber)
            putExtra("levelName", levelName)
            putExtra("playerName", playerName)
        }
        startActivity(intent)
    }

    private fun ranking() {
        startActivity(Intent(this, RankingActivity::class.java))
    }
}