package com.eliudlucio.portioner

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.eliudlucio.portioner.LocaleHelper.setLocale
import kotlin.math.roundToInt



class ResultsActivity : AppCompatActivity() {

    private lateinit var layInputs: LinearLayout
    private lateinit var layResults: LinearLayout
    private lateinit var suffix: String

    override fun attachBaseContext(newBase: Context?) {
        val base = requireNotNull(newBase)
        val prefs = PreferenceManager.getDefaultSharedPreferences(base)
        val lang = prefs.getString("preferred_language", "es") ?: "es"
        val localized = setLocale(base, lang)

        super.attachBaseContext(localized)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val unitPref = prefs.getString("preferred_unit", "cm") ?: "cm"

        suffix = when (unitPref) {
            "cm" -> "cm"
            "mm" -> "mm"
            "in" -> "in"
            else -> ""
        }

        layInputs = findViewById(R.id.lay_inputs)
        layResults = findViewById(R.id.lay_results)

        // Recibir datos
        val cutType = intent.getStringExtra("CUT_TYPE")
        val length = intent.getStringExtra("LENGTH")?.toDoubleOrNull()
        val width = intent.getStringExtra("WIDTH")?.toDoubleOrNull()
        val portionLength = intent.getStringExtra("PORTION_LENGTH")?.toDoubleOrNull()
        val portionWidth = intent.getStringExtra("PORTION_WIDTH")?.toDoubleOrNull()
        val portions = intent.getIntExtra("PORTIONS", 1)
        val percentage = intent.getIntExtra("PERCENTAGE", 50)

        when (cutType) {
            "PRECISE_CUT" -> showPreciseCut(length, width, portions)
            "DEFINED_CUT" -> showDefinedCut(length, width, portionLength, portionWidth)
            "REVERSE_CUT" -> showReverseCut(portionLength, portionWidth, portions)
            "PROPORTIONAL_CUT" -> showProportionalCut(length, width, percentage)
        }

        // Establecer la Toolbar como el App Bar
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val canvas = findViewById<CustomView>(R.id.canvas)
        canvas.objectWidth  = length?.toFloat() ?: 0f
        canvas.objectHeight = width?.toFloat()  ?: 0f

        val rects = when (cutType) {
            "PRECISE_CUT"     -> calculatePreciseCutRects(length!!, width!!, portions)
            "DEFINED_CUT"     -> calculateDefinedCutRects(length!!, width!!, portionLength!!, portionWidth!!)
            "REVERSE_CUT"     -> calculateReverseCutRects(portionLength!!, portionWidth!!, portions)
            "PROPORTIONAL_CUT"-> calculateProportionalCutRects(length!!, width!!, percentage)
            else              -> emptyList()
        }

        canvas.rects = rects
        val maxRight  = rects.maxOfOrNull { it.right  } ?: 0f
        val maxBottom = rects.maxOfOrNull { it.bottom } ?: 0f

        canvas.objectWidth  = maxRight
        canvas.objectHeight = maxBottom
        canvas.rects        = rects
        canvas.invalidate()
        canvas.invalidate()



    }

    private fun addDataLine(layout: LinearLayout, label: String, value: String) {
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.item_data_line, layout, false)

        val tvLabel = view.findViewById<TextView>(R.id.tv_label)
        val tvValue = view.findViewById<TextView>(R.id.tv_value)

        tvLabel.text = label
        tvValue.text = value

