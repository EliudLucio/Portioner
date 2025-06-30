package com.eliudlucio.portioner

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class ResultsActivity : AppCompatActivity() {

    private lateinit var layInputs: LinearLayout
    private lateinit var layResults: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)


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

    //TODO: Unidades de medida

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
        addDataLine(layInputs, getString(R.string.area_object), "$objectArea cm²")
        addDataLine(layInputs, getString(R.string.input_obj_dimensions), "${length}x${width} cm")
        addDataLine(layInputs, getString(R.string.input_piece_quantity), portions.toString())

        // Outputs
        addLine(layResults, "$portions ${getString(R.string.data_resume)} ${roundData(bestWidth)} x ${roundData(bestHeight)}")

        addDataLine(layResults, getString(R.string.res_width), "${roundData(bestWidth)} cm")
        addDataLine(layResults, getString(R.string.res_height), "${roundData(bestHeight)} cm")
        addDataLine(layResults, getString(R.string.res_area), "${roundData(portionArea)} cm²")

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
        addDataLine(layInputs, getString(R.string.input_obj_dimensions), "${length}x${width} cm")
        addDataLine(layInputs, getString(R.string.input_piece_dimensions), "${portionLength}x${portionWidth} cm")

        // Outputs
        addLine(layResults, "$totalPortions ${getString(R.string.data_resume)} ${roundData(portionLength)} x ${roundData(portionWidth)}")
        addDataLine(layResults, getString(R.string.input_piece_quantity), totalPortions.toString())
        addDataLine(layResults, getString(R.string.res_used_area), "${roundData(totalUsedArea)} cm² (${usedPercent.roundToInt()}%)")
        addDataLine(layResults, getString(R.string.res_mat_waste), "${roundData(wasteArea)} cm² (${wastePercent.roundToInt()}%)")
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
        addDataLine(layInputs, getString(R.string.input_piece_dimensions), "${portionLength}x${portionWidth} cm")
        addDataLine(layInputs, getString(R.string.input_piece_quantity), portions.toString())

        // Outputs
        addDataLine(layResults, getString(R.string.res_requiered_area), "${roundData(portionLength * portionWidth * portions)} cm²")
        addDataLine(layResults, getString(R.string.input_obj_dimensions), "${roundData(bestLength)}x${roundData(bestWidth)} cm")


        //addLine(layResults, getString(R.string.res_min_obj_dimen), "Al menos ${portionLength}x${portionWidth * portions} cm o equivalente")
    }

    private fun showProportionalCut(length: Double?, width: Double?, percentage: Int) {
        if (length == null || width == null) return

        val area = length * width
        val cutOrientation = if (length >= width) "horizontal" else "vertical"
        val cutDistance = if (cutOrientation == "horizontal") length * (percentage / 100.0) else width * (percentage / 100.0)

        val portionArea = area * (percentage / 100.0)
        val remainingArea = area - portionArea

        val portionSize = if (cutOrientation == "horizontal") "${roundData(cutDistance)}x${roundData(width)} cm"
        else "${roundData(length)} x ${roundData(cutDistance)} cm"

        val remainingSize = if (cutOrientation == "horizontal") "${roundData(length - cutDistance)}x${roundData(width)} cm"
        else "$length x ${width - cutDistance} cm"

        // Inputs
        addDataLine(layInputs, getString(R.string.input_cut_type), getString(R.string.proportional_cut))
        addDataLine(layInputs, getString(R.string.input_obj_dimensions), "${length}x${width} cm")
        addDataLine(layInputs, getString(R.string.res_percentage), "$percentage%")

        // Outputs
        //TODO: Resumen
        addDataLine(layResults, getString(R.string.res_piece1), "$portionSize (${portionArea.roundToInt()} cm²)")
        addDataLine(layResults, getString(R.string.res_piece2), "$remainingSize (${remainingArea.roundToInt()} cm²)")
    }
}
