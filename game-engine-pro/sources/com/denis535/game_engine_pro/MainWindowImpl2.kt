package com.denis535.game_engine_pro

import glfw.*
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
        glfwMakeContextCurrent(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
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
        glfwSwapBuffers(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
    }

}
