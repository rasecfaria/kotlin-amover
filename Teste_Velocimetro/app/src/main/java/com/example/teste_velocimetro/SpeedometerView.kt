package com.example.teste_velocimetro

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class SpeedometerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Configuração da velocidade máxima e atual
    var maxSpeed: Float = 180f
        set(value) { field = value; invalidate() }
    var speed: Float = 0f
        set(value) { field = value.coerceIn(0f, maxSpeed); invalidate() }

    // Tintas para desenhar (Estilo)
    private val basePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 24f
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#2962FF") // Azul
        style = Paint.Style.STROKE
        strokeWidth = 24f
        strokeCap = Paint.Cap.ROUND
    }

    private val needlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }

    // O desenho propriamente dito
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        val radius = min(w, h) / 2f * 0.9f
        val cx = w / 2f
        val cy = h / 2f
        val rect = RectF(cx - radius, cy - radius, cx + radius, cy + radius)

        // 1. Desenhar o arco cinzento (fundo)
        canvas.drawArc(rect, 135f, 270f, false, basePaint)

        // 2. Desenhar o arco azul (progresso) baseado na velocidade
        val sweep = 270f * (speed / maxSpeed)
        canvas.drawArc(rect, 135f, sweep, false, progressPaint)

        // 3. Desenhar o ponteiro (Matemática para encontrar a ponta da agulha)
        val angleDeg = 135f + sweep
        val angleRad = Math.toRadians(angleDeg.toDouble())
        val needleLen = radius * 0.85f
        val nx = cx + (cos(angleRad) * needleLen).toFloat()
        val ny = cy + (sin(angleRad) * needleLen).toFloat()
        canvas.drawLine(cx, cy, nx, ny, needlePaint)
    }
}