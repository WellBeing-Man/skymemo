package com.ldg.skymemo.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class AlwaysTopImageVIew@JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0)
    : AppCompatImageView(context, attrs, defStyleAttr){


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.bringToFront()
    }
    }