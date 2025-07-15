package com.eliudlucio.portioner

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import com.eliudlucio.portioner.LocaleHelper.setLocale

class SettingsActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        val base = requireNotNull(newBase)
        val prefs = PreferenceManager.getDefaultSharedPreferences(base)
        val lang = prefs.getString("preferred_language", "es") ?: "es"
        val localized = setLocale(base, lang)

        super.attachBaseContext(localized)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Establecer la Toolbar como el App Bar
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()

    }
}