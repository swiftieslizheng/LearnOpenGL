package com.lizheng.learnopengldemo

import android.content.Context
import android.opengl.GLSurfaceView
import com.lizheng.learnopengldemo.base.BaseGLRenderer
import com.lizheng.learnopengldemo.oval.OvalGLRenderer

/**
 * GLSurfaceView 是一种专用视图，您可以在其中绘制 OpenGL ES 图形。
 * 它本身并没有很大的作用。对象的实际绘制由您在此视图中设置的 GLSurfaceView.Renderer 控制。
 * 实际上，此对象的代码过于单薄，以至于您想要跳过对它的扩展，直接创建一个未经修改的 GLSurfaceView 实例，
 * 但请不要这样操作。您需要扩展此类才能捕获触摸事件
 */

class MyGLSurfaceView(context:Context, type:Int):GLSurfaceView(context) {
    private lateinit var renderer:BaseGLRenderer

    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        when(type){
            0 -> renderer = OvalGLRenderer()
            1 -> renderer = MyGLRenderer()
        }

        // Set the Renderer for drawing on the GLSurfaceView
//        setRenderer(renderer)
        setRenderer(CubeGLRenderer())
    }

}