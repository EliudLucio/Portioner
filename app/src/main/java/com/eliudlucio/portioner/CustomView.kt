// CustomView.kt
package com.eliudlucio.portioner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var rectColor: Int = Color.BLUE
    var rectLeft: Float = 100f
    var rectTop: Float = 100f
    var rectRight: Float = 500f
    var rectBottom: Float = 300f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint().apply {
            style = Paint.Style.STROKE
            color = rectColor
        }

        canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, paint)
    }
}
