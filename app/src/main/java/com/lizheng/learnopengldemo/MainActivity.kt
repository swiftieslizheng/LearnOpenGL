package com.lizheng.learnopengldemo

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lizheng.learnopengldemo.constant.ovalType
import com.lizheng.learnopengldemo.constant.triangleType

class MainActivity : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gLView = MyGLSurfaceView(this, ovalType)
        setContentView(gLView)
    }
}