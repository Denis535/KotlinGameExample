//package com.denis535.engine
//
//import org.lwjgl.glfw.GLFW
//
//public class MainWindow : AutoCloseable {
//
//    private var _NativeWindowPointer: Long = 0
//
//    public val IsClosed: Boolean
//        get() {
//            return this._NativeWindowPointer == 0L
//        }
//
//    public val NativeWindowPointer: Long
//        get() {
//            check(!this.IsClosed)
//            return this._NativeWindowPointer
//        }
//
//    public val Time: Double
//        get() {
//            check(!this.IsClosed)
//            return GLFW.glfwGetTime().also { GLFW2.ThrowErrorIfNeeded() }
//        }
//
//    public var Title: String = ""
//        get() {
//            check(!this.IsClosed)
//            return field
//        }
//        set(value) {
//            check(!this.IsClosed)
//            field = value
//            GLFW.glfwSetWindowTitle(this.NativeWindowPointer, value)
//        }
//
//    public var Position: Pair<Int, Int>
//        get() {
//            check(!this.IsClosed)
//            val posX = IntArray(1)
//            val posY = IntArray(1)
//            GLFW.glfwGetWindowPos(this.NativeWindowPointer, posX, posY).also { GLFW2.ThrowErrorIfNeeded() }
//            return posX[0] to posY[0]
//        }
//        set(value) {
//            check(!this.IsClosed)
//            val (x, y) = value
//            GLFW.glfwSetWindowPos(this.NativeWindowPointer, x, y).also { GLFW2.ThrowErrorIfNeeded() }
//        }
//
//    public var Size: Pair<Int, Int>
//        get() {
//            check(!this.IsClosed)
//            val width = IntArray(1)
//            val height = IntArray(1)
//            GLFW.glfwGetWindowSize(this.NativeWindowPointer, width, height).also { GLFW2.ThrowErrorIfNeeded() }
//            return width[0] to height[0]
//        }
//        set(value) {
//            check(!this.IsClosed)
//            val (width, height) = value
//            GLFW.glfwSetWindowSize(this.NativeWindowPointer, width, height).also { GLFW2.ThrowErrorIfNeeded() }
//        }
//
//    public val IsVisible: Boolean
//        get() {
//            check(!this.IsClosed)
//            return GLFW.glfwGetWindowAttrib(this.NativeWindowPointer, GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE
//        }
//
//    public val IsIconified: Boolean
//        get() {
//            check(!this.IsClosed)
//            return GLFW.glfwGetWindowAttrib(this.NativeWindowPointer, GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE
//        }
//
//    public val IsFocused: Boolean
//        get() {
//            check(!this.IsClosed)
//            return GLFW.glfwGetWindowAttrib(this.NativeWindowPointer, GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE
//        }
//
//    public var IsFullscreen: Boolean
//        get() {
//            check(!this.IsClosed)
//            val monitor = GLFW.glfwGetWindowMonitor(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
//            return monitor != 0L
//        }
//        set(value) {
//            check(!this.IsClosed)
//            val monitor = GLFW.glfwGetPrimaryMonitor().also { GLFW2.ThrowErrorIfNeeded() }
//            val videoMode = GLFW.glfwGetVideoMode(monitor)!!.also { GLFW2.ThrowErrorIfNeeded() }
//            if (value) {
//                GLFW.glfwSetWindowMonitor(this.NativeWindowPointer, monitor, 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate()).also { GLFW2.ThrowErrorIfNeeded() }
//            } else {
//                GLFW.glfwSetWindowMonitor(this.NativeWindowPointer, 0, (videoMode.width() - 1280) / 2, (videoMode.height() - 720) / 2, 1280, 720, 0).also { GLFW2.ThrowErrorIfNeeded() }
//            }
//        }
//
//    public var IsClosingRequested: Boolean
//        get() {
//            check(!this.IsClosed)
//            return GLFW.glfwWindowShouldClose(this.NativeWindowPointer)
//        }
//        set(value) {
//            check(!this.IsClosed)
//            GLFW.glfwSetWindowShouldClose(this.NativeWindowPointer, value)
//        }
//
//    public constructor(title: String, width: Int = 1280, height: Int = 720) {
//        GLFW.glfwInit().also { GLFW2.ThrowErrorIfNeeded() }
//        this._NativeWindowPointer = run {
//            val monitor = GLFW.glfwGetPrimaryMonitor().also { GLFW2.ThrowErrorIfNeeded() }
//            val videoMode = GLFW.glfwGetVideoMode(monitor)!!.also { GLFW2.ThrowErrorIfNeeded() }
//            GLFW.glfwDefaultWindowHints().also { GLFW2.ThrowErrorIfNeeded() }
//            GLFW.glfwWindowHint(GLFW.GLFW_POSITION_X, (videoMode.width() - width) / 2)
//            GLFW.glfwWindowHint(GLFW.GLFW_POSITION_Y, (videoMode.height() - height) / 2)
//            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_CREATION_API, GLFW.GLFW_NATIVE_CONTEXT_API)
//            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4)
//            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6)
//            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
//            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE)
//            GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, 8)
//            GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, 8)
//            GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, 8)
//            GLFW.glfwWindowHint(GLFW.GLFW_ALPHA_BITS, 8)
//            GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24)
//            GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 8)
//            GLFW.glfwCreateWindow(width, height, title, 0, 0).also { GLFW2.ThrowErrorIfNeeded() }
//        }
//        this.Title = title
//    }
//
//    public override fun close() {
//        check(!this.IsClosed)
//        this._NativeWindowPointer = run {
//            GLFW.glfwDestroyWindow(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
//            0L
//        }
//        GLFW.glfwTerminate()
//    }
//
//    public fun Show() {
//        check(!this.IsClosed)
//        GLFW.glfwMakeContextCurrent(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
//        GLFW.glfwSwapInterval(1).also { GLFW2.ThrowErrorIfNeeded() }
//        GLFW.glfwShowWindow(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
//        GLFW.glfwFocusWindow(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
//    }
//
//    public fun Hide() {
//        check(!this.IsClosed)
//        GLFW.glfwHideWindow(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
//    }
//
//}
