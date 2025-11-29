package com.denis535.kotlin_game_example

import org.lwjgl.glfw.GLFW

public object Engine {

    public val Time: Double
        get() {
            return GLFW.glfwGetTime().also { GLFW2.ThrowErrorIfNeeded() }
        }

    public fun Initialize() {
        GLFW.glfwInit().also { GLFW2.ThrowErrorIfNeeded() }
    }

    public fun Deinitialize() {
        GLFW.glfwTerminate()
    }

}

public object MainWindow {

    internal var Id: Long = 0
        private set

    public val IsCreated: Boolean
        get() {
            return this.Id != 0L
        }

    public val IsVisible: Boolean
        get() {
            check(this.IsCreated)
            return GLFW.glfwGetWindowAttrib(this.Id, GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE
        }

    public val IsFocused: Boolean
        get() {
            check(this.IsCreated)
            return GLFW.glfwGetWindowAttrib(this.Id, GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE
        }

    public val Position: Pair<Int, Int>
        get() {
            check(this.IsCreated)
            val posX = IntArray(1)
            val posY = IntArray(1)
            GLFW.glfwGetWindowPos(this.Id, posX, posY).also { GLFW2.ThrowErrorIfNeeded() }
            return posX[0] to posY[0]
        }

    public val Size: Pair<Int, Int>
        get() {
            check(this.IsCreated)
            val width = IntArray(1)
            val height = IntArray(1)
            GLFW.glfwGetWindowSize(this.Id, width, height).also { GLFW2.ThrowErrorIfNeeded() }
            return width[0] to height[0]
        }

    public var IsFullscreen: Boolean
        get() {
            check(this.IsCreated)
            val monitor = GLFW.glfwGetWindowMonitor(this.Id).also { GLFW2.ThrowErrorIfNeeded() }
            return monitor != 0L
        }
        set(value) {
            check(this.IsCreated)
            val monitor = GLFW.glfwGetPrimaryMonitor().also { GLFW2.ThrowErrorIfNeeded() }
            val videoMode = GLFW.glfwGetVideoMode(monitor)!!.also { GLFW2.ThrowErrorIfNeeded() }
            if (value) {
                GLFW.glfwSetWindowMonitor(this.Id, monitor, 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate()).also { GLFW2.ThrowErrorIfNeeded() }
            } else {
                GLFW.glfwSetWindowMonitor(this.Id, 0, (videoMode.width() - 1280) / 2, (videoMode.height() - 720) / 2, 1280, 720, 0).also { GLFW2.ThrowErrorIfNeeded() }
            }
        }

    public var IsClosing: Boolean
        get() {
            check(this.IsCreated)
            return GLFW.glfwWindowShouldClose(this.Id)
        }
        set(value) {
            check(this.IsCreated)
            GLFW.glfwSetWindowShouldClose(this.Id, true)
        }

    public fun Create(title: String, width: Int = 1280, height: Int = 720) {
        check(!this.IsCreated)
        check(!MainLoop.IsRunning)
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
        this.Id = GLFW.glfwCreateWindow(width, height, title, 0, 0).also { GLFW2.ThrowErrorIfNeeded() }
    }

    public fun Destroy() {
        check(this.IsCreated)
        check(!MainLoop.IsRunning)
        GLFW.glfwDestroyWindow(this.Id).also { GLFW2.ThrowErrorIfNeeded() }
        this.Id = 0L
    }

    public fun Show() {
        check(this.IsCreated)
        GLFW.glfwMakeContextCurrent(this.Id).also { GLFW2.ThrowErrorIfNeeded() }
        GLFW.glfwSwapInterval(1).also { GLFW2.ThrowErrorIfNeeded() }
        GLFW.glfwShowWindow(this.Id).also { GLFW2.ThrowErrorIfNeeded() }
        GLFW.glfwFocusWindow(this.Id).also { GLFW2.ThrowErrorIfNeeded() }
    }

    public fun Hide() {
        check(this.IsCreated)
        GLFW.glfwHideWindow(this.Id).also { GLFW2.ThrowErrorIfNeeded() }
    }

}

public object MainLoop {

    public var IsRunning: Boolean = false
        get() {
            return field
        }
        private set(value) {
            check(MainWindow.IsCreated)
            field = value
        }

    public var NumberOfFrame: Int = 0
        get() {
            check(this.IsRunning)
            return field
        }
        private set(value) {
            check(this.IsRunning)
            field = value
        }

    public fun Run() {
        check(!this.IsRunning)
        this.IsRunning = true
        this.NumberOfFrame = 0
        while (!MainWindow.IsClosing) {
            GLFW.glfwPollEvents().also { GLFW2.ThrowErrorIfNeeded() }
            if (GLFW.glfwGetKey(MainWindow.Id, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(MainWindow.Id, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS) {
                if (GLFW.glfwGetKey(MainWindow.Id, GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS) {
                    MainWindow.IsFullscreen = !MainWindow.IsFullscreen
                }
            }
            // Update()
            // Draw()
            GLFW.glfwSwapBuffers(MainWindow.Id).also { GLFW2.ThrowErrorIfNeeded() }
            this.NumberOfFrame++
        }
        this.IsRunning = false
    }

}
