package com.example.finalexamapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.CYAN)
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 50f
            textAlign = Paint.Align.CENTER
        }

        canvas.drawText(
            "Vue personnalisée!",
            width / 2f,
            height / 2f,
            paint
        )
    }
}
