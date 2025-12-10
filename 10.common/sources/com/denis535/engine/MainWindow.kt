package com.denis535.engine

import kotlinx.cinterop.*
import glfw.*

public class MainWindow : AutoCloseable {

    @OptIn(ExperimentalForeignApi::class)
    private var _NativeWindowPointer: CPointer<cnames.structs.GLFWwindow>? = null

    @OptIn(ExperimentalForeignApi::class)
    public val IsClosed: Boolean
        get() {
            return this._NativeWindowPointer == null
        }

    @OptIn(ExperimentalForeignApi::class)
    public val NativeWindowPointer: CPointer<cnames.structs.GLFWwindow>
        get() {
            check(!this.IsClosed)
            return this._NativeWindowPointer!!
        }

    @OptIn(ExperimentalForeignApi::class)
    public val Time: Double
        get() {
            check(!this.IsClosed)
            return glfwGetTime().also { GLFW2.ThrowErrorIfNeeded() }
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
            glfwSetWindowTitle(this.NativeWindowPointer, value)
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Position: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            return memScoped {
                val posX = this.alloc<IntVar>()
                val posY = this.alloc<IntVar>()
                glfwGetWindowPos(this@MainWindow.NativeWindowPointer, posX.ptr, posY.ptr).also { GLFW2.ThrowErrorIfNeeded() }
                Pair(posX.value, posY.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            val (x, y) = value
            glfwSetWindowPos(this.NativeWindowPointer, x, y).also { GLFW2.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Size: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            return memScoped {
                val width = this.alloc<IntVar>()
                val height = this.alloc<IntVar>()
                glfwGetWindowSize(this@MainWindow.NativeWindowPointer, width.ptr, height.ptr).also { GLFW2.ThrowErrorIfNeeded() }
                Pair(width.value, height.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            val (width, height) = value
            glfwSetWindowSize(this.NativeWindowPointer, width, height).also { GLFW2.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public val IsVisible: Boolean
        get() {
            check(!this.IsClosed)
            return glfwGetWindowAttrib(this.NativeWindowPointer, GLFW_VISIBLE) == GLFW_TRUE
        }

    @OptIn(ExperimentalForeignApi::class)
    public val IsIconified: Boolean
        get() {
            check(!this.IsClosed)
            return glfwGetWindowAttrib(this.NativeWindowPointer, GLFW_ICONIFIED) == GLFW_TRUE
        }

    @OptIn(ExperimentalForeignApi::class)
    public val IsFocused: Boolean
        get() {
            check(!this.IsClosed)
            return glfwGetWindowAttrib(this.NativeWindowPointer, GLFW_FOCUSED) == GLFW_TRUE
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsFullscreen: Boolean
        get() {
            check(!this.IsClosed)
            val monitor = glfwGetWindowMonitor(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
            return monitor != null
        }
        set(value) {
            check(!this.IsClosed)
            val monitor = glfwGetPrimaryMonitor().also { GLFW2.ThrowErrorIfNeeded() }
            val videoMode = glfwGetVideoMode(monitor)!!.also { GLFW2.ThrowErrorIfNeeded() }
            if (value) {
                glfwSetWindowMonitor(this.NativeWindowPointer, monitor, 0, 0, videoMode.pointed.width, videoMode.pointed.height, videoMode.pointed.refreshRate).also { GLFW2.ThrowErrorIfNeeded() }
            } else {
                glfwSetWindowMonitor(this.NativeWindowPointer, null, (videoMode.pointed.width - 1280) / 2, (videoMode.pointed.height - 720) / 2, 1280, 720, 0).also { GLFW2.ThrowErrorIfNeeded() }
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsClosingRequested: Boolean
        get() {
            check(!this.IsClosed)
            return glfwWindowShouldClose(this.NativeWindowPointer) == GLFW_TRUE
        }
        set(value) {
            check(!this.IsClosed)
            glfwSetWindowShouldClose(this.NativeWindowPointer, if (value) GLFW_TRUE else GLFW_FALSE)
        }

    @OptIn(ExperimentalForeignApi::class)
    public constructor(title: String, width: Int = 1280, height: Int = 720) {
        glfwInit().also { GLFW2.ThrowErrorIfNeeded() }
        this._NativeWindowPointer = run {
            val monitor = glfwGetPrimaryMonitor()!!.also { GLFW2.ThrowErrorIfNeeded() }
            val videoMode = glfwGetVideoMode(monitor)!!.also { GLFW2.ThrowErrorIfNeeded() }
            glfwDefaultWindowHints().also { GLFW2.ThrowErrorIfNeeded() }
            glfwWindowHint(GLFW_POSITION_X, (videoMode.pointed.width - width) / 2)
            glfwWindowHint(GLFW_POSITION_Y, (videoMode.pointed.height - height) / 2)
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
            glfwCreateWindow(width, height, title, null, null).also { GLFW2.ThrowErrorIfNeeded() }
        }
        this.Title = title
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        this._NativeWindowPointer = run {
            glfwDestroyWindow(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
            null
        }
        glfwTerminate()
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun Show() {
        check(!this.IsClosed)
        glfwMakeContextCurrent(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
        glfwSwapInterval(1).also { GLFW2.ThrowErrorIfNeeded() }
        glfwShowWindow(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
        glfwFocusWindow(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun Hide() {
        check(!this.IsClosed)
        glfwHideWindow(this.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
    }

}
