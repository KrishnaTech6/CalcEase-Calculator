package com.example.calceasecalculator

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.calceasecalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private var hasOpenBracket: Boolean = false
    private lateinit var binding: ActivityMainBinding
    private var calculationText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun getData(view: View) {
        vibrate()
        if (view is Button) {
            var text = view.text.toString()
            when (text) {
                "( )" -> {
                    text = if (hasOpenBracket) {
                        hasOpenBracket = false
                        ")"
                    } else {
                        hasOpenBracket = true
                        "("
                    }
                }
            }
            calculationText += text
            solvedEquationDisplayed()
            binding.tvShowCalculation.text = calculationText
        }
    }

    fun clearField(view: View) {
        vibrate()
        if (view is Button) {
            calculationText = ""
            hasOpenBracket = false
            solvedEquationDisplayed()
            binding.tvShowCalculation.text = calculationText
            binding.tvDisplayAnswer.text = calculationText
        }
    }

    fun deleteSingle(view: View) {
        vibrate()
        if (view is Button) {
            if (calculationText.isNotEmpty()) {
                calculationText = calculationText.removeRange(calculationText.length - 1, calculationText.length)
                hasOpenBracket = checkOpenBrackets()
            }
            solvedEquationDisplayed()
            binding.tvShowCalculation.text = calculationText
        }
    }

    fun solveEquation(view: View) {
        vibrate()
        if (view is Button) {
            solvedEquationDisplayed(false)
        }
    }

    private fun evaluateExpression(expression: String): Double {
        return ExpressionBuilder(expression).build().evaluate()
    }

    private fun solvedEquationDisplayed(hasPreview: Boolean = true) {
        try {
            val result = evaluateExpression(calculationText.replace("×", "*"))
            updateUIText(hasPreview, result.toString())
        } catch (e: Exception) {
            if (hasPreview) {
                updateUIText(true, "")
            } else {
                updateUIText(false, "Error")
            }
        }
    }

    private fun updateUIText(hasPreview: Boolean, displayText: String) {
        if (hasPreview) {
            binding.tvShowCalculation.textSize = 44f
            binding.tvDisplayAnswer.textSize = 30f
            binding.tvDisplayAnswer.setTextColor(ContextCompat.getColor(this, R.color.previewTextColor))
            binding.tvShowCalculation.setTextColor(ContextCompat.getColor(this, R.color.textColor))
        } else {
            binding.tvShowCalculation.textSize = 30f
            binding.tvDisplayAnswer.textSize = 44f
            binding.tvDisplayAnswer.setTextColor(ContextCompat.getColor(this, R.color.textColor))
            binding.tvShowCalculation.setTextColor(ContextCompat.getColor(this, R.color.previewTextColor))
        }
        binding.tvDisplayAnswer.text = displayText
    }

    private fun checkOpenBrackets(): Boolean {
        var openBracketCount = 0
        var closeBracketCount = 0
        for (char in calculationText) {
            when (char) {
                '(' -> openBracketCount++
                ')' -> closeBracketCount++
            }
        }
        return openBracketCount > closeBracketCount
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(100)
        }
    }
}
