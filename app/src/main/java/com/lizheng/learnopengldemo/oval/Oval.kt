package com.lizheng.learnopengldemo.oval

import android.opengl.GLES20
import com.lizheng.learnopengldemo.COORDS_PER_VERTEX
import com.lizheng.learnopengldemo.loadShader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin

/**
 *  int GL_POINTS       //将传入的顶点坐标作为单独的点绘制
    int GL_LINES        //将传入的坐标作为单独线条绘制，ABCDEFG六个顶点，绘制AB、CD、EF三条线
    int GL_LINE_STRIP   //将传入的顶点作为折线绘制，ABCD四个顶点，绘制AB、BC、CD三条线
    int GL_LINE_LOOP    //将传入的顶点作为闭合折线绘制，ABCD四个顶点，绘制AB、BC、CD、DA四条线。
    int GL_TRIANGLES    //将传入的顶点作为单独的三角形绘制，ABCDEF绘制ABC,DEF两个三角形
    int GL_TRIANGLE_FAN    //将传入的顶点作为扇面绘制，ABCDEF绘制ABC、ACD、ADE、AEF四个三角形
    int GL_TRIANGLE_STRIP   //将传入的顶点作为三角条带绘制，ABCDEF绘制ABC,BCD,CDE,DEF四个三角形
 */

class Oval {
    private val vertexShaderCode = "attribute vec4 vPosition;" +
            "uniform mat4 vMatrix;" +
            "void main() {" +
            "  gl_Position = vMatrix*vPosition;" +
            "}"

    private val fragmentShaderCode = "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}"

    private var vertexBuffer:FloatBuffer
    private val n = 360 //切割份数
    private var radius = 0.5f
    private var height = 0.0f
    private val vertexStride = 0
    private var color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)  //设置颜色，依次为红绿蓝和透明通道


    private var shapePos:FloatArray

    private var mProgram:Int
    private var mMatrixHandler = 0
    private var mPositionHandle = 0
    private var mColorHandle = 0

    private fun createPosition(): FloatArray {
        val data = ArrayList<Float>()
        //设置圆心坐标
        //----------------------------
        data.add(0.0f)
        data.add(0.0f)
        data.add(height)
        //----------------------------
        val angDegSpan = 360f/n
        run {
            var i = 0f
            while (i < 360 + angDegSpan) {
                data.add((radius * sin(i * Math.PI / 180f)).toFloat())
                data.add((radius * cos(i * Math.PI / 180f)).toFloat())
                data.add(height)
                i += angDegSpan
            }
        }

        val f = FloatArray(data.size)
        for (i in f.indices) {
            f[i] = data[i]
        }
        return f
    }

    init{
        shapePos = createPosition()
        vertexBuffer = ByteBuffer.allocateDirect(shapePos.size*4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(shapePos)
                position(0)
            }
        }

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        //创建一个OpenGL ES 程序
        mProgram = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    fun draw(mvpMatrix: FloatArray){
        GLES20.glUseProgram(mProgram)
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram,"vMatrix").also {
            GLES20.glUniformMatrix4fv(it,1,false,mvpMatrix,0)
        }
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition").also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
            )

            mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor").also {colorHandle->
                //设置绘制三角形的颜色
                GLES20.glUniform4fv(colorHandle,1,color,0)
            }

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,shapePos.size / COORDS_PER_VERTEX)
            GLES20.glDisableVertexAttribArray(it)
        }
    }

}

