package com.lizheng.learnopengldemo

import android.opengl.Matrix
import android.os.SystemClock
import com.lizheng.learnopengldemo.base.BaseGLRenderer

open class MyGLRenderer:BaseGLRenderer(){
    private lateinit var triangle:Triangle
    private lateinit var colortriangle:ColorTriangle
    private val rotationMatrix = FloatArray(16)
    @Volatile
    var angle: Float = 0f

    override fun onCreatedView() {
        //        triangle = Triangle()
        colortriangle = ColorTriangle()
    }

    override fun onDrawView() {
//        triangle.draw()
//        triangle.draw(vPMatrix)

        val scratch = FloatArray(16)

        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)

        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
        colortriangle.drawColor(scratch)
    }

    override fun onViewChanged(width: Int, height: Int) {

    }

}

