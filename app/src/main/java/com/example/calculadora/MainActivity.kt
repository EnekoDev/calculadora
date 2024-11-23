package com.example.calculadora

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        val result:TextView = binding.resultText

        binding.btn0.setOnClickListener(this)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
        binding.btn7.setOnClickListener(this)
        binding.btn8.setOnClickListener(this)
        binding.btn9.setOnClickListener(this)
        binding.btnSumar.setOnClickListener(this)
        binding.btnRestar.setOnClickListener(this)
        binding.btnMulti.setOnClickListener(this)
        binding.btnDivision.setOnClickListener(this)
        binding.btnLimpiar.setOnClickListener {
            result.text = "0"
            mostrarResultado(result)
        }
        binding.btnIgual.setOnClickListener { mostrarResultado(result) }
    }

    override fun onClick(v: View?) {
        val value = when (v?.id) {
            R.id.btn0 -> binding.btn0.text.toString()
            R.id.btn1 -> binding.btn1.text.toString()
            R.id.btn2 -> binding.btn2.text.toString()
            R.id.btn3 -> binding.btn3.text.toString()
            R.id.btn4 -> binding.btn4.text.toString()
            R.id.btn5 -> binding.btn5.text.toString()
            R.id.btn6 -> binding.btn6.text.toString()
            R.id.btn7 -> binding.btn7.text.toString()
            R.id.btn8 -> binding.btn8.text.toString()
            R.id.btn9 -> binding.btn9.text.toString()
            R.id.btnSumar -> binding.btnSumar.text.toString()
            R.id.btnRestar -> binding.btnRestar.text.toString()
            R.id.btnMulti -> binding.btnMulti.text.toString()
            R.id.btnDivision -> binding.btnDivision.text.toString()
            else -> ""
        }

        updateResultText(value)
    }

    private fun updateResultText(value:String) {
        val currentText = binding.resultText.text.toString()

        if (currentText == "0" && value.isNotEmpty()) {
            binding.resultText.text = value
        } else if (value.isNotEmpty()) {
            binding.resultText.text = buildString { append(currentText).append(value) }
        }
    }

    private fun extraerValores(result:TextView):String {
        val resultString = result.text.toString()
        val regex = Regex("(\\d+(?:\\d+)?|[+\\-*/])")
        val numbers:MutableList<Int> = mutableListOf()
        val operators:MutableList<String> = mutableListOf()
        val matches = regex.findAll(resultString)

        for (match in matches) {
            val value = match.value
            if(value.matches(Regex("\\d+(?:\\d+)?"))) {
                numbers.add(value.toInt())
            } else {
                operators.add(value)
            }
        }
        return  try {
            realizarOperacion(numbers, operators).toString()
        } catch (err:ArithmeticException) {
            "No se puede dividir entre cero"
        }
    }

    private fun realizarOperacion(numeros:List<Int>, operadores:List<String>):Int {
        var resultado: Int = numeros.firstOrNull() ?: 0

        for (i in operadores.indices) {
            resultado = when (operadores[i]) {
                "+" -> resultado + numeros[i + 1]
                "-" -> resultado - numeros[i + 1]
                "*" -> resultado * numeros[i + 1]
                "/" -> { if (numeros[i + 1] != 0) resultado / numeros[i + 1] else throw ArithmeticException("No se puede dividir entre cero") }
                else -> resultado
            }
        }
        return resultado
    }

    private fun mostrarResultado(view:TextView) {
        binding.resultText.text = extraerValores(view)
    }
}