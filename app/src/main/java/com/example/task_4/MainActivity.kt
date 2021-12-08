package com.example.task_4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultView = findViewById<TextView>(R.id.resultView)
        resultView.movementMethod = ScrollingMovementMethod()
        val authorCondition = findViewById<TextInputLayout>(R.id.authorCondition)
        val countryCondition = findViewById<TextInputLayout>(R.id.countryCondition)
        val count = findViewById<TextView>(R.id.countId)
        val btnFilter = findViewById<Button>(R.id.btnFilter)
        var result = mutableListOf<String>()

        btnFilter.setOnClickListener {
            val apiApp = application as ApiApp
            val httpApiService = apiApp.httpApiService
            CoroutineScope(Dispatchers.IO).launch {
                var apiResult = httpApiService.getBooks()
                withContext(Dispatchers.Main){
                    resultView.text = ""
                    count.text = ""
                    result.clear()
                    apiResult.forEach{
                        if (authorCondition.editText?.text.toString() == it.author
                            && countryCondition.editText?.text.toString() == it.country)
                            result.add(it.title)
                        else if (authorCondition.editText?.text.toString().isEmpty()
                            && countryCondition.editText?.text.toString() == it.country)
                                result.add(it.title)
                        else if (authorCondition.editText?.text.toString() == it.author
                            && countryCondition.editText?.text.toString().isEmpty())
                            result.add(it.title)
                        else if (authorCondition.editText?.text.toString().isEmpty()
                            && countryCondition.editText?.text.toString().isEmpty())
                            result.add(it.title)
                    }
                    count.text = "Results:${result.count()}"
                    resultView.text = result.joinToString("\nResult: ", "Result: ","",3)
                }
            }
        }
    }
}