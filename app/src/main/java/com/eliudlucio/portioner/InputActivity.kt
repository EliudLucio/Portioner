package com.eliudlucio.portioner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        // Recibimos el tipo de corte seleccionado
        val cutType = intent.getStringExtra("CUT_TYPE")

        val etLenght = findViewById<EditText>(R.id.et_length)
        val etWidth = findViewById<EditText>(R.id.et_width)
        val etPortionLength = findViewById<EditText>(R.id.et_portion_length)
        val etPortionWidth = findViewById<EditText>(R.id.et_portion_width)
        val layPortions = findViewById<LinearLayout>(R.id.lay_portions)
        val layPercentage = findViewById<LinearLayout>(R.id.lay_percentage)

       when (cutType) {
          "PRECISE_CUT" -> {
              etLenght.visibility = EditText.VISIBLE
              etWidth.visibility = EditText.VISIBLE
              layPortions.visibility = LinearLayout.VISIBLE
          }
           "DEFINED_CUT" -> {
               etLenght.visibility = EditText.VISIBLE
               etWidth.visibility = EditText.VISIBLE
               etPortionLength.visibility = EditText.VISIBLE
               etPortionWidth.visibility = EditText.VISIBLE
           }
          "REVERSE_CUT" -> {
              etPortionLength.visibility = EditText.VISIBLE
              etPortionWidth.visibility = EditText.VISIBLE
              layPortions.visibility = LinearLayout.VISIBLE
           }
          "PROPORTIONAL_CUT" -> {
              etLenght.visibility = EditText.VISIBLE
              etWidth.visibility = EditText.VISIBLE
              layPercentage.visibility = LinearLayout.VISIBLE
          }
        }

        // Manejo del boton calcular

        val btnCal = findViewById<Button>(R.id.btn_cal)

        btnCal.setOnClickListener{
            startResultsActivity()
        }



        // Manejo del Portion Seekbar
        val tvValuePortion = findViewById<TextView>(R.id.tv_portions_number)
        val portionSeekBar = findViewById<SeekBar>(R.id.portionSeekBar)

        tvValuePortion.text = "1"

        portionSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvValuePortion.text = "${progress + 1}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Opcional: Aquí puedes hacer algo cuando se empieza a mover el SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Opcional: Aquí puedes hacer algo cuando se deja de mover el SeekBar
            }
        })

        // Manejo del Percentage Seekbar

        val tvValuePercentage = findViewById<TextView>(R.id.tv_percentage_number)
        val percentageSeekBar = findViewById<SeekBar>(R.id.percentageSeekBar)

        tvValuePercentage.text = "50%"
        percentageSeekBar.progress = percentageSeekBar.max / 2

        percentageSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val percent = progress * 5
                tvValuePercentage.text = "$percent%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Opcional: Aquí puedes hacer algo cuando se empieza a mover el SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Opcional: Aquí puedes hacer algo cuando se deja de mover el SeekBar
            }
        })


    }

    private fun startResultsActivity(){
        val intent = Intent(this, ResultsActivity::class.java)
        startActivity(intent)
    }
}