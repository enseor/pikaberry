package dev.enriqueseor.pikaberry.core

interface GameEventListener {
    fun onBerryCollected()
    fun onRockCollision()
    fun onHeartCollected()
    fun onScoreUpdated(newScore: Int)
}