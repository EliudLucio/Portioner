package com.eliudlucio.portioner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)


        findPreference<ListPreference>("preferred_language")?.setOnPreferenceChangeListener { _, newValue ->
            val lang = newValue as String
            PreferenceManager
                .getDefaultSharedPreferences(requireContext())
                .edit()
                .putString("preferred_language", lang)
                .apply()

            restartApp()
            true
        }
    }

    private fun restartApp() {
        val context = requireContext()
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
        if (context is Activity) {
            context.finishAffinity()
        }
    }

}