        layout.addView(view)
    }


    private fun addLine(layout: LinearLayout, value: String) {
        val textView = TextView(this)
        textView.text = value
        textView.textSize = 17f
        textView.setTextColor(getColor(R.color.black))
        textView.setPadding(0, 8, 0, 8)
        textView.gravity = Gravity.CENTER
        layout.addView(textView)
    }


    private fun roundData(value : Double): Double {
        val roundedData = Math.round(value * 100.0) / 100.0
        return roundedData;
    }

    private fun showPreciseCut(length: Double?, width: Double?, portions: Int) {
        if (length == null || width == null || portions <= 0) return

        // Para buscar la combinación más cuadrada posible
        var bestWidth = 0.0
        var bestHeight = 0.0
        var minAspectRatioDiff = Double.MAX_VALUE

        for (rows in 1..portions) {
            if (portions % rows == 0) {
                val cols = portions / rows
                val portionWidth = width / cols
                val portionHeight = length / rows
                val aspectRatioDiff = kotlin.math.abs(portionWidth - portionHeight)

                if (aspectRatioDiff < minAspectRatioDiff) {
                    minAspectRatioDiff = aspectRatioDiff
                    bestWidth = portionWidth
                    bestHeight = portionHeight
                }
            }
        }

        val objectArea = length * width
        val portionArea = objectArea / portions


        // Inputs
        addDataLine(layInputs, getString(R.string.input_cut_type), getString(R.string.precise_cut))
        addDataLine(layInputs, getString(R.string.area_object), "$objectArea $suffix²")
        addDataLine(layInputs, getString(R.string.input_obj_dimensions), "${length}x${width} $suffix")
        addDataLine(layInputs, getString(R.string.input_piece_quantity), portions.toString())

        // Outputs
        addLine(layResults, "$portions ${getString(R.string.data_resume)} ${roundData(bestWidth)} x ${roundData(bestHeight)} $suffix")

        addDataLine(layResults, getString(R.string.res_width), "${roundData(bestWidth)} $suffix")
        addDataLine(layResults, getString(R.string.res_height), "${roundData(bestHeight)} $suffix")
        addDataLine(layResults, getString(R.string.res_area), "${roundData(portionArea)} $suffix²")

    }

    private fun showDefinedCut(length: Double?, width: Double?, portionLength: Double?, portionWidth: Double?) {
        if (length == null || width == null || portionLength == null || portionWidth == null) return

        val fitRows = (length / portionLength).toInt()
        val fitCols = (width / portionWidth).toInt()
        val totalPortions = fitRows * fitCols

        val totalUsedArea = totalPortions * portionLength * portionWidth
        val totalArea = length * width
        val wasteArea = totalArea - totalUsedArea

        val usedPercent = (totalUsedArea / totalArea) * 100
        val wastePercent = 100 - usedPercent

        // Inputs
        addDataLine(layInputs, getString(R.string.input_cut_type), getString(R.string.defined_cut))
        addDataLine(layInputs, getString(R.string.input_obj_dimensions), "${length}x${width} $suffix")
        addDataLine(layInputs, getString(R.string.input_piece_dimensions), "${portionLength}x${portionWidth} $suffix")

        // Outputs
        addLine(layResults, "$totalPortions ${getString(R.string.data_resume)} ${roundData(portionLength)} x ${roundData(portionWidth)} $suffix")
        addDataLine(layResults, getString(R.string.input_piece_quantity), totalPortions.toString())
        addDataLine(layResults, getString(R.string.res_used_area), "${roundData(totalUsedArea)} $suffix² (${usedPercent.roundToInt()}%)")
        addDataLine(layResults, getString(R.string.res_mat_waste), "${roundData(wasteArea)} $suffix² (${wastePercent.roundToInt()}%)")
    }

    private fun showReverseCut(portionLength: Double?, portionWidth: Double?, portions: Int) {
        if (portionLength == null || portionWidth == null || portions <= 0) return

        var bestLength = 0.0
        var bestWidth = 0.0
        var minAspectRatioDiff = Double.MAX_VALUE

        for (rows in 1..portions) {
            if (portions % rows == 0) {
                val cols = portions / rows
                val tempLength = portionLength * rows
                val tempWidth = portionWidth * cols
                val diff = kotlin.math.abs(tempLength - tempWidth)

                if (diff < minAspectRatioDiff) {
                    minAspectRatioDiff = diff
                    bestLength = tempLength
                    bestWidth = tempWidth
                }
            }
        }


        // Inputs
        addDataLine(layInputs, getString(R.string.input_cut_type), getString(R.string.reverse_cut))
        addDataLine(layInputs, getString(R.string.input_piece_dimensions), "$portionLength x $portionWidth $suffix")
        addDataLine(layInputs, getString(R.string.input_piece_quantity), portions.toString())

        // Outputs
        addDataLine(layResults, getString(R.string.res_requiered_area), "${roundData(portionLength * portionWidth * portions)} $suffix²")
        addDataLine(layResults, getString(R.string.input_obj_dimensions), "${roundData(bestLength)} x ${roundData(bestWidth)} $suffix")


        //addLine(layResults, getString(R.string.res_min_obj_dimen), "Al menos ${portionLength}x${portionWidth * portions} cm o equivalente")
    }

    private fun showProportionalCut(length: Double?, width: Double?, percentage: Int) {
        if (length == null || width == null) return

        val area = length * width
        val cutOrientation = if (length >= width) "horizontal" else "vertical"
        val cutDistance = if (cutOrientation == "horizontal") length * (percentage / 100.0) else width * (percentage / 100.0)

        val portionArea = area * (percentage / 100.0)
        val remainingArea = area - portionArea

        val portionSize = if (cutOrientation == "horizontal") "${roundData(cutDistance)} x ${roundData(width)} $suffix"
        else "${roundData(length)} x ${roundData(cutDistance)} $suffix"

        val remainingSize = if (cutOrientation == "horizontal") "${roundData(length - cutDistance)} x ${roundData(width)} $suffix"
        else "${roundData(length)} x ${roundData(width - cutDistance)} $suffix"

        // Inputs
        addDataLine(layInputs, getString(R.string.input_cut_type), getString(R.string.proportional_cut))
        addDataLine(layInputs, getString(R.string.input_obj_dimensions), "$length x $width $suffix")
        addDataLine(layInputs, getString(R.string.res_percentage), "$percentage%")

        // Outputs
        addDataLine(layResults, getString(R.string.res_piece1), "$portionSize (${portionArea.roundToInt()} $suffix²)")
        addDataLine(layResults, getString(R.string.res_piece2), "$remainingSize (${remainingArea.roundToInt()} $suffix²)")
    }

    private fun calculatePreciseCutRects(
        length: Double, width: Double, portions: Int
    ): List<RectData> {
        //Encuentra la mejor división en filas x columnas
        var bestRows = 1
        var bestCols = portions
        var minDiff = Double.MAX_VALUE

        for (rows in 1..portions) {
            if (portions % rows == 0) {
                val cols = portions / rows
                val pw = width / cols
                val ph = length / rows
                val diff = kotlin.math.abs(pw - ph)
                if (diff < minDiff) {
                    minDiff = diff
                    bestRows = rows
                    bestCols = cols
                }
            }
        }

        //Calcula tamaño de cada pieza
        val pieceW = width / bestCols
        val pieceH = length / bestRows

        //Genera lista de rects
        val rects = mutableListOf<RectData>()
        for (r in 0 until bestRows) {
            for (c in 0 until bestCols) {
                val left = (c * pieceW).toFloat()
                val top = (r * pieceH).toFloat()
                rects += RectData(
                    left = left,
                    top = top,
                    right = left + pieceW.toFloat(),
                    bottom = top + pieceH.toFloat()
                )
            }
        }
        return rects
    }

    private fun calculateDefinedCutRects(
        length: Double, width: Double,
        pieceLength: Double, pieceWidth: Double
    ): List<RectData> {

        val all = mutableListOf(
            RectData(0f, 0f, width.toFloat(), length.toFloat(),
                color = Color.LTGRAY)
        )

        val rows = (length / pieceLength).toInt()
        val cols = (width  / pieceWidth ).toInt()
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val l = c * pieceWidth
                val t = r * pieceLength
                all += RectData(
                    left   = l.toFloat(),
                    top    = t.toFloat(),
                    right  = (l + pieceWidth).toFloat(),
                    bottom = (t + pieceLength).toFloat(),
                    color  = Color.BLUE
                )
            }
        }

        return all

    }

    private fun calculateReverseCutRects(
        pieceLength: Double, pieceWidth: Double, portions: Int
    ): List<RectData> {
        // 1. Busca mejor rows×cols como en Precise
        var bestRows = 1; var bestCols = portions; var minDiff = Double.MAX_VALUE
        for (rows in 1..portions) {
            if (portions % rows == 0) {
                val cols = portions / rows
                val totalL = pieceLength * rows
                val totalW = pieceWidth  * cols
                val diff = kotlin.math.abs(totalL - totalW)
                if (diff < minDiff) {
                    minDiff = diff
                    bestRows = rows
                    bestCols = cols
                }
            }
        }

        // 2. Genera posiciones ignorando el “objeto” original
        return (0 until bestRows).flatMap { r ->
            (0 until bestCols).map { c ->
                val left = (c * pieceWidth).toFloat()
                val top  = (r * pieceLength).toFloat()
                RectData(
                    left  = left,
                    top   = top,
                    right = left + pieceWidth.toFloat(),
                    bottom= top  + pieceLength.toFloat()
                )
            }
        }
    }

    private fun calculateProportionalCutRects(
        length: Double, width: Double, percentage: Int
    ): List<RectData> {
        val ratio = percentage / 100.0
        return if (length >= width) {
            // corte horizontal
            val cutH = (length * ratio).toFloat()
            listOf(
                // pieza 1 arriba
                RectData(0f, 0f, width.toFloat(), cutH),
                // resto abajo
                RectData(0f, cutH, width.toFloat(), length.toFloat())
            )
        } else {
            // corte vertical
            val cutW = (width * ratio).toFloat()
            listOf(
                // pieza 1 izquierda
                RectData(0f, 0f, cutW, length.toFloat()),
                // resto derecha
                RectData(cutW, 0f, width.toFloat(), length.toFloat())
            )
        }
    }





}


