package dev.enriqueseor.pikaberry.data.model

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import dev.enriqueseor.pikaberry.R
import java.util.Random

class Berry(var x: Int, var y: Int, private val resources: Resources) {
    val rect = RectF()
    private var drawable: Drawable? = null
    var type: Int = customRandomBerryType()

    init {
        setDrawable()
    }

    fun customRandomBerryType(): Int {
        val probabilities = doubleArrayOf(0.60, 0.20, 0.10, 0.050, 0.025)
        val rand = Random().nextDouble()
        var cumulativeProbability = 0.0
        for (i in probabilities.indices) {
            cumulativeProbability += probabilities[i]
            if (rand <= cumulativeProbability) {
                return i
            }
        }
        return 0
    }

    fun setDrawable() {
        val berriesDrawable = arrayOf(
            ResourcesCompat.getDrawable(resources, R.drawable.razz_berry, null),
            ResourcesCompat.getDrawable(resources, R.drawable.pinap_berry, null),
            ResourcesCompat.getDrawable(resources, R.drawable.nanap_berry, null),
            ResourcesCompat.getDrawable(resources, R.drawable.pinap_berry_silver, null),
            ResourcesCompat.getDrawable(resources, R.drawable.razz_berry_golden, null)
        )
        drawable = berriesDrawable[type]
    }

    fun draw(canvas: Canvas, radius: Int) {
        drawable?.setBounds(
            x - radius,
            y - radius,
            x + radius,
            y + radius
        )
        drawable?.draw(canvas)

        rect.set(
            (x - radius).toFloat(),
            (y - radius).toFloat(),
            (x + radius).toFloat(),
            (y + radius).toFloat()
        )
    }

    fun updatePosition(canvasWidth: Int, canvasHeight: Int, baseSpeed: Int, level: Int) {
        y += baseSpeed * level
    }
}