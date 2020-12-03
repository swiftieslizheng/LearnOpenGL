package com.lizheng.learnopengldemo

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 渲染程序类
 *  onSurfaceCreated() - 调用一次以设置视图的 OpenGL ES 环境。
    onDrawFrame() - 每次重新绘制视图时调用。
    onSurfaceChanged() - 当视图的几何图形发生变化（例如当设备的屏幕方向发生变化）时调用。

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

    override fun onDrawFrame(p0: GL10?) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        triangle.draw()
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        // initialize a triangle
        mTriangle = Triangle()
        // initialize a square
        mSquare = Square()
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        triangle = Triangle()
    }

}

