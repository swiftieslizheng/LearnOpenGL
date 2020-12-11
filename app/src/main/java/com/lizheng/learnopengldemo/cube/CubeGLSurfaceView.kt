package com.lizheng.learnopengldemo.cube

import android.content.Context
import android.opengl.GLSurfaceView

class CubeGLSurfaceView(context: Context): GLSurfaceView(context) {
    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)
        setRenderer(CubeGLRenderer())
    }
}