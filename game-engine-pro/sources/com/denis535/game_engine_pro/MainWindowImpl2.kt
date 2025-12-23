package com.denis535.game_engine_pro

import com.denis535.glfw.*
import kotlinx.cinterop.*

public abstract class MainWindowImpl2 : MainWindowImpl {

    @OptIn(ExperimentalForeignApi::class)
    public constructor(desc: MainWindowDesc) : super(desc)

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        super.close()
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnStart() {
        glfwMakeContextCurrent(this.NativeWindow).also { Glfw.ThrowErrorIfNeeded() }
        glfwSwapInterval(1).also { Glfw.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnStop() {
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnFrameBegin(info: FrameInfo) {
        glfwPollEvents().also { Glfw.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnFrameEnd(info: FrameInfo) {
        glfwSwapBuffers(this.NativeWindow).also { Glfw.ThrowErrorIfNeeded() }
    }

}
