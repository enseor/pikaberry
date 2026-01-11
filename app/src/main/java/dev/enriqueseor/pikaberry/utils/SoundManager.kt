package dev.enriqueseor.pikaberry.utils

import android.content.Context
import android.media.SoundPool
import dev.enriqueseor.pikaberry.R

class SoundManager(context: Context) {
    private val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(5).build()
    private val soundMap: Map<String, Int>

    init {
        soundMap = mapOf(
            "rock" to soundPool.load(context, R.raw.geodude, 1),
            "heart" to soundPool.load(context, R.raw.heart, 1),
            "berry" to soundPool.load(context, R.raw.berry, 1)
        )
    }

    fun playRockSound() = play("rock")
    fun playHeartSound() = play("heart")
    fun playBerrySound() = play("berry")

    private fun play(key: String) {
        soundMap[key]?.let { id ->
            soundPool.play(id, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool.release()
    }
}