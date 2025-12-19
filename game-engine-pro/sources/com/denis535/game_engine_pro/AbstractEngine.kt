package com.denis535.game_engine_pro

import glfw.*
import kotlinx.cinterop.*

public abstract class AbstractEngine : AutoCloseable {

    protected val Window: MainWindow

    public var FixedDeltaTime: Float = 1.0f / 20.0f

    public var IsRunning: Boolean = false
        private set(value) {
            check(field != value)
            field = value
        }

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
        this.OnStart()
        while (!this.Window.IsClosingRequested) {
            val startTime = this.Window.Time
            run {
                this.OnFrameBegin(info)
                if (info.FixedFrameInfo.Number == 0) {
                    this.OnFixedUpdate(info)
                    info.FixedFrameInfo.Number++
                    info.FixedFrameInfo.DeltaTime = this.FixedDeltaTime
                } else {
                    while (info.FixedFrameInfo.Time <= info.Time) {
                        this.OnFixedUpdate(info)
                        info.FixedFrameInfo.Number++
                        info.FixedFrameInfo.DeltaTime = this.FixedDeltaTime
                    }
                }
                this.OnUpdate(info)
                this.OnDraw(info)
                this.OnFrameEnd(info)
            }
            val endTime = this.Window.Time
            val deltaTime = (endTime - startTime).toFloat()
            info.Number++
            info.Time += deltaTime
            info.DeltaTime = deltaTime
        }
        this.OnStop()
        this.IsRunning = false
    }

    protected abstract fun OnStart()
    protected abstract fun OnStop()

    protected abstract fun OnFrameBegin(info: FrameInfo)
    protected abstract fun OnFixedUpdate(info: FrameInfo)
    protected abstract fun OnUpdate(info: FrameInfo)
    protected abstract fun OnDraw(info: FrameInfo)
    protected abstract fun OnFrameEnd(info: FrameInfo)

}

public abstract class AbstractEngine2 : AbstractEngine {

    public constructor(window: MainWindow) : super(window)

    public override fun close() {
        super.close()
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnStart() {
        glfwMakeContextCurrent(this.Window.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
        glfwSwapInterval(1).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnStop() {
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnFrameBegin(info: FrameInfo) {
        glfwPollEvents().also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnFrameEnd(info: FrameInfo) {
        glfwSwapBuffers(this.Window.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
    }

}

public class FrameInfo {

    public val FixedFrameInfo: FixedFrameInfo = FixedFrameInfo()

    public var Number: Int = 0
        internal set

    public var Time: Float = 0.0f
        internal set

    public var DeltaTime: Float = 0.0f
        internal set

    public val Fps: Float
        get() {
            return if (this.DeltaTime > 0.0f) 1.0f / this.DeltaTime else 0.0f
        }

    internal constructor()

    public override fun toString(): String {
        return "FrameInfo(Number=${this.Number}, Time=${this.Time})"
    }

}

public class FixedFrameInfo {

    public var Number: Int = 0
        internal set

    public val Time: Float
        get() {
            return this.Number * this.DeltaTime
        }

    public var DeltaTime: Float = 0.0f
        internal set

    internal constructor()

    public override fun toString(): String {
        return "FixedFrameInfo(Number=${this.Number}, Time=${this.Time})"
    }

}
