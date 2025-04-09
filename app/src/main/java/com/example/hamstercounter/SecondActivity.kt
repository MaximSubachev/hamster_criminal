package com.example.hamstercounter

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.os.Environment
import android.view.View


class SecondActivity : ComponentActivity() {
    private var balance = 0
    private lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        textView = findViewById(R.id.textView)


        // Получаем значение balance из Intent
        balance = intent.getIntExtra("balance", 0)
        val downloadButton: Button = findViewById(R.id.downloadButton)
        downloadButton.setOnClickListener {
            saveFile(balance)
        }

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
    }

    private fun saveFile(balance: Int) {
        // Создаем файл и записываем в него значение balance
        val fileName = "balance.txt"
        val fileContents = "Общее количество тапов: $balance"

        // Путь для сохранения файла
        //val file = File(getExternalFilesDir(null), fileName)
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(downloadsDir, fileName)

        try {
            FileOutputStream(file).use { output ->
                output.write(fileContents.toByteArray())
            }
            Toast.makeText(this, "Файл сохранен: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show()
        }

    }



}
