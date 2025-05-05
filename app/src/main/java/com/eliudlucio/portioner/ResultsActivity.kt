package com.eliudlucio.portioner

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        class ResultsActivity : AppCompatActivity() {

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_results)

                val cutType = intent.getStringExtra("CUT_TYPE")
                val length = intent.getStringExtra("LENGTH")?.toDoubleOrNull()
                val width = intent.getStringExtra("WIDTH")?.toDoubleOrNull()
                val portionLength = intent.getStringExtra("PORTION_LENGTH")?.toDoubleOrNull()
                val portionWidth = intent.getStringExtra("PORTION_WIDTH")?.toDoubleOrNull()
                val portions = intent.getIntExtra("PORTIONS", 1)
                val percentage = intent.getIntExtra("PERCENTAGE", 50)

                // TODO Hacer los calculos dependiendo el corte y mostrarlos en pantalla

                when (cutType) {
                    "DEFINED_CUT" -> {
                        // Haz cálculos con length, width, portionLength, portionWidth
                    }
                    "PRECISE_CUT" -> {
                        // Usa length, width, portions
                    }
                    "REVERSE_CUT" -> {
                        // Usa portionLength, portionWidth, portions
                    }
                    "PROPORTIONAL_CUT" -> {
                        // Usa length, width, percentage
                    }
                }

                /*
                val resultText = findViewById<TextView>(R.id.result_text)
                resultText.text = "Tipo de corte: $cutType\nLongitud: $length\nAncho: $width\n..."
                */
            }
        }

    }
}