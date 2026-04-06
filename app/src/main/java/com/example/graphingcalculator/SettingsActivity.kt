package com.example.graphingcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit

/**
 * The activity containing app settings
 */
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val settingAngles = findViewById<RadioGroup>(R.id.settingAngles)
        val settingDegrees = findViewById<RadioButton>(R.id.settingDegrees)
        val settingRadians = findViewById<RadioButton>(R.id.settingRadians)

        val sharedPref = getSharedPreferences("prefs", MODE_PRIVATE)

        if (sharedPref.getBoolean("isRadians", true)) {
            settingRadians.isChecked = true
        } else {
            settingDegrees.isChecked = true
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            sharedPref.edit {
                putBoolean("isRadians", settingAngles.checkedRadioButtonId == R.id.settingRadians)
            }
            finish()
        }
    }
}