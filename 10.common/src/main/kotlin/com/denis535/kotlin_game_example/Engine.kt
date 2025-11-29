package com.denis535.kotlin_game_example

import org.lwjgl.glfw.GLFW

public object Engine {

    internal var Window: Long = 0
        private set

    public val IsWindowCreated: Boolean
        get() {
            return this.Window != 0L
        }

    public var IsWindowFullscreen: Boolean
        get() {
            check(this.IsWindowCreated)
            val monitor = GLFW.glfwGetWindowMonitor(this.Window).also { GLFW2.ThrowErrorIfNeeded() }
            return monitor != 0L
        }
        set(value) {
            check(this.IsWindowCreated)
            val monitor = GLFW.glfwGetPrimaryMonitor().also { GLFW2.ThrowErrorIfNeeded() }
            val videoMode = GLFW.glfwGetVideoMode(monitor)!!.also { GLFW2.ThrowErrorIfNeeded() }
            if (value) {
                GLFW.glfwSetWindowMonitor(this.Window, monitor, 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate()).also { GLFW2.ThrowErrorIfNeeded() }
            } else {
                GLFW.glfwSetWindowMonitor(this.Window, 0, (videoMode.width() - 1280) / 2, (videoMode.height() - 720) / 2, 1280, 720, 0).also { GLFW2.ThrowErrorIfNeeded() }
            }
        }

    public val WindowPosition: Pair<Int, Int>
        get() {
            check(this.IsWindowCreated)
            val posX = IntArray(1)
            val posY = IntArray(1)
            GLFW.glfwGetWindowPos(this.Window, posX, posY)
            return posX[0] to posY[0]
        }

    public val WindowSize: Pair<Int, Int>
        get() {
            check(this.IsWindowCreated)
            val width = IntArray(1)
            val height = IntArray(1)
            GLFW.glfwGetWindowSize(this.Window, width, height)
            return width[0] to height[0]
        }

    public val Time: Double
        get() {
            check(this.IsWindowCreated)
            return GLFW.glfwGetTime().also { GLFW2.ThrowErrorIfNeeded() }
        }

    public fun Initialize() {
        GLFW.glfwInit().also { GLFW2.ThrowErrorIfNeeded() }
    }

    public fun Deinitialize() {
        GLFW.glfwTerminate()
    }

    public fun CreateWindow(title: String, width: Int = 1280, height: Int = 720) {
        check(!this.IsWindowCreated)
        this.Window = run {
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
        }.also { window ->
            GLFW.glfwMakeContextCurrent(window).also { GLFW2.ThrowErrorIfNeeded() }
            GLFW.glfwSwapInterval(1).also { GLFW2.ThrowErrorIfNeeded() }
        }.also { window ->
            GLFW.glfwShowWindow(window).also { GLFW2.ThrowErrorIfNeeded() }
            GLFW.glfwFocusWindow(window).also { GLFW2.ThrowErrorIfNeeded() }
        }
    }

    public fun CloseWindow() {
        check(this.IsWindowCreated)
        GLFW.glfwSetWindowShouldClose(this.Window, true)
    }

    public fun DestroyWindow() {
        check(this.IsWindowCreated)
        GLFW.glfwDestroyWindow(this.Window).also { GLFW2.ThrowErrorIfNeeded() }
        this.Window = 0L
    }

}

public object MainLoop {

    public var NumberOfFrame: Int = 0
        get() {
            check(Engine.IsWindowCreated)
            return field
        }
        private set(value) {
            check(Engine.IsWindowCreated)
            field = value
        }

    public fun Run() {
        check(Engine.IsWindowCreated)
        this.NumberOfFrame = 0
        while (!GLFW.glfwWindowShouldClose(Engine.Window)) {
            GLFW.glfwPollEvents().also { GLFW2.ThrowErrorIfNeeded() }
//            if (GLFW.glfwGetKey(Engine.Window, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(Engine.Window, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS) {
//                if (GLFW.glfwGetKey(Engine.Window, GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS) {
//                    Engine.SetIsWindowFullscreen(!Engine.IsWindowFullscreen)
//                }
//            }
            // Update()
            // Draw()
            GLFW.glfwSwapBuffers(Engine.Window).also { GLFW2.ThrowErrorIfNeeded() }
            this.NumberOfFrame++
        }
    }

}
