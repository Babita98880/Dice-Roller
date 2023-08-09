package com.example.dicerolling
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.floor

import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {

    private lateinit var diceSpinner: Spinner
    private lateinit var rollButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceSpinner = findViewById(R.id.diceSpinner)
        rollButton = findViewById(R.id.rollButton)
        resultTextView = findViewById(R.id.resultTextView)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("diceroller", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setDefaultNightMode(nightMode)


        // Initialize the spinner
        val diceOptions = resources.getStringArray(R.array.dice_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, diceOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        diceSpinner.adapter = adapter

        // Set click listener for roll button
        rollButton.setOnClickListener {
            rollDice()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the options menu layout
        menuInflater.inflate(R.menu.menu_main, menu)
        // Find the night mode toggle switch in the menu
        val nightModeItem = menu?.findItem(R.id.action_toggle_daynight_mode)
        val nightModeSwitch = nightModeItem?.actionView as? SwitchCompat
        // Set the switch state based on the current night mode
        nightModeSwitch?.isChecked = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)

        // Add a listener to the night mode switch to update the night mode setting
        nightModeSwitch?.setOnCheckedChangeListener { _, isChecked ->
            val newNightMode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else
                AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(newNightMode)

            // Save the new night mode setting and team scores to SharedPreferences
            val sharedPreferences = getSharedPreferences("diceroller", Context.MODE_PRIVATE)
             // Check if the save score setting is true, if not, set default team scores to 0

            with(sharedPreferences.edit()) {
                putInt("night_mode", newNightMode)
                apply()
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun rollDice() {
        val selectedOption = diceSpinner.selectedItem.toString()
        val maxVal = when (selectedOption) {
            "4-sided" -> 4
            "6-sided" -> 6
            "8-sided" -> 8
            "10-sided" -> 10
            "12-sided" -> 12
            "20-sided" -> 20
            else -> 6
        }

        val randomVal1 = (floor(Math.random() * maxVal) + 1).toInt()
        val randomVal2 = (floor(Math.random() * maxVal) + 1).toInt()
        resultTextView.text = "Results: $randomVal1, $randomVal2"
    }
}
