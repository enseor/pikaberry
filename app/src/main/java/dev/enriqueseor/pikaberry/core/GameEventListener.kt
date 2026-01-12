package dev.enriqueseor.pikaberry.core

interface GameEventListener {
    fun onBerryCollision()
    fun onRockCollision()
    fun onHeartCollision()
    fun onScoreUpdated(newScore: Int)
    fun onTick()
}