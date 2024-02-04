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


        binding.tvShowCalculation.text = calculationText






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
            binding.tvShowCalculation.text = calculationText
        }
    }
    fun clearField(view: View) {
        if (view is Button){
            //Delete calculation text
            calculationText = ""
            binding.tvShowCalculation.text = calculationText
            binding.tvDisplayAnswer.text = calculationText
        }

    }
    fun deleteSingle(view: View) {
        if (view is Button){
            if (calculationText.isNotEmpty())
                calculationText = calculationText.removeRange(calculationText.length-1, calculationText.length)
            binding.tvShowCalculation.text = calculationText
        }
    }

    fun solveEquation(view: View) {
        if (view is Button ){
            try {
                val result = evaluateExpression(calculationText)
                binding.tvDisplayAnswer.text = result.toString()
            }catch (e:Exception){
                binding.tvDisplayAnswer.text = "Error"
                Toast.makeText(this ,"$e", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun evaluateExpression(expression: String): Double {
        return ExpressionBuilder(expression).build().evaluate()
    }

}