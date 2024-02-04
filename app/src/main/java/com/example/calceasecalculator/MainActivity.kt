package com.example.calceasecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.calceasecalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private var hasOpenBracket: Boolean = false
    private lateinit var binding: ActivityMainBinding
    private var calculationText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun getData(view: View) {
        if(view is Button){
            var text = view.text.toString()
            when(text){
                "X" -> text = "*"
                "( )" ->{
                    if (hasOpenBracket){
                        text = ")"
                        hasOpenBracket= false
                    }else{
                        text = "("
                        hasOpenBracket= true
                    }
                }
            }
            calculationText += text
            solvedEquationDisplayed()
            binding.tvShowCalculation.text = calculationText
        }
    }
    fun clearField(view: View) {
        if (view is Button){
            //Delete calculation text
            calculationText = ""
            hasOpenBracket= false
            solvedEquationDisplayed()
            binding.tvShowCalculation.text = calculationText
            binding.tvDisplayAnswer.text = calculationText
        }

    }
    fun deleteSingle(view: View) {
        if (view is Button){
            if (calculationText.isNotEmpty())
                calculationText = calculationText.removeRange(calculationText.length-1, calculationText.length)
            else hasOpenBracket=false

            //For determining if we have to apply open bracket or closed bracket
            var openBracketCount = 0
            var closeBracketCount = 0
            for (char in calculationText){
                when(char){
                    '(' -> openBracketCount++
                    ')' -> closeBracketCount++
                }
            }
            hasOpenBracket =
                openBracketCount > closeBracketCount //will return true of false , so that next time clicking on brackets button right bracket is shown
            solvedEquationDisplayed()
            binding.tvShowCalculation.text = calculationText
        }
    }

    fun solveEquation(view: View) {
        if (view is Button ){
            solvedEquationDisplayed(false)
        }
    }

    private fun evaluateExpression(expression: String): Double {
        return ExpressionBuilder(expression).build().evaluate()
    }
    private fun solvedEquationDisplayed(hasPreview:Boolean =true){
        try {
            val result = evaluateExpression(calculationText)
            if (hasPreview){
                binding.tvShowCalculation.textSize =44f
                binding.tvDisplayAnswer.textSize = 30F
            }else{
                binding.tvShowCalculation.textSize =30f
                binding.tvDisplayAnswer.textSize = 44F
            }
            binding.tvDisplayAnswer.text = result.toString()

        }catch (e:Exception){
            if (hasPreview){
                binding.tvShowCalculation.textSize =44f
                binding.tvDisplayAnswer.textSize = 30F
                binding.tvDisplayAnswer.text = ""
            }else{
                binding.tvShowCalculation.textSize =44f
                binding.tvDisplayAnswer.textSize = 30F
                binding.tvDisplayAnswer.text = "Error"
            }
        }
    }

}