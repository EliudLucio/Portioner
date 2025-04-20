package com.eliudlucio.portioner

import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        // Recibimos el tipo de corte seleccionado
        val cutType = intent.getStringExtra("cut_type")

//        when (cutType) {
//            "precise" -> {
//                layoutPrecise.visibility = View.VISIBLE
//            }
//            "custom" -> {
//                layoutCustom.visibility = View.VISIBLE
//            }
//            "reverse" -> {
//                layoutReverse.visibility = View.VISIBLE
//            }
//            "proportional" -> {
//                layoutProportional.visibility = View.VISIBLE
//            }
//        }

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
}