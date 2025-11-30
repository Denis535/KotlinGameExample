package com.denis535.kotlin_game_example

import org.lwjgl.glfw.GLFW

public class Engine : AutoCloseable {

    private val MainWindow: MainWindow

    public var IsRunning: Boolean = false
        private set(value) {
            field = value
        }

    public val Time: Double
        get() {
            check(this.IsRunning)
            return GLFW.glfwGetTime().also { GLFW2.ThrowErrorIfNeeded() }
        }

    public var NumberOfFrame: Int = 0
        get() {
            check(this.IsRunning)
            return field
        }
        private set(value) {
            field = value
        }

    public constructor(mainWindow: MainWindow) {
        this.MainWindow = mainWindow.also { check(!it.IsClosed) }
        this.IsRunning = false
        this.NumberOfFrame = 0
    }

    public override fun close() {
        check(!this.MainWindow.IsClosed)
        check(!this.IsRunning)
    }

    public fun Run() {
        check(!this.IsRunning)
        this.IsRunning = true
        this.NumberOfFrame = 0
        while (!this.MainWindow.IsClosingRequested) {
            GLFW.glfwPollEvents().also { GLFW2.ThrowErrorIfNeeded() }
//            if (GLFW.glfwGetKey(this.MainWindow.NativePointer, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(this.MainWindow.NativePointer, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS) {
//                if (GLFW.glfwGetKey(this.MainWindow.NativePointer, GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS) {
//                    this.MainWindow.IsFullscreen = !this.MainWindow.IsFullscreen
//                }
//            }
            // Update()
            // Draw()
            GLFW.glfwSwapBuffers(this.MainWindow.NativePointer).also { GLFW2.ThrowErrorIfNeeded() }
            this.NumberOfFrame++
        }
        this.IsRunning = false
    }

}
