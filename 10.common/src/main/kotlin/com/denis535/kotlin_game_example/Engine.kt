package com.denis535.kotlin_game_example

import org.lwjgl.glfw.GLFW

public class Engine : AutoCloseable {

    private val Window: MainWindow
    private val OnFixedUpdateCallback: ((Time) -> Unit)
    private val OnUpdateCallback: ((Time) -> Unit)

    public var IsRunning: Boolean = false
        private set(value) {
            check(field != value)
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

    public constructor(window: MainWindow, onFixedUpdateCallback: ((Time) -> Unit), onUpdateCallback: ((Time) -> Unit)) {
        this.Window = window.also { require(!it.IsClosed) }
        this.OnFixedUpdateCallback = onFixedUpdateCallback
        this.OnUpdateCallback = onUpdateCallback
    }

    public override fun close() {
        check(!this.Window.IsClosed)
        check(!this.IsRunning)
    }

    public fun Run(fixedDeltaTime: Double = 1.0 / 20.0) {
        this.IsRunning = true
        this.Fps = 0.0
        val time = Time()
        while (!this.Window.IsClosingRequested) {
            val deltaTime = run {
                val startTime = this.Window.Time
                this.OnFrameBegin()
                this.OnFixedUpdate(time)
                this.OnUpdate(time)
                this.OnFrameEnd()
                val endTime = this.Window.Time
                endTime - startTime
            }
            this.Fps = 1.0 / deltaTime
            time.FixedTime.DeltaTime = fixedDeltaTime
            time.RealTime.FrameTime += deltaTime
            time.RealTime.DeltaTime = deltaTime
        }
        this.IsRunning = false
    }

    private fun OnFrameBegin() {
        GLFW.glfwPollEvents().also { GLFW2.ThrowErrorIfNeeded() }
    }

    private fun OnFixedUpdate(time: Time) {
        if (time.FixedTime.FrameNumber == 0) {
            this.OnFixedUpdateCallback(time)
            time.FixedTime.FrameNumber++
        } else {
            while (time.FixedTime.FrameTime <= time.RealTime.FrameTime) {
                this.OnFixedUpdateCallback(time)
                time.FixedTime.FrameNumber++
            }
        }
    }

    private fun OnUpdate(time: Time) {
        if (GLFW.glfwGetKey(this.Window.NativeWindowPointer, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(this.Window.NativeWindowPointer, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS) {
            if (GLFW.glfwGetKey(this.Window.NativeWindowPointer, GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS) {
                this.Window.IsFullscreen = !this.Window.IsFullscreen
            }
        }
        this.OnUpdateCallback(time)
        time.RealTime.FrameNumber++
    }

    private fun OnFrameEnd() {
        GLFW.glfwSwapBuffers(this.Window.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
    }

}

public class Time {

    public val FixedTime: FixedTime = FixedTime()
    public val RealTime: RealTime = RealTime()

    internal constructor() {
    }

}

public class FixedTime {

    public var FrameNumber: Int = 0
        internal set

    public val FrameTime: Double
        get() {
            return this.FrameNumber * this.DeltaTime
        }

    public var DeltaTime: Double = 0.0
        internal set

    internal constructor() {
    }

    public override fun toString(): String {
        return "FixedTime: $this.FrameNumber, $this.FrameTime"
    }

}

public class RealTime {

    public var FrameNumber: Int = 0
        internal set

    public var FrameTime: Double = 0.0
        internal set

    public var DeltaTime: Double = 0.0
        internal set

    internal constructor() {
    }

    public override fun toString(): String {
        return "FixedTime: $this.FrameNumber, $this.FrameTime"
    }

}
