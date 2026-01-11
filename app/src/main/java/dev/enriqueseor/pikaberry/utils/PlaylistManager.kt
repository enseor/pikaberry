package dev.enriqueseor.pikaberry.utils

import android.content.Context
import android.content.res.Configuration
import android.media.MediaPlayer
import dev.enriqueseor.pikaberry.R

data class SongBackgroundPair(val songResId: Int, val backgroundResId: Int)

class PlaylistManager(context: Context) : MediaPlayer.OnCompletionListener {

    private var playlist: Array<MediaPlayer?> = arrayOfNulls(0)
    private var pairs: List<SongBackgroundPair> = listOf()
    private var currentSongIndex = 0
    private var onSongChangeListener: ((SongBackgroundPair) -> Unit)? = null

    init {
        val orientation = context.resources.configuration.orientation

        pairs = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            listOf(
                SongBackgroundPair(R.raw.route_101, R.drawable.route_102_landscape),
                SongBackgroundPair(R.raw.route_104, R.drawable.route_116_landscape),
                SongBackgroundPair(R.raw.route_110, R.drawable.route_117_landscape),
                SongBackgroundPair(R.raw.route_113, R.drawable.route_113_landscape),
                SongBackgroundPair(R.raw.route_119, R.drawable.route_118_landscape),
                SongBackgroundPair(R.raw.route_120, R.drawable.route_121_landscape),
                SongBackgroundPair(R.raw.route_123, R.drawable.route_123_landscape)
            )
        } else {
            listOf(
                SongBackgroundPair(R.raw.route_101, R.drawable.route_101_portrait),
                SongBackgroundPair(R.raw.route_104, R.drawable.route_104_portrait),
                SongBackgroundPair(R.raw.route_110, R.drawable.route_110_portrait),
                SongBackgroundPair(R.raw.route_111, R.drawable.route_111_portrait),
                SongBackgroundPair(R.raw.route_119, R.drawable.route_119_portrait),
                SongBackgroundPair(R.raw.route_120, R.drawable.route_120_portrait)
            )
        }.shuffled()

        playlist = arrayOfNulls(pairs.size)
        for (i in pairs.indices) {
            playlist[i] = MediaPlayer.create(context, pairs[i].songResId).apply {
                setVolume(0.5f, 0.5f)
                setOnCompletionListener(this@PlaylistManager)
            }
        }
    }

    fun setOnSongChangeListener(listener: (SongBackgroundPair) -> Unit) {
        this.onSongChangeListener = listener
        if (pairs.isNotEmpty()) listener.invoke(pairs[currentSongIndex])
    }

    fun start() { playlist.getOrNull(currentSongIndex)?.start() }
    fun pause() { playlist.getOrNull(currentSongIndex)?.let { if (it.isPlaying) it.pause() } }
    fun release() { playlist.forEach { it?.release() } }

    override fun onCompletion(mp: MediaPlayer) {
        currentSongIndex = (currentSongIndex + 1) % playlist.size
        start()
        onSongChangeListener?.invoke(pairs[currentSongIndex])
    }
}