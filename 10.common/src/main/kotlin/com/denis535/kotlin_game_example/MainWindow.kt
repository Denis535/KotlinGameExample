package com.denis535.kotlin_game_example

import org.lwjgl.glfw.GLFW

public class MainWindow : AutoCloseable {

    public var NativePointer: Long = 0
        get() {
            return field
        }
        private set(value) {
            field = value
        }

    public val IsClosed: Boolean
        get() {
            return this.NativePointer == 0L
        }

    public var IsClosingRequested: Boolean
        get() {
            check(!this.IsClosed)
            return GLFW.glfwWindowShouldClose(this.NativePointer)
        }
        set(value) {
            check(!this.IsClosed)
            GLFW.glfwSetWindowShouldClose(this.NativePointer, value)
        }

    public val IsVisible: Boolean
        get() {
            check(!this.IsClosed)
            return GLFW.glfwGetWindowAttrib(this.NativePointer, GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE
        }

    public val IsIconified: Boolean
        get() {
            check(!this.IsClosed)
            return GLFW.glfwGetWindowAttrib(this.NativePointer, GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE
        }

    public val IsFocused: Boolean
        get() {
            check(!this.IsClosed)
            return GLFW.glfwGetWindowAttrib(this.NativePointer, GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE
        }

    public var IsFullscreen: Boolean
        get() {
            check(!this.IsClosed)
            val monitor = GLFW.glfwGetWindowMonitor(this.NativePointer).also { GLFW2.ThrowErrorIfNeeded() }
            return monitor != 0L
        }
        set(value) {
            check(!this.IsClosed)
            val monitor = GLFW.glfwGetPrimaryMonitor().also { GLFW2.ThrowErrorIfNeeded() }
            val videoMode = GLFW.glfwGetVideoMode(monitor)!!.also { GLFW2.ThrowErrorIfNeeded() }
            if (value) {
                GLFW.glfwSetWindowMonitor(this.NativePointer, monitor, 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate()).also { GLFW2.ThrowErrorIfNeeded() }
            } else {
                GLFW.glfwSetWindowMonitor(this.NativePointer, 0, (videoMode.width() - 1280) / 2, (videoMode.height() - 720) / 2, 1280, 720, 0).also { GLFW2.ThrowErrorIfNeeded() }
            }
        }

//    public var Title: String
//        get() {
//        }
//        set(value) {
//            GLFW.glfwSetWindowTitle(this.NativePointer, value)
//        }

    public val Position: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            val posX = IntArray(1)
            val posY = IntArray(1)
            GLFW.glfwGetWindowPos(this.NativePointer, posX, posY).also { GLFW2.ThrowErrorIfNeeded() }
            return posX[0] to posY[0]
        }

    public val Size: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            val width = IntArray(1)
            val height = IntArray(1)
            GLFW.glfwGetWindowSize(this.NativePointer, width, height).also { GLFW2.ThrowErrorIfNeeded() }
            return width[0] to height[0]
        }

    public constructor(title: String, width: Int = 1280, height: Int = 720) {
        GLFW.glfwInit().also { GLFW2.ThrowErrorIfNeeded() }
        this.NativePointer = run {
            val monitor = GLFW.glfwGetPrimaryMonitor().also { GLFW2.ThrowErrorIfNeeded() }
            val videoMode = GLFW.glfwGetVideoMode(monitor)!!.also { GLFW2.ThrowErrorIfNeeded() }
            GLFW.glfwDefaultWindowHints().also { GLFW2.ThrowErrorIfNeeded() }
            GLFW.glfwWindowHint(GLFW.GLFW_POSITION_X, (videoMode.width() - width) / 2)
            GLFW.glfwWindowHint(GLFW.GLFW_POSITION_Y, (videoMode.height() - height) / 2)
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_CREATION_API, GLFW.GLFW_NATIVE_CONTEXT_API)
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4)
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6)
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE)
            GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, 8)
            GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, 8)
            GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, 8)
            GLFW.glfwWindowHint(GLFW.GLFW_ALPHA_BITS, 8)
            GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24)
            GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 8)
            GLFW.glfwCreateWindow(width, height, title, 0, 0).also { GLFW2.ThrowErrorIfNeeded() }
        }
    }

    public override fun close() {
        check(!this.IsClosed)
        this.NativePointer = run {
            GLFW.glfwDestroyWindow(this.NativePointer).also { GLFW2.ThrowErrorIfNeeded() }
            0L
        }
        GLFW.glfwTerminate()
    }

    public fun Show() {
        check(!this.IsClosed)
        GLFW.glfwMakeContextCurrent(this.NativePointer).also { GLFW2.ThrowErrorIfNeeded() }
        GLFW.glfwSwapInterval(1).also { GLFW2.ThrowErrorIfNeeded() }
        GLFW.glfwShowWindow(this.NativePointer).also { GLFW2.ThrowErrorIfNeeded() }
        GLFW.glfwFocusWindow(this.NativePointer).also { GLFW2.ThrowErrorIfNeeded() }
    }

    public fun Hide() {
        check(!this.IsClosed)
        GLFW.glfwHideWindow(this.NativePointer).also { GLFW2.ThrowErrorIfNeeded() }
    }

}
