package com.eliudlucio.portioner

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class ResultsActivity : AppCompatActivity() {

    lateinit var layInputs: LinearLayout
    lateinit var layResults: LinearLayout
    lateinit var tvCutInstruction: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        layInputs = findViewById(R.id.lay_inputs)
        layResults = findViewById(R.id.lay_results)
        tvCutInstruction = findViewById(R.id.tv_cut_instruction)

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

    private fun showPreciseCut(length: Double?, width: Double?, portions: Int) {
        if (length == null || width == null || portions <= 0) return

        val portionArea = (length * width) / portions
        val portionWidth = width / portions
        val portionHeight = length / portions

        addLine(layInputs, getString(R.string.input_cut_type), "Corte Exacto")
        addLine(layInputs, getString(R.string.input_obj_dimensions), "${length}x${width} cm")
        addLine(layInputs, getString(R.string.input_piece_quantity), portions.toString())

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

        addLine(layInputs, getString(R.string.input_cut_type), "Corte a Medida")
        addLine(layInputs, getString(R.string.input_obj_dimensions), "${length}x${width} cm")
        addLine(layInputs, getString(R.string.input_piece_dimensions), "${portionLength}x${portionWidth} cm")

        addLine(layResults, getString(R.string.input_piece_quantity), totalPortions.toString())
        addLine(layResults, getString(R.string.res_used_area), "$totalUsedArea cm² (${usedPercent.roundToInt()}%)")
        addLine(layResults, getString(R.string.res_mat_waste), "$wasteArea cm² (${wastePercent.roundToInt()}%)")
    }

    private fun showReverseCut(portionLength: Double?, portionWidth: Double?, portions: Int) {
        if (portionLength == null || portionWidth == null || portions <= 0) return

        val totalArea = portionLength * portionWidth * portions

        addLine(layInputs, getString(R.string.input_cut_type), "Corte Inverso")
        addLine(layInputs, getString(R.string.input_piece_dimensions), "${portionLength}x${portionWidth} cm")
        addLine(layInputs, getString(R.string.input_piece_quantity), portions.toString())

        addLine(layResults, getString(R.string.res_requiered_area), "$totalArea cm²")
        addLine(layResults, getString(R.string.res_min_obj_dimen), "Al menos ${portionLength}x${portionWidth * portions} cm o equivalente")
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

        addLine(layInputs, getString(R.string.input_cut_type), "Corte por Porcentaje")
        addLine(layInputs, getString(R.string.input_obj_dimensions), "${length}x${width} cm")
        addLine(layInputs, getString(R.string.res_percentage), "$percentage%")

        addLine(layResults, getString(R.string.res_piece1), "$portionSize (${portionArea.roundToInt()} cm²)")
        addLine(layResults, getString(R.string.res_piece2), "$remainingSize (${remainingArea.roundToInt()} cm²)")

        tvCutInstruction.text = getString(R.string.res_instruction, cutDistance.roundToInt(), cutOrientation)
    }
}
