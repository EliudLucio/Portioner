package com.eliudlucio.portioner

import android.graphics.Color

data class RectData(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val color: Int = Color.rgb(0,100,160)
)

