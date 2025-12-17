package com.example.teste_velocimetro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // 1. Declarar as peças que vamos usar
    private lateinit var speedometerView: SpeedometerView
    private lateinit var speedText: TextView
    private lateinit var btnBrake: Button
    private lateinit var btnAccel: Button

    // Variáveis de estado (como está a mota agora?)
    private var speed = 0f
    private val maxSpeed = 180f

    // Coisas técnicas para a animação de desacelerar (Timer)
    private val handler = Handler(Looper.getMainLooper())
    private val decelRunnable = object : Runnable {
        override fun run() {
            if (speed > 0f) {
                speed = (speed - 0.5f).coerceAtLeast(0f) // Desaceleração mais forte
                handler.postDelayed(this, 50L) // Repetir daqui a 50 milisegundos
                updateUI()
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

        // Configurar o velocímetro
        speedometerView.maxSpeed = maxSpeed

        // 3. O que acontece quando clicamos em "Acelerar"?
        btnAccel.setOnClickListener {
            speed = (speed + 8f).coerceAtMost(maxSpeed) // Aumenta 8, mas não passa do máximo
            updateUI()

            // Se começámos a acelerar, paramos a desaceleração automática
            handler.removeCallbacks(decelRunnable)
            // E agendamos para começar de novo se o utilizador parar de carregar
            handler.postDelayed(decelRunnable, 300L)
        }

        // 4. O que acontece quando clicamos em "Travar"?
        btnBrake.setOnClickListener {
            speed = (speed - 15f).coerceAtLeast(0f) // Diminui 15, mas não baixa de 0
            updateUI()
        }
    }

    override fun onResume() {
        super.onResume()
        // Começa a desacelerar gradualmente quando a app está ativa
        handler.post(decelRunnable)
    }

    override fun onPause() {
        super.onPause()
        // Para a desaceleração para não gastar recursos em background
        handler.removeCallbacks(decelRunnable)
    }


    // Função auxiliar para atualizar o texto e o desenho
    private fun updateUI() {
        speedometerView.speed = speed
        speedText.text = "Velocidade: ${speed.toInt()} km/h"
    }
}