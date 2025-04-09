package com.example.hamstercounter

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog
import android.content.DialogInterface

import android.media.AudioManager
import android.media.MediaPlayer




class MainActivity : ComponentActivity() {
    private var balance = 0

    private lateinit var counterTextView: TextView
    private lateinit var hamsterButton: Button
    private lateinit var goToSecondActivityButton: Button

    private var counter = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                )
        mediaPlayer = MediaPlayer.create(this, R.raw.my_music)
        mediaPlayer.isLooping = true //зацикливание
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.start()

        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("HamsterCounterPrefs", MODE_PRIVATE)
        counter = sharedPreferences.getInt("counter", 0)
        balance = sharedPreferences.getInt("balance", 0)

        counterTextView = findViewById(R.id.counterTextView)
        hamsterButton = findViewById(R.id.hamsterButton)

        goToSecondActivityButton = findViewById(R.id.goToSecondActivityButton)

        counterTextView.text = counter.toString()

        hamsterButton.setOnClickListener {
            counter++
            balance += 1
            counterTextView.text = counter.toString()
            saveCounter()


            // Добавляем вибрацию
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(50)
        }

        goToSecondActivityButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("balance", balance)
            startActivity(intent)
        }



        val resetButton: Button = findViewById(R.id.doubleProfitButton)
        resetButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Вы уверены, что хотите сбросить прогресс?")
                .setCancelable(false)
                .setPositiveButton("Да") { dialog: DialogInterface, which: Int ->
                    counter = 0
                    counterTextView.text = "Счетчик: $counter"
                }
                .setNegativeButton("Нет") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss() // Закрытие диалога
                }
            val alert = builder.create()
            alert.show() // Показываем диалог
        }
    }


    private fun saveCounter() {
        val editor = sharedPreferences.edit()
        editor.putInt("counter", counter)
        editor.putInt("balance", balance)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        // Сохранение баланса при выходе из приложения
        saveCounter()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop() // Остановить воспроизведение, когда активити уничтожается
        mediaPlayer.release() // Освобождаем ресурсы
    }
}
