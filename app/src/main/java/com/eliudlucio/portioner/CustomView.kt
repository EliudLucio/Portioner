package com.eliudlucio.portioner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.eliudlucio.portioner.RectData

class CustomView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    // Propiedades que configuramos desde la Activity
    private var scale   = 1f
    private var offsetX = 0f
    private var offsetY = 0f
    var objectWidth  = 0f
    var objectHeight = 0f
    var rects: List<RectData> = emptyList()

    // Variables internas de escalado
    private var scaleX = 1f
    private var scaleY = 1f

    // Paints
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = 0xFF000000.toInt()
    }

    // Calcula la escala cuando cambia el tamaño de la vista
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (objectWidth > 0 && objectHeight > 0) {
            val sx = w  / objectWidth
            val sy = h  / objectHeight
            scale = minOf(sx, sy)

            // espaciado para centrar
            offsetX = (w  - objectWidth  * scale) / 2f
            offsetY = (h  - objectHeight * scale) / 2f
        }
    }

    // Dibuja cada RectData convertido a píxeles
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(0xFFFFFFFF.toInt())

        // aplica offset y escala uniforme
        canvas.save()
        canvas.translate(offsetX, offsetY)

        rects.forEach { rd ->
            fillPaint.color = rd.color
            val l = rd.left   * scale
            val t = rd.top    * scale
            val r = rd.right  * scale
            val b = rd.bottom * scale
            canvas.drawRect(l, t, r, b, fillPaint)
            canvas.drawRect(l, t, r, b, strokePaint)
        }
        canvas.restore()
    }
}
