package com.example.teste_velocimetro

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // 1. Declarar as peças que vamos usar
    private lateinit var speedometerView: SpeedometerView
    private lateinit var speedText: TextView
    private lateinit var btnBrake: Button
    private lateinit var btnAccel: Button
    private lateinit var batteryBar : ProgressBar
    private var battery = 100

    // Variáveis de estado (como está a mota agora?)
    private var speed = 0f
    private val maxSpeed = 180f

    // Coisas técnicas para a animação de desacelerar (Timer)
    private val handler = Handler(Looper.getMainLooper())
    private val decelRunnable = object : Runnable {
        override fun run() {
            if (speed > 0f) {
                speed = (speed - 0.5f).coerceAtLeast(0f)
                updateUI()
                handler.postDelayed(this, 50L) // Repetir daqui a 50 milisegundos
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 2. Ligar as variáveis ao XML (Encontrar as peças pelo ID)
        speedometerView = findViewById(R.id.speedometerView)
        speedText = findViewById(R.id.speedText)
        btnBrake = findViewById(R.id.btnBrake)
        btnAccel = findViewById(R.id.btnAccel)
        batteryBar = findViewById(R.id.batteryBar)


        // Configurar o velocímetro
        speedometerView.maxSpeed = maxSpeed

        // 3. O que acontece quando clicamos em "Acelerar"?
        btnAccel.setOnClickListener {
            if (battery > 0) {
                speed = (speed + 10f).coerceAtMost(maxSpeed) // Aumenta 10, mas não passa do máx
                battery = (battery - 4).coerceAtLeast(0)

                updateUI()

                // Se começámos a acelerar, garantimos que a desaceleração está ativa
                handler.removeCallbacks(decelRunnable)
                handler.post(decelRunnable)
            }



        }

        // 4. O que acontece quando clicamos em "Travar"?
        btnBrake.setOnClickListener {
            speed = (speed - 15f).coerceAtLeast(0f) // Diminui 15, mas não baixa de 0
            battery = (battery + 2).coerceAtMost(100)
            updateUI()
        }
    }

    // Função auxiliar para atualizar o texto e o desenho
    private fun updateUI() {
        speedometerView.speed = speed
        speedText.text = "Velocidade: ${speed.toInt()} km/h"

        batteryBar.progress = battery

        if (battery == 0) {
            speedText.setTextColor(Color.RED)
        } else {
            speedText.setTextColor(Color.BLACK)
        }

    }
}