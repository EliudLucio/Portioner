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
import com.eliudlucio.portioner.databinding.ActivityMainBinding


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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Establecer la Toolbar como el App Bar
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnPrecise = findViewById<LinearLayout>(R.id.btn_precise)
        btnPrecise.setOnClickListener{
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra(EXTRA_CUT_TYPE, PRECISE_CUT)
            startActivity(intent)
        }

        val btnDefined = findViewById<LinearLayout>(R.id.btn_defined)
        btnDefined.setOnClickListener{
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra(EXTRA_CUT_TYPE, DEFINED_CUT)
            startActivity(intent)
        }

        val btnReverse = findViewById<LinearLayout>(R.id.btn_reverse)
        btnReverse.setOnClickListener{
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra(EXTRA_CUT_TYPE, REVERSE_CUT)
            startActivity(intent)
        }

        val btnProportional = findViewById<LinearLayout>(R.id.btn_proportion)
        btnProportional.setOnClickListener{
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra(EXTRA_CUT_TYPE, PROPORTIONAL_CUT)
            startActivity(intent)
        }

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