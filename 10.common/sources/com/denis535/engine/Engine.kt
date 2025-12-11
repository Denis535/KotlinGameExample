package com.denis535.engine

import kotlinx.cinterop.*
import glfw.*

public class Engine : AutoCloseable {

    private val Window: MainWindow

    public var FixedDeltaTime: Double = 1.0 / 20.0

    public var IsRunning: Boolean = false
        private set(value) {
            check(field != value)
            field = value
        }

    public var OnFixedUpdateCallback: ((FixedFrameInfo) -> Unit)? = null
    public var OnUpdateCallback: ((FrameInfo) -> Unit)? = null
    public var OnDrawCallback: ((FrameInfo) -> Unit)? = null

    public constructor(window: MainWindow) {
        this.Window = window.also { require(!it.IsClosed) }
    }

    public override fun close() {
        check(!this.Window.IsClosed)
        check(!this.IsRunning)
    }

    public fun Run() {
        this.IsRunning = true
        val info = FrameInfo()
        while (!this.Window.IsClosingRequested) {
            val startTime = this.Window.Time
            this.OnFrameBegin(info)
            this.OnFixedUpdate(info)
            this.OnUpdate(info)
            this.OnDraw(info)
            this.OnFrameEnd(info, startTime)
        }
        this.IsRunning = false
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun OnFrameBegin(info: FrameInfo) {
        glfwPollEvents().also { GLFW2.ThrowErrorIfNeeded() }
    }

    private fun OnFixedUpdate(info: FrameInfo) {
        if (info.FixedFrameInfo.Number == 0) {
            this.OnFixedUpdateCallback?.invoke(info.FixedFrameInfo)
            info.FixedFrameInfo.Number++
            info.FixedFrameInfo.DeltaTime = this.FixedDeltaTime
        } else {
            while (info.FixedFrameInfo.Time <= info.Time) {
                this.OnFixedUpdateCallback?.invoke(info.FixedFrameInfo)
                info.FixedFrameInfo.Number++
                info.FixedFrameInfo.DeltaTime = this.FixedDeltaTime
            }
        }
    }

    private fun OnUpdate(info: FrameInfo) {
        this.OnUpdateCallback?.invoke(info)
    }

    private fun OnDraw(info: FrameInfo) {
        this.OnDrawCallback?.invoke(info)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun OnFrameEnd(info: FrameInfo, startTime: Double) {
        glfwSwapBuffers(this.Window.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
        val endTime = this.Window.Time
        val deltaTime = endTime - startTime
        info.Number++
        info.Time += deltaTime
        info.DeltaTime = deltaTime
    }

}

public class FrameInfo {

    public val FixedFrameInfo: FixedFrameInfo = FixedFrameInfo()

    public var Number: Int = 0
        internal set

    public var Time: Double = 0.0
        internal set

    public var DeltaTime: Double = 0.0
        internal set

    public val Fps: Double
        get() {
            return if (this.DeltaTime > 0.0) 1.0 / this.DeltaTime else 0.0
        }

    internal constructor()

    public override fun toString(): String {
        return "FrameInfo(Number=${this.Number}, Time=${this.Time})"
    }

}

public class FixedFrameInfo {

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
        return "FixedFrameInfo(Number=${this.Number}, Time=${this.Time})"
    }

}
