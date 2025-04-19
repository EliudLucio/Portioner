package com.eliudlucio.portioner

import android.os.Bundle
import android.view.View
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

    }
}