package com.eliudlucio.portioner

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eliudlucio.portioner.databinding.ActivityMainBinding

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

        val valueTextView = findViewById<TextView>(R.id.tv_portions_number)
        val portionSeekBar = findViewById<SeekBar>(R.id.portionSeekBar)

        valueTextView.text = "1"

        portionSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                valueTextView.text = "${progress + 1}"
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