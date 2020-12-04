package com.lizheng.learnopengldemo

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 渲染程序类
 *  onSurfaceCreated() - 调用一次以设置视图的 OpenGL ES 环境。
    onDrawFrame() - 每次重新绘制视图时调用。
    onSurfaceChanged() - 当视图的几何图形发生变化（例如当设备的屏幕方向发生变化）时调用。

右手坐标系
以右手握住z轴，当右手的四指从正向x轴以π/2角度转向正向y轴，大拇指的指向的的就是z轴的正向，这样的三条坐标轴就组成了一个空间直角坐标系，点O叫做坐标原点。

OpenGL ES采用的是右手坐标，选取屏幕中心为原点，从原点到屏幕边缘默认长度为1，
也就是说默认情况下，从原点到（1,0,0）的距离和到（0,1,0）的距离在屏幕上展示的并不相同。
即向右为X正轴方向，向左为X负轴方向，向上为Y轴正轴方向，向下为Y轴负轴方向，屏幕面垂直向上为Z轴正轴方向，垂直向下为Z轴负轴方向。

初始化形状
    在进行任何绘制之前，您必须初始化并加载您打算绘制的形状。
    除非您在程序中使用的形状结构（原始坐标）在执行过程中发生变化，
    否则应该在渲染程序的 onSurfaceCreated() 方法中对它们进行初始化，以提高内存和处理效率。

绘制形状
    使用 OpenGL ES 2.0 绘制定义的形状需要大量代码，因为您必须向图形渲染管道提供大量详细信息。具体来说，您必须定义以下内容：
        顶点着色程序 - 用于渲染形状的顶点的 OpenGL ES 图形代码。
        片段着色程序 - 用于使用颜色或纹理渲染形状面的 OpenGL ES 代码。
        程序 - 包含您希望用于绘制一个或多个形状的着色程序的 OpenGL ES 对象。
    您至少需要一个顶点着色程序绘制形状，以及一个 Fragment 着色程序为该形状着色。您还必须对这些着色程序进行编译，然后将其添加到之后用于绘制形状的 OpenGL ES 程序中。


 */
class MyGLRenderer:GLSurfaceView.Renderer {
    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square

    private lateinit var triangle:Triangle
    private lateinit var colortriangle:ColorTriangle

    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    override fun onDrawFrame(p0: GL10?) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
//        triangle.draw()

        //应用投影和相机视图
        //相机视图转换使用 Matrix.setLookAtM() 方法进行计算，然后与之前计算的投影矩阵合并
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix,0,projectionMatrix,0,viewMatrix,0)
//        triangle.draw(vPMatrix)
        colortriangle.drawColor(vPMatrix)
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        // initialize a triangle
        mTriangle = Triangle()
        // initialize a square
        mSquare = Square()

        //应用投影和相机视图
        val ratio:Float = width.toFloat() / height.toFloat()
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix,0,-ratio,ratio,-1f,1f,3f,7f)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
//        triangle = Triangle()
        colortriangle = ColorTriangle()
    }

}

