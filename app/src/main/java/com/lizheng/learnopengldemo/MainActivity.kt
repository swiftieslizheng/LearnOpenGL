package com.lizheng.learnopengldemo

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lizheng.learnopengldemo.constant.cubeType
import com.lizheng.learnopengldemo.constant.ovalType
import com.lizheng.learnopengldemo.constant.triangleType
import com.lizheng.learnopengldemo.cube.CubeGLSurfaceView

class MainActivity : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gLView = MyGLSurfaceView(this, triangleType)
//        gLView = CubeGLSurfaceView(this)
        setContentView(gLView)
    }
}