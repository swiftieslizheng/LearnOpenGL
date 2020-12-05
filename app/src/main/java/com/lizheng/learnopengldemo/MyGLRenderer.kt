package com.lizheng.learnopengldemo

import com.lizheng.learnopengldemo.base.BaseGLRenderer

open class MyGLRenderer:BaseGLRenderer(){
    private lateinit var triangle:Triangle
    private lateinit var colortriangle:ColorTriangle

    override fun onCreatedView() {
        //        triangle = Triangle()
        colortriangle = ColorTriangle()
    }

    override fun onDrawView() {
//        triangle.draw()
//        triangle.draw(vPMatrix)
        colortriangle.drawColor(vPMatrix)
    }

    override fun onViewChanged(width: Int, height: Int) {

    }

}

