package dev.enseor.pikaberry.core

interface GameEventListener {
    fun onBerryCollision()
    fun onRockCollision()
    fun onHeartCollision()
    fun onScoreUpdated(newScore: Int)
    fun onTick()
}