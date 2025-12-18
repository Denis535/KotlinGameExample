package com.denis535.engine

import kotlinx.cinterop.*
import glfw.*

public class MainWindow : AutoCloseable {

    @OptIn(ExperimentalForeignApi::class)
    private var _NativeWindow: CPointer<cnames.structs.GLFWwindow>? = null

    @OptIn(ExperimentalForeignApi::class)
    public val IsClosed: Boolean
        get() {
            return this._NativeWindow == null
        }

    @OptIn(ExperimentalForeignApi::class)
    public val NativeWindow: CPointer<cnames.structs.GLFWwindow>
        get() {
            check(!this.IsClosed)
            return this._NativeWindow!!
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Title: String = ""
        get() {
            check(!this.IsClosed)
            return field
        }
        set(value) {
            check(!this.IsClosed)
            field = value
            glfwSetWindowTitle(this.NativeWindow, value)
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Position: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            return memScoped {
                val posX = this.alloc<IntVar>()
                val posY = this.alloc<IntVar>()
                glfwGetWindowPos(this@MainWindow.NativeWindow, posX.ptr, posY.ptr).also { GLFW2.ThrowErrorIfNeeded() }
                Pair(posX.value, posY.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            val (x, y) = value
            glfwSetWindowPos(this.NativeWindow, x, y).also { GLFW2.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Size: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            return memScoped {
                val width = this.alloc<IntVar>()
                val height = this.alloc<IntVar>()
                glfwGetWindowSize(this@MainWindow.NativeWindow, width.ptr, height.ptr).also { GLFW2.ThrowErrorIfNeeded() }
                Pair(width.value, height.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            val (width, height) = value
            glfwSetWindowSize(this.NativeWindow, width, height).also { GLFW2.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsFullscreen: Boolean
        get() {
            check(!this.IsClosed)
            val monitor = glfwGetWindowMonitor(this.NativeWindow).also { GLFW2.ThrowErrorIfNeeded() }
            return monitor != null
        }
        set(value) {
            check(!this.IsClosed)
            val monitor = glfwGetPrimaryMonitor().also { GLFW2.ThrowErrorIfNeeded() }
            val videoMode = glfwGetVideoMode(monitor)!!.also { GLFW2.ThrowErrorIfNeeded() }
            if (value) {
                glfwSetWindowMonitor(this.NativeWindow, monitor, 0, 0, videoMode.pointed.width, videoMode.pointed.height, videoMode.pointed.refreshRate).also { GLFW2.ThrowErrorIfNeeded() }
            } else {
                glfwSetWindowMonitor(this.NativeWindow, null, (videoMode.pointed.width - 1280) / 2, (videoMode.pointed.height - 720) / 2, 1280, 720, 0).also { GLFW2.ThrowErrorIfNeeded() }
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    public val IsVisible: Boolean
        get() {
            check(!this.IsClosed)
            return glfwGetWindowAttrib(this.NativeWindow, GLFW_VISIBLE) == GLFW_TRUE
        }

    @OptIn(ExperimentalForeignApi::class)
    public val IsIconified: Boolean
        get() {
            check(!this.IsClosed)
            return glfwGetWindowAttrib(this.NativeWindow, GLFW_ICONIFIED) == GLFW_TRUE
        }

    @OptIn(ExperimentalForeignApi::class)
    public val IsFocused: Boolean
        get() {
            check(!this.IsClosed)
            return glfwGetWindowAttrib(this.NativeWindow, GLFW_FOCUSED) == GLFW_TRUE
        }

//    @OptIn(ExperimentalForeignApi::class)
//    public var IsCursorVisible
//        get() {
//        }
//        set(value) {
//        }

    @OptIn(ExperimentalForeignApi::class)
    public val Time: Double
        get() {
            check(!this.IsClosed)
            return glfwGetTime().also { GLFW2.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsClosingRequested: Boolean
        get() {
            check(!this.IsClosed)
            return glfwWindowShouldClose(this.NativeWindow) == GLFW_TRUE
        }
        set(value) {
            check(!this.IsClosed)
            glfwSetWindowShouldClose(this.NativeWindow, if (value) GLFW_TRUE else GLFW_FALSE)
        }

    @OptIn(ExperimentalForeignApi::class)
    public constructor(title: String, width: Int = 1280, height: Int = 720) {
        glfwInit().also { GLFW2.ThrowErrorIfNeeded() }
        this._NativeWindow = run {
            val monitor = glfwGetPrimaryMonitor()!!.also { GLFW2.ThrowErrorIfNeeded() }
            val videoMode = glfwGetVideoMode(monitor)!!.also { GLFW2.ThrowErrorIfNeeded() }
            glfwDefaultWindowHints().also { GLFW2.ThrowErrorIfNeeded() }
            glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_NATIVE_CONTEXT_API)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6)
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
            glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE)
            glfwWindowHint(GLFW_RED_BITS, 8)
            glfwWindowHint(GLFW_GREEN_BITS, 8)
            glfwWindowHint(GLFW_BLUE_BITS, 8)
            glfwWindowHint(GLFW_ALPHA_BITS, 8)
            glfwWindowHint(GLFW_DEPTH_BITS, 24)
            glfwWindowHint(GLFW_STENCIL_BITS, 8)
            val window = glfwCreateWindow(width, height, title, null, null).also { GLFW2.ThrowErrorIfNeeded() }
            glfwSetWindowPos(window, (videoMode.pointed.width - width) / 2, (videoMode.pointed.height - height) / 2).also { GLFW2.ThrowErrorIfNeeded() }
            window
        }
        this.Title = title
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        this._NativeWindow = run {
            glfwDestroyWindow(this.NativeWindow).also { GLFW2.ThrowErrorIfNeeded() }
            null
        }
        glfwTerminate()
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun Show() {
        check(!this.IsClosed)
        glfwShowWindow(this.NativeWindow).also { GLFW2.ThrowErrorIfNeeded() }
        glfwFocusWindow(this.NativeWindow).also { GLFW2.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun Hide() {
        check(!this.IsClosed)
        glfwHideWindow(this.NativeWindow).also { GLFW2.ThrowErrorIfNeeded() }
    }

}
