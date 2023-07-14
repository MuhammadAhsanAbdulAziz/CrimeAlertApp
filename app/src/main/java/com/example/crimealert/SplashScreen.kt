package com.example.crimealert

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep((2 * 1000).toLong())
                    val i = Intent(baseContext, MainActivity::class.java)
                    startActivity(i)

                    finish()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext,e.message,Toast.LENGTH_LONG).show()
                }
            }
        }
        background.start()
    }
}