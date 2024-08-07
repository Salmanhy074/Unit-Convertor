package com.example.unitconvertor

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerUnitFrom: Spinner
    private lateinit var spinnerUnitTo: Spinner
    private lateinit var editTextValue: EditText
    private lateinit var buttonConvert: Button
    private lateinit var textViewResult: TextView

    private val lengthUnits = arrayOf("Select", "Meter", "Kilometer", "Centimeter", "Millimeter", "Inch", "Foot", "Yard", "Mile")
    private val massUnits = arrayOf("Select", "Gram", "Kilogram", "Milligram", "Pound", "Ounce")
    private val temperatureUnits = arrayOf("Select", "Celsius", "Fahrenheit", "Kelvin")
    private val emptyUnits = arrayOf("Select")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        spinnerCategory = findViewById(R.id.spinner_category)
        spinnerUnitFrom = findViewById(R.id.spinner_unit_from)
        spinnerUnitTo = findViewById(R.id.spinner_unit_to)
        editTextValue = findViewById(R.id.edittext_value)
        buttonConvert = findViewById(R.id.button_convert)
        textViewResult = findViewById(R.id.textview_result)

        val categories = arrayOf("Select", "Length", "Mass", "Temperature")
        spinnerCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)

        // Disable the convert button initially
        buttonConvert.isEnabled = false

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    1 -> {
                        setUnitsAdapter(lengthUnits)
                        buttonConvert.isEnabled = true
                    }
                    2 -> {
                        setUnitsAdapter(massUnits)
                        buttonConvert.isEnabled = true
                    }
                    3 -> {
                        setUnitsAdapter(temperatureUnits)
                        buttonConvert.isEnabled = true
                    }
                    else -> {
                        setUnitsAdapter(emptyUnits)
                        buttonConvert.isEnabled = false
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                setUnitsAdapter(emptyUnits)
                buttonConvert.isEnabled = false
            }
        }

        buttonConvert.setOnClickListener {
            val category = spinnerCategory.selectedItem.toString()
            val unitFrom = spinnerUnitFrom.selectedItem.toString()
            val unitTo = spinnerUnitTo.selectedItem.toString()
            val value = editTextValue.text.toString().toDoubleOrNull()

            if (unitFrom == "Select" || unitTo == "Select") {
                Toast.makeText(this, "Please select valid units for conversion", Toast.LENGTH_SHORT).show()
            } else if (value == null) {
                Toast.makeText(this, "Please enter a valid value", Toast.LENGTH_SHORT).show()
            } else {
                val result = convertUnits(category, unitFrom, unitTo, value)
                textViewResult.text = "$result $unitTo"
            }
        }
    }


    private fun setUnitsAdapter(units: Array<String>) {
        spinnerUnitFrom.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        spinnerUnitTo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        spinnerUnitFrom.setSelection(0)
        spinnerUnitTo.setSelection(0)

    }

    private fun convertUnits(category: String, unitFrom: String, unitTo: String, value: Double): Double {
        return when (category) {
            "Length" -> convertLength(unitFrom, unitTo, value)
            "Mass" -> convertMass(unitFrom, unitTo, value)
            "Temperature" -> convertTemperature(unitFrom, unitTo, value)
            else -> 0.0
        }

    }

    private fun convertLength(unitFrom: String, unitTo: String, value: Double): Double {
        val toMeters = mapOf(
            "Meter" to 1.0,
            "Kilometer" to 1000.0,
            "Centimeter" to 0.01,
            "Millimeter" to 0.001,
            "Inch" to 0.0254,
            "Foot" to 0.3048,
            "Yard" to 0.9144,
            "Mile" to 1609.34
        )
        val fromMeters = mapOf(
            "Meter" to 1.0,
            "Kilometer" to 0.001,
            "Centimeter" to 100.0,
            "Millimeter" to 1000.0,
            "Inch" to 39.3701,
            "Foot" to 3.28084,
            "Yard" to 1.09361,
            "Mile" to 0.000621371
        )
        return value * toMeters[unitFrom]!! * fromMeters[unitTo]!!

    }

    private fun convertMass(unitFrom: String, unitTo: String, value: Double): Double {
        val toGrams = mapOf(
            "Gram" to 1.0,
            "Kilogram" to 1000.0,
            "Milligram" to 0.001,
            "Pound" to 453.592,
            "Ounce" to 28.3495
        )
        val fromGrams = mapOf(
            "Gram" to 1.0,
            "Kilogram" to 0.001,
            "Milligram" to 1000.0,
            "Pound" to 0.00220462,
            "Ounce" to 0.035274
        )
        return value * toGrams[unitFrom]!! * fromGrams[unitTo]!!
    }

    private fun convertTemperature(unitFrom: String, unitTo: String, value: Double): Double {
        return when (unitFrom to unitTo) {
            "Celsius" to "Fahrenheit" -> value * 9 / 5 + 32
            "Celsius" to "Kelvin" -> value + 273.15
            "Fahrenheit" to "Celsius" -> (value - 32) * 5 / 9
            "Fahrenheit" to "Kelvin" -> (value - 32) * 5 / 9 + 273.15
            "Kelvin" to "Celsius" -> value - 273.15
            "Kelvin" to "Fahrenheit" -> (value - 273.15) * 9 / 5 + 32
            else -> value
        }
    }
}