package com.eliudlucio.portioner

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_CUT_TYPE = "cut-type"
        const val PRECISE_CUT = 1
        const val DEFINED_CUT = 2
        const val REVERSE_CUT = 3
        const val PROPORTIONAL_CUT = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Establecer la Toolbar como el App Bar
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        // Asignar click listeners a cada botón
        findViewById<LinearLayout>(R.id.btn_precise).setOnClickListener {
            startInputActivity(PRECISE_CUT)
        }
        findViewById<LinearLayout>(R.id.btn_defined).setOnClickListener {
            startInputActivity(DEFINED_CUT)
        }
        findViewById<LinearLayout>(R.id.btn_reverse).setOnClickListener {
            startInputActivity(REVERSE_CUT)
        }
        findViewById<LinearLayout>(R.id.btn_proportion).setOnClickListener {
            startInputActivity(PROPORTIONAL_CUT)
        }

    }

    // Función helper para crear y lanzar InputActivity
    private fun startInputActivity(cutType: Int) {
        val intent = Intent(this, InputActivity::class.java).apply {
            putExtra(EXTRA_CUT_TYPE, cutType)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_config -> {
                //TODO
                Toast.makeText(this, "Abrir Configuración", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_help -> {
                //TODO
                Toast.makeText(this, "Abrir Ayuda", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}