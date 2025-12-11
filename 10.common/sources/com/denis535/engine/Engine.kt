package com.denis535.engine

import kotlinx.cinterop.*
import glfw.*

public class Engine : AutoCloseable {

    private val Window: MainWindow
    private val OnFixedUpdateCallback: ((PhysicsFrameInfo) -> Unit)
    private val OnUpdateCallback: ((FrameInfo) -> Unit)
    private val OnDrawCallback: ((FrameInfo) -> Unit)

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

    public constructor(window: MainWindow, onFixedUpdateCallback: ((PhysicsFrameInfo) -> Unit), onUpdateCallback: ((FrameInfo) -> Unit), onDrawCallback: ((FrameInfo) -> Unit)) {
        this.Window = window.also { require(!it.IsClosed) }
        this.OnFixedUpdateCallback = onFixedUpdateCallback
        this.OnUpdateCallback = onUpdateCallback
        this.OnDrawCallback = onDrawCallback
    }

    public override fun close() {
        check(!this.Window.IsClosed)
        check(!this.IsRunning)
    }

    public fun Run(fixedDeltaTime: Double = 1.0 / 20.0) {
        this.IsRunning = true
        this.Fps = 0.0
        val info = FrameInfo()
        while (!this.Window.IsClosingRequested) {
            val deltaTime = run {
                val startTime = this.Window.Time
                this.OnFrameBegin(info)
                this.OnFixedUpdate(info)
                this.OnUpdate(info)
                this.OnDraw(info)
                this.OnFrameEnd(info)
                val endTime = this.Window.Time
                endTime - startTime
            }
            this.Fps = 1.0 / deltaTime
            info.PhysicsFrameInfo.DeltaTime = fixedDeltaTime
            info.Time += deltaTime
            info.DeltaTime = deltaTime
        }
        this.IsRunning = false
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun OnFrameBegin(info: FrameInfo) {
        glfwPollEvents().also { GLFW2.ThrowErrorIfNeeded() }
    }

    private fun OnFixedUpdate(info: FrameInfo) {
        if (info.PhysicsFrameInfo.Number == 0) {
            this.OnFixedUpdateCallback(info.PhysicsFrameInfo)
            info.PhysicsFrameInfo.Number++
        } else {
            while (info.PhysicsFrameInfo.Time <= info.Time) {
                this.OnFixedUpdateCallback(info.PhysicsFrameInfo)
                info.PhysicsFrameInfo.Number++
            }
        }
    }

    private fun OnUpdate(info: FrameInfo) {
        this.OnUpdateCallback(info)
    }

    private fun OnDraw(info: FrameInfo) {
        this.OnDrawCallback(info)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun OnFrameEnd(info: FrameInfo) {
        glfwSwapBuffers(this.Window.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
        info.Number++
    }

}

public class FrameInfo {

    public val PhysicsFrameInfo: PhysicsFrameInfo = PhysicsFrameInfo()

    public var Number: Int = 0
        internal set

    public var Time: Double = 0.0
        internal set

    public var DeltaTime: Double = 0.0
        internal set

    internal constructor()

    public override fun toString(): String {
        return "FrameInfo(Number=${this.Number}, Time=${this.Time})"
    }

}

public class PhysicsFrameInfo {

    public var Number: Int = 0
        internal set

    public val Time: Double
        get() {
            return this.Number * this.DeltaTime
        }

    public var DeltaTime: Double = 0.0
        internal set

    internal constructor()

    public override fun toString(): String {
        return "PhysicsFrameInfo(Number=${this.Number}, Time=${this.Time})"
    }

}
