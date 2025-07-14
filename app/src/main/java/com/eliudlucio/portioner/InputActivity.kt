package com.eliudlucio.portioner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class InputActivity : AppCompatActivity() {

    private var cutType: String? = null
    private lateinit var valueLength: EditText
    private lateinit var valueWidth: EditText
    private lateinit var valuePortionLength: EditText
    private lateinit var valuePortionWidth: EditText
    private lateinit var valuePortionSeekBar: SeekBar
    private lateinit var valuePercentageSeekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        // Recibimos el tipo de corte seleccionado
        cutType = intent.getStringExtra("CUT_TYPE")

        valueLength = findViewById(R.id.et_length)
        valueWidth = findViewById(R.id.et_width)
        valuePortionLength = findViewById(R.id.et_portion_length)
        valuePortionWidth = findViewById(R.id.et_portion_width)
        val layPortions = findViewById<LinearLayout>(R.id.lay_portions)
        val layPercentage = findViewById<LinearLayout>(R.id.lay_percentage)
        val tvDimenObj = findViewById<TextView>(R.id.tv_dimen_obj)
        val tvDimenPiece = findViewById<TextView>(R.id.tv_dimen_piece)

        valuePortionSeekBar = findViewById(R.id.portionSeekBar)
        valuePercentageSeekBar = findViewById(R.id.percentageSeekBar)

        when (cutType) {
            "PRECISE_CUT" -> setVisible(valueLength, valueWidth, layPortions, tvDimenObj)
            "DEFINED_CUT" -> setVisible(valueLength, valueWidth, valuePortionLength, valuePortionWidth, tvDimenObj, tvDimenPiece)
            "REVERSE_CUT" -> setVisible(valuePortionLength, valuePortionWidth, layPortions, tvDimenPiece)
            "PROPORTIONAL_CUT" -> setVisible(valueLength, valueWidth, layPercentage, tvDimenObj)
        }

        // Establecer la Toolbar como el App Bar
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }



        // Manejo del Portion Seekbar
        val tvValuePortion = findViewById<TextView>(R.id.tv_portions_number)
        tvValuePortion.text = "1"

        valuePortionSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvValuePortion.text = "${progress + 1}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Manejo del Percentage Seekbar
        val tvValuePercentage = findViewById<TextView>(R.id.tv_percentage_number)
        tvValuePercentage.text = "50%"
        valuePercentageSeekBar.progress = valuePercentageSeekBar.max / 2

        valuePercentageSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val percent = progress * 5
                tvValuePercentage.text = "$percent%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Manejo del boton calcular
        val btnCal = findViewById<Button>(R.id.btn_cal)

        btnCal.setOnClickListener {
            if (!isInputValid()) {
                Toast.makeText(this, getString(R.string.toast_fill_required_fields), Toast.LENGTH_SHORT).show()
            } else { startResultsActivity() }
        }
    }





    private fun setVisible(vararg views: View) {
        views.forEach { it.visibility = View.VISIBLE }
    }

    private fun isInputValid(): Boolean {
        return when (cutType) {
            "DEFINED_CUT" -> !valueLength.text.isNullOrBlank() &&
                    !valueWidth.text.isNullOrBlank() &&
                    !valuePortionLength.text.isNullOrBlank() &&
                    !valuePortionWidth.text.isNullOrBlank()
            "PRECISE_CUT", "PROPORTIONAL_CUT" -> !valueLength.text.isNullOrBlank() &&
                    !valueWidth.text.isNullOrBlank()
            "REVERSE_CUT" -> !valuePortionLength.text.isNullOrBlank() &&
                    !valuePortionWidth.text.isNullOrBlank()
            else -> true
        }
    }

    // Función para envío de información a la pantalla de resultados
    private fun startResultsActivity() {

        val intent = Intent(this, ResultsActivity::class.java).apply {
            putExtra("CUT_TYPE", cutType)
            putExtra("LENGTH", valueLength.text.toString())
            putExtra("WIDTH", valueWidth.text.toString())
            putExtra("PORTION_LENGTH", valuePortionLength.text.toString())
            putExtra("PORTION_WIDTH", valuePortionWidth.text.toString())
            putExtra("PORTIONS", valuePortionSeekBar.progress + 1)
            putExtra("PERCENTAGE", valuePercentageSeekBar.progress * 5)
        }
        startActivity(intent)
    }
}
