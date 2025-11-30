package com.denis535.kotlin_game_example

import org.lwjgl.glfw.GLFW

public class Engine : AutoCloseable {

    private val MainWindow: MainWindow

    public var IsRunning: Boolean = false
        private set(value) {
            check(field != value)
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

    public constructor(mainWindow: MainWindow) {
        this.MainWindow = mainWindow.also { require(!it.IsClosed) }
    }

    public override fun close() {
        check(!this.MainWindow.IsClosed)
        check(!this.IsRunning)
    }

    public fun Run() {
        this.IsRunning = true
        this.NumberOfFrame = 0
        while (!this.MainWindow.IsClosingRequested) {
            this.OnFrame()
        }
        this.IsRunning = false
    }

    private fun OnFrame() {
        GLFW.glfwPollEvents().also { GLFW2.ThrowErrorIfNeeded() }
        this.OnUpdate()
        this.OnDraw()
        GLFW.glfwSwapBuffers(this.MainWindow.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
        this.NumberOfFrame++
    }

    private fun OnUpdate() {
        if (GLFW.glfwGetKey(this.MainWindow.NativeWindowPointer, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(this.MainWindow.NativeWindowPointer, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS) {
            if (GLFW.glfwGetKey(this.MainWindow.NativeWindowPointer, GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS) {
                this.MainWindow.IsFullscreen = !this.MainWindow.IsFullscreen
            }
        }
    }

    private fun OnDraw() {

    }

}
