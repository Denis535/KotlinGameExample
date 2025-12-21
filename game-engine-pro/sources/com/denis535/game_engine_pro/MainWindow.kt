package com.denis535.game_engine_pro

import cnames.structs.*
import glfw.*
import kotlinx.cinterop.*
import kotlin.experimental.*

public abstract class MainWindow : AutoCloseable {

    @OptIn(ExperimentalForeignApi::class)
    private var _NativeWindow: CPointer<GLFWwindow>? = null

    @OptIn(ExperimentalForeignApi::class)
    public val IsClosed: Boolean
        get() {
            return this._NativeWindow == null
        }

    @OptIn(ExperimentalForeignApi::class)
    protected val NativeWindow: CPointer<GLFWwindow>
        get() {
            check(!this.IsClosed)
            return this._NativeWindow!!
        }

    @OptIn(ExperimentalForeignApi::class)
    public val Time: Double
        get() {
            return glfwGetTime().also { GLFW.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public val IsFullScreen: Boolean
        get() {
            check(!this.IsClosed)
            val monitor = glfwGetWindowMonitor(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
            return monitor != null
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Title: String
        get() {
            check(!this.IsClosed)
            error("Not implemented")
        }
        set(value) {
            check(!this.IsClosed)
            glfwSetWindowTitle(this.NativeWindow, value)
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Position: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            return memScoped {
                val posX = this.alloc<IntVar>()
                val posY = this.alloc<IntVar>()
                glfwGetWindowPos(this@MainWindow.NativeWindow, posX.ptr, posY.ptr).also { GLFW.ThrowErrorIfNeeded() }
                Pair(posX.value, posY.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            val (x, y) = value
            glfwSetWindowPos(this.NativeWindow, x, y).also { GLFW.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Size: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            return memScoped {
                val width = this.alloc<IntVar>()
                val height = this.alloc<IntVar>()
                glfwGetWindowSize(this@MainWindow.NativeWindow, width.ptr, height.ptr).also { GLFW.ThrowErrorIfNeeded() }
                Pair(width.value, height.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            val (width, height) = value
            glfwSetWindowSize(this.NativeWindow, width, height).also { GLFW.ThrowErrorIfNeeded() }
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

    @OptIn(ExperimentalForeignApi::class)
    public var IsCursorEnabled: Boolean
        get() {
            check(!this.IsClosed)
            return glfwGetInputMode(this.NativeWindow, GLFW_CURSOR) != GLFW_CURSOR_DISABLED
        }
        set(value) {
            check(!this.IsClosed)
            glfwSetInputMode(this.NativeWindow, GLFW_CURSOR, if (value) GLFW_CURSOR_NORMAL else GLFW_CURSOR_DISABLED)
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

    @OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
    public constructor(nativeWindowProvider: () -> CPointer<GLFWwindow>) {
        glfwInit()
        this._NativeWindow = nativeWindowProvider()
        run {
            val thisRef = StableRef.create(this)
            val thisPtr = thisRef.asCPointer()
            glfwSetWindowUserPointer(this.NativeWindow, thisPtr).also { GLFW.ThrowErrorIfNeeded() }
        }
    }

    @OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
    public constructor(title: String) : this({
        val monitor = glfwGetPrimaryMonitor()!!.also { GLFW.ThrowErrorIfNeeded() }
        val videoMode = glfwGetVideoMode(monitor)!!.also { GLFW.ThrowErrorIfNeeded() }
        glfwDefaultWindowHints().also { GLFW.ThrowErrorIfNeeded() }
        // OpenGL
        glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_NATIVE_CONTEXT_API)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        if (Platform.isDebugBinary) glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE)
        // Frame
        glfwWindowHint(GLFW_RED_BITS, 8)
        glfwWindowHint(GLFW_GREEN_BITS, 8)
        glfwWindowHint(GLFW_BLUE_BITS, 8)
        glfwWindowHint(GLFW_ALPHA_BITS, 8)
        glfwWindowHint(GLFW_DEPTH_BITS, 24)
        glfwWindowHint(GLFW_STENCIL_BITS, 8)
        // Window
        glfwWindowHint(GLFW_POSITION_X, 0)
        glfwWindowHint(GLFW_POSITION_Y, 0)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
        glfwWindowHint(GLFW_REFRESH_RATE, videoMode.pointed.refreshRate)
        glfwCreateWindow(videoMode.pointed.width, videoMode.pointed.height, title, monitor, null)!!.also { GLFW.ThrowErrorIfNeeded() }
    })

    @OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
    public constructor(title: String, width: Int = 1280, height: Int = 720, isResizable: Boolean = false) : this({
        val monitor = glfwGetPrimaryMonitor()!!.also { GLFW.ThrowErrorIfNeeded() }
        val videoMode = glfwGetVideoMode(monitor)!!.also { GLFW.ThrowErrorIfNeeded() }
        glfwDefaultWindowHints().also { GLFW.ThrowErrorIfNeeded() }
        // OpenGL
        glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_NATIVE_CONTEXT_API)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        if (Platform.isDebugBinary) glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE)
        // Frame
        glfwWindowHint(GLFW_RED_BITS, 8)
        glfwWindowHint(GLFW_GREEN_BITS, 8)
        glfwWindowHint(GLFW_BLUE_BITS, 8)
        glfwWindowHint(GLFW_ALPHA_BITS, 8)
        glfwWindowHint(GLFW_DEPTH_BITS, 24)
        glfwWindowHint(GLFW_STENCIL_BITS, 8)
        // Window
        glfwWindowHint(GLFW_POSITION_X, (videoMode.pointed.width - width) / 2)
        glfwWindowHint(GLFW_POSITION_Y, (videoMode.pointed.height - height) / 2)
        glfwWindowHint(GLFW_RESIZABLE, if (isResizable) GLFW_TRUE else GLFW_FALSE)
        glfwWindowHint(GLFW_REFRESH_RATE, videoMode.pointed.refreshRate)
        glfwCreateWindow(width, height, title, null, null)!!.also { GLFW.ThrowErrorIfNeeded() }
    })

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        run {
            val thisPtr = glfwGetWindowUserPointer(this.NativeWindow)!!.also { GLFW.ThrowErrorIfNeeded() }
            val thisRef = thisPtr.asStableRef<MainWindow>()
            thisRef.dispose()
            glfwSetWindowUserPointer(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        }
        glfwDestroyWindow(this.NativeWindow)
        this._NativeWindow = null
        glfwTerminate()
    }

    @OptIn(ExperimentalForeignApi::class)
    public open fun Show() {
        check(!this.IsClosed)
        glfwShowWindow(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
        glfwFocusWindow(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public open fun Hide() {
        check(!this.IsClosed)
        glfwHideWindow(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun SetFullScreenMode() {
        check(!this.IsClosed)
        val monitor = glfwGetPrimaryMonitor().also { GLFW.ThrowErrorIfNeeded() }
        val videoMode = glfwGetVideoMode(monitor)!!.also { GLFW.ThrowErrorIfNeeded() }
        glfwSetWindowMonitor(this.NativeWindow, monitor, 0, 0, videoMode.pointed.width, videoMode.pointed.height, videoMode.pointed.refreshRate).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun SetWindowedMode(width: Int = 1280, height: Int = 720) {
        check(!this.IsClosed)
        val monitor = glfwGetPrimaryMonitor().also { GLFW.ThrowErrorIfNeeded() }
        val videoMode = glfwGetVideoMode(monitor)!!.also { GLFW.ThrowErrorIfNeeded() }
        glfwSetWindowMonitor(this.NativeWindow, null, (videoMode.pointed.width - width) / 2, (videoMode.pointed.height - height) / 2, width, height, videoMode.pointed.refreshRate).also { GLFW.ThrowErrorIfNeeded() }
    }

}
