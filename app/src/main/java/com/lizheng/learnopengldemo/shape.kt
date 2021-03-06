package com.lizheng.learnopengldemo

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * 在 OpenGL 中，执行此操作的典型方式是为坐标定义浮点数的顶点数组。
 * 为了最大限度地提高工作效率，您可以将这些坐标写入 ByteBuffer 中，它会传递到 OpenGL ES 图形管道进行处理。
 */
// number of coordinates per vertex in this array
const val COORDS_PER_VERTEX = 3
var triangleCoords = floatArrayOf(     // in counterclockwise order:
    0.0f, 0.622008459f, 0.0f,      // top
    -0.5f, -0.311004243f, 0.0f,    // bottom left
    0.5f, -0.311004243f, 0.0f      // bottom right
)

var squareCoords = floatArrayOf(
    -0.5f,  0.5f, 0.0f,      // top left
    -0.5f, -0.5f, 0.0f,      // bottom left
    0.5f, -0.5f, 0.0f,      // bottom right
    0.5f,  0.5f, 0.0f       // top right
)

//设置颜色
var color2 = floatArrayOf(
        0.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f
)

class Triangle {

    //着色程序包含 OpenGL 着色语言 (GLSL) 代码，必须先对其进行编译，然后才能在 OpenGL ES 环境中使用。
//    private val vertexShaderCode =
//        "attribute vec4 vPosition;" +
//                "void main() {" +
//                "  gl_Position = vPosition;" +
//                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    /**
     * 应用投影和相机视图
     * -------------------------------------------
     */
    private val vertexShaderCode =
    // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}"

    // Use to access and set the view transformation
    private var vPMatrixHandle: Int = 0
    /**
     * -------------------------------------------
     */

    private var mProgram: Int

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
    private var vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(triangleCoords.size *4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(triangleCoords)
            position(0)
        }
    }

    init {

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }
    }

    fun draw() {
        // Add program to OpenGL ES environment 将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member 获取顶点着色器的vPosition成员句柄
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices 启用三角形顶点的句柄
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data 准备三角形的坐标数据
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // get handle to fragment shader's vColor member 获取片元着色器的vColor成员的句柄
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle 设置绘制三角形的颜色
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Draw the triangle 绘制三角形
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array 禁止顶点数组的句柄
            GLES20.glDisableVertexAttribArray(it)
        }
    }
    /**
     * 应用投影和相机视图
     * -------------------------------------------
     */
    fun draw(mvpMatrix: FloatArray) { // pass in the calculated transformation matrix
        GLES20.glUseProgram(mProgram)
        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

        // get handle to vertex shader's vPosition member 获取顶点着色器的vPosition成员句柄
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices 启用三角形顶点的句柄
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data 准备三角形的坐标数据
            GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
            )

            // get handle to fragment shader's vColor member 获取片元着色器的vColor成员的句柄
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle 设置绘制三角形的颜色
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }
    }

    /**
     * -------------------------------------------
     */

}

class Square{
    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

    private val vertexBuffer: FloatBuffer =
        // (# of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(squareCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(squareCoords)
                position(0)
            }
        }

    private val drawListBuffer: ShortBuffer =
        // (# of coordinate values * 2 bytes per short)
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

}

class ColorTriangle {
    // Use to access and set the view transformation
    private var vPMatrixHandle: Int = 0

    private val vertexShaderCode2 =
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
        "varying vec4 vColor;" +
        "attribute vec4 aColor;" +
                "void main() {" +
                "gl_Position = uMVPMatrix*vPosition;" +
                "vColor=aColor;" +
                "}"

//    private val vertexShaderCode2 =
//            "uniform mat4 uMVPMatrix;" +
//                    "attribute vec4 vPosition;" +
//                    "void main() {" +
//                    "  gl_Position = uMVPMatrix * vPosition;" +
//                    "}"

    private val fragmentShaderCode2 =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}"

    private var program: Int
    private var mColorHandle : Int = 0
    private var positionHandle: Int = 0
    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    init {

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode2)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode2)

        // create empty OpenGL ES Program
        program = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }
    }

    private var colorBuffer: FloatBuffer = ByteBuffer.allocateDirect(color2.size *4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(color2)
            position(0)
        }
    }

    private var vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(triangleCoords.size *4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(triangleCoords)
            position(0)
        }
    }

    fun drawColor(mvpMatrix: FloatArray) {
        // Add program to OpenGL ES environment 将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(program)

        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

        // get handle to vertex shader's vPosition member 获取顶点着色器的vPosition成员句柄
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition").also {

            // Enable a handle to the triangle vertices 启用三角形顶点的句柄
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data 准备三角形的坐标数据
            GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
            )

            mColorHandle = GLES20.glGetAttribLocation(program, "aColor").also {colorHandle ->

                // Set color2 for drawing the triangle 设置绘制三角形的颜色
                GLES20.glEnableVertexAttribArray(colorHandle);
                GLES20.glVertexAttribPointer(colorHandle,4,
                        GLES20.GL_FLOAT,false,
                        0,colorBuffer);
            }

            // Draw the triangle 绘制三角形
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array 禁止顶点数组的句柄
            GLES20.glDisableVertexAttribArray(it)
        }

    }

}

fun loadShader(type: Int, shaderCode: String): Int {

    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
    return GLES20.glCreateShader(type).also { shader ->

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
    }
}

