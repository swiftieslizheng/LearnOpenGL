package com.lizheng.learnopengldemo

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.lizheng.learnopengldemo.base.BaseGLRenderer
import com.lizheng.learnopengldemo.cube.CubeGLRenderer
import com.lizheng.learnopengldemo.oval.OvalGLRenderer

/**
 * GLSurfaceView 是一种专用视图，您可以在其中绘制 OpenGL ES 图形。
 * 它本身并没有很大的作用。对象的实际绘制由您在此视图中设置的 GLSurfaceView.Renderer 控制。
 * 实际上，此对象的代码过于单薄，以至于您想要跳过对它的扩展，直接创建一个未经修改的 GLSurfaceView 实例，
 * 但请不要这样操作。您需要扩展此类才能捕获触摸事件
 */

class MyGLSurfaceView(context:Context, type:Int):GLSurfaceView(context) {
    private lateinit var renderer:BaseGLRenderer
    private var previousX: Float = 0f
    private var previousY: Float = 0f

    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        when(type){
            0 -> {
                renderer = OvalGLRenderer()
                setRenderer(renderer)
            }
            1 -> {
                renderer = MyGLRenderer()
                setRenderer(renderer)
                renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(renderer is OvalGLRenderer){
            return false
        }
        val x: Float = event.x
        val y: Float = event.y
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {

                var dx: Float = x - previousX
                var dy: Float = y - previousY

                // reverse direction of rotation above the mid-line
                if (y > height / 2) {
                    dx *= -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x < width / 2) {
                    dy *= -1
                }

                (renderer as MyGLRenderer).angle += (dx + dy) * Companion.TOUCH_SCALE_FACTOR
                requestRender()
            }
        }
        previousX = x
        previousY = y
        return true
    }

    companion object {
        private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f
    }

}