package com.eliudlucio.portioner

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class ResultsActivity : AppCompatActivity() {

    private lateinit var layInputs: LinearLayout
    private lateinit var layResults: LinearLayout
    private lateinit var tvCutInstruction: TextView

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

    private fun addLine(layout: LinearLayout, label: String, value: String) {
        val textView = TextView(this)
        textView.text = "$label: $value"
        layout.addView(textView)
    }

    //TODO: Unidades de medida

    private fun showPreciseCut(length: Double?, width: Double?, portions: Int) {
        if (length == null || width == null || portions <= 0) return

        val objectArea = length * width
        val portionArea = objectArea / portions
        val portionWidth = width / (portions / 2)
        val portionHeight = length / (portions / 2)

        // Inputs
        addLine(layInputs, getString(R.string.input_cut_type), getString(R.string.precise_cut))
        addLine(layInputs, getString(R.string.area_object), "$objectArea cm²")
        addLine(layInputs, getString(R.string.input_obj_dimensions), "${length}x${width} cm")
        addLine(layInputs, getString(R.string.input_piece_quantity), portions.toString())

        // Outputs
        addLine(layResults, getString(R.string.res_width), "$portionWidth cm")
        addLine(layResults, getString(R.string.res_height), "$portionHeight cm")
        addLine(layResults, getString(R.string.res_area), "$portionArea cm²")
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
        addLine(layInputs, getString(R.string.input_cut_type), getString(R.string.defined_cut))
        addLine(layInputs, getString(R.string.input_obj_dimensions), "${length}x${width} cm")
        addLine(layInputs, getString(R.string.input_piece_dimensions), "${portionLength}x${portionWidth} cm")

        // Outputs
        addLine(layResults, getString(R.string.input_piece_quantity), totalPortions.toString())
        addLine(layResults, getString(R.string.res_used_area), "$totalUsedArea cm² (${usedPercent.roundToInt()}%)")
        addLine(layResults, getString(R.string.res_mat_waste), "$wasteArea cm² (${wastePercent.roundToInt()}%)")
    }

    private fun showReverseCut(portionLength: Double?, portionWidth: Double?, portions: Int) {
        if (portionLength == null || portionWidth == null || portions <= 0) return

        val totalAreaRequiered = portionLength * portionWidth * portions
        val length = portionLength * (portions / 2)
        val width = portionWidth * (portions / 2)

        // Inputs
        addLine(layInputs, getString(R.string.input_cut_type), getString(R.string.reverse_cut))
        addLine(layInputs, getString(R.string.input_piece_dimensions), "${portionLength}x${portionWidth} cm")
        addLine(layInputs, getString(R.string.input_piece_quantity), portions.toString())

        // Outputs
        addLine(layResults, getString(R.string.res_requiered_area), "$totalAreaRequiered cm²")
        addLine(layResults, getString(R.string.input_obj_dimensions), "${length}x${width} cm")

        //addLine(layResults, getString(R.string.res_min_obj_dimen), "Al menos ${portionLength}x${portionWidth * portions} cm o equivalente")
    }

    private fun showProportionalCut(length: Double?, width: Double?, percentage: Int) {
        if (length == null || width == null) return

        val area = length * width
        val cutOrientation = if (length >= width) "horizontal" else "vertical"
        val cutDistance = if (cutOrientation == "horizontal") length * (percentage / 100.0) else width * (percentage / 100.0)

        val portionArea = area * (percentage / 100.0)
        val remainingArea = area - portionArea

        val portionSize = if (cutOrientation == "horizontal") "${cutDistance}x$width cm"
        else "$length x ${cutDistance} cm"

        val remainingSize = if (cutOrientation == "horizontal") "${length - cutDistance}x$width cm"
        else "$length x ${width - cutDistance} cm"

        // Inputs
        addLine(layInputs, getString(R.string.input_cut_type), getString(R.string.proportional_cut))
        addLine(layInputs, getString(R.string.input_obj_dimensions), "${length}x${width} cm")
        addLine(layInputs, getString(R.string.res_percentage), "$percentage%")

        // Outputs
        addLine(layResults, getString(R.string.res_piece1), "$portionSize (${portionArea.roundToInt()} cm²)")
        addLine(layResults, getString(R.string.res_piece2), "$remainingSize (${remainingArea.roundToInt()} cm²)")
    }
}
