package com.ldg.skymemo.memo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.math.abs


class DrawingView@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private val TOUCH_TOLERANCE=4

    private var xPoint: Float = 0f
    private var yPoint: Float = 0f


    private var mCanvas=Canvas()

    private var path = Path()

    private val paths=ArrayList<Path>()
    private val undoPath=ArrayList<Path>()

    private var isDrawingMode:Boolean=false


    private var paint = Paint().apply {
        isAntiAlias=true
        strokeWidth=10f
        color= Color.BLACK
        style=Paint.Style.STROKE
        strokeJoin=Paint.Join.ROUND
    }


    private fun touchStart(x: Float, y: Float) {
        undoPath.clear()
        path.reset()
        path.moveTo(x, y)
        xPoint = x
        yPoint = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx: Float = abs(x - xPoint)
        val dy: Float = abs(y - yPoint)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(xPoint, yPoint, (x + xPoint) / 2, (y + yPoint) / 2)
            xPoint = x
            yPoint = y
        }
    }

    private fun touchUp() {

        path.lineTo(xPoint, yPoint)
        mCanvas.drawPath(path, paint)
        paths.add(path)
        path = Path()
    }

    fun onClickUndo() {
        if (paths.size > 0) {
            undoPath.add(paths.removeAt(paths.size - 1))
            invalidate()
        }
    }

    fun onClickRedo() {
        if (undoPath.size> 0) {
            paths.add(undoPath.removeLast())
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        for (path in paths) {
            canvas.drawPath(path, paint)
        }
            canvas.drawPath(path, paint)
    }




    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if(isDrawingMode) {
            val x = event.x
            val y = event.y

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchStart(x, y)
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    touchMove(x, y)
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    touchUp()
                    invalidate()
                }
            }
            return true
        }else{
            return false
        }
    }


    fun isDrawNothing():Boolean{
        return paths.size==0
    }


    fun unlockDrawMode(){
        isDrawingMode=true
    }
    fun lockDrawMode(){
        isDrawingMode=false
    }


}