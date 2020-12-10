package com.lizheng.learnopengldemo

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube {
    private val vertexShaderCode = "attribute vec4 vPosition;" +
            "uniform mat4 vMatrix;"+
            "varying  vec4 vColor;"+
            "attribute vec4 aColor;"+
            "void main() {" +
            "  gl_Position = vMatrix*vPosition;" +
            "  vColor=aColor;"+
            "}";

    private val fragmentShaderCode = "precision mediump float;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    val cubePositions = floatArrayOf(
            -1.0f, 1.0f, 1.0f,  //正面左上0
            -1.0f, -1.0f, 1.0f,  //正面左下1
            1.0f, -1.0f, 1.0f,  //正面右下2
            1.0f, 1.0f, 1.0f,  //正面右上3
            -1.0f, 1.0f, -1.0f,  //反面左上4
            -1.0f, -1.0f, -1.0f,  //反面左下5
            1.0f, -1.0f, -1.0f,  //反面右下6
            1.0f, 1.0f, -1.0f)

    val index = shortArrayOf(
            6, 7, 4, 6, 4, 5,  //后面
            6, 3, 7, 6, 2, 3,  //右面
            6, 5, 1, 6, 1, 2,  //下面
            0, 3, 2, 0, 2, 1,  //正面
            0, 1, 5, 0, 5, 4,  //左面
            0, 7, 3, 0, 4, 7)

    var color = floatArrayOf(
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f)

    private var vertexBuffer: FloatBuffer
    private var colorBuffer: FloatBuffer
    private var indexBuffer: ShortBuffer
    private var mProgram:Int
    private var mMatrixHandler = 0
    private var mPositionHandle = 0
    private var mColorHandle = 0

    init {
        vertexBuffer = ByteBuffer.allocateDirect(cubePositions.size*4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(cubePositions)
                position(0)
            }
        }

        colorBuffer = ByteBuffer.allocateDirect(color.size*4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(color)
                position(0)
            }
        }

        indexBuffer = ByteBuffer.allocateDirect(index.size*2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(index)
                position(0)
            }
        }

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

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
                    0,
                    vertexBuffer
            )
            mColorHandle = GLES20.glGetAttribLocation(mProgram,"aColor").also {colorHandle->
                GLES20.glEnableVertexAttribArray(colorHandle);
                //设置绘制三角形的颜色
                GLES20.glVertexAttribPointer(
                        colorHandle,
                        4,
                        GLES20.GL_FLOAT,
                        false,
                        0,
                        colorBuffer
                )
            }
            GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.size,GLES20.GL_UNSIGNED_SHORT,indexBuffer)
            GLES20.glDisableVertexAttribArray(it)
        }
    }

}
