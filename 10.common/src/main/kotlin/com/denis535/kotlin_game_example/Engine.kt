package com.denis535.kotlin_game_example

import org.lwjgl.glfw.GLFW

public class Engine : AutoCloseable {

    private val Window: MainWindow

    public var IsRunning: Boolean = false
        private set(value) {
            check(field != value)
            field = value
        }

    public var Tick: Int = 0
        get() {
            check(this.IsRunning)
            return field
        }
        private set(value) {
            check(this.IsRunning)
            field = value
        }

    public var FixedTick: Int = 0
        get() {
            check(this.IsRunning)
            return field
        }
        private set(value) {
            check(this.IsRunning)
            field = value
        }

    public var Fps: Double = 0.0
        get() {
            check(this.IsRunning)
            return field
        }
        private set(value) {
            check(this.IsRunning)
            field = value
        }

    public constructor(mainWindow: MainWindow) {
        this.Window = mainWindow.also { require(!it.IsClosed) }
    }

    public override fun close() {
        check(!this.Window.IsClosed)
        check(!this.IsRunning)
    }

    public fun Run() {
        this.IsRunning = true
        this.Tick = 0
        this.FixedTick = 0
        this.Fps = 0.0
        var time = 0.0
        var deltaTime = 0.0
        while (!this.Window.IsClosingRequested) {
            deltaTime = run {
                val startTime = this.Window.Time
                this.OnFrameBegin()
                this.OnFrame(time, deltaTime)
                this.OnFrameEnd()
                val endTime = this.Window.Time
                endTime - startTime
            }
            time += deltaTime
            this.Fps = 1.0 / deltaTime
        }
        this.IsRunning = false
    }

    private fun OnFrameBegin() {
        GLFW.glfwPollEvents().also { GLFW2.ThrowErrorIfNeeded() }
    }

    private fun OnFrame(time: Double, deltaTime: Double) {
        if (this.FixedTick == 0) {
            this.OnFixedUpdate(0.0)
            this.FixedTick++
        } else {
            val fixedDeltaTime = 1.0 / 25.0
            val desiredFixedTicks = (time / fixedDeltaTime).toInt()
            while (this.FixedTick < desiredFixedTicks) {
                this.OnFixedUpdate(fixedDeltaTime)
                this.FixedTick++
            }
        }
        this.OnUpdate(deltaTime)
        this.OnDraw(deltaTime)
        this.Tick++
    }

    private fun OnFrameEnd() {
        GLFW.glfwSwapBuffers(this.Window.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
    }

    private fun OnFixedUpdate(deltaTime: Double) {
//        println("OnFixedUpdate")
    }

    private fun OnUpdate(deltaTime: Double) {
//        println("OnUpdate")
        if (GLFW.glfwGetKey(this.Window.NativeWindowPointer, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(this.Window.NativeWindowPointer, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS) {
            if (GLFW.glfwGetKey(this.Window.NativeWindowPointer, GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS) {
                this.Window.IsFullscreen = !this.Window.IsFullscreen
            }
        }
    }

    private fun OnDraw(deltaTime: Double) {

    }

}
