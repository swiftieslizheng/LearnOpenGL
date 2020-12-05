package com.lizheng.learnopengldemo.oval

import com.lizheng.learnopengldemo.base.BaseGLRenderer

open class OvalGLRenderer: BaseGLRenderer() {
    private lateinit var oval: Oval

    override fun onCreatedView() {
        oval = Oval()

    }

    override fun onDrawView() {
        oval.draw(vPMatrix)
    }

    override fun onViewChanged(width: Int, height: Int) {

    }

}