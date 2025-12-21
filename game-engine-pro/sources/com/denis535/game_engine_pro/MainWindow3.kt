package com.denis535.game_engine_pro

import cnames.structs.*
import glfw.*
import kotlinx.cinterop.*

public abstract class MainWindow3 : MainWindow2 {

    @OptIn(ExperimentalForeignApi::class)
    private val OnMouseCursorEnterCallback = staticCFunction { window: CPointer<GLFWwindow>?, isEntered: Int ->
        if (isEntered == GLFW_TRUE) {
            val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
            val thisRef = thisPtr.asStableRef<MainWindow3>()
            thisRef.get().OnMouseCursorEnter()
        } else {
            val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
            val thisRef = thisPtr.asStableRef<MainWindow3>()
            thisRef.get().OnMouseCursorLeave()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private val OnMouseCursorMoveCallback = staticCFunction { window: CPointer<GLFWwindow>?, posX: Double, posY: Double ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindow3>()
        thisRef.get().OnMouseCursorMove(posX, posY)
    }

    @OptIn(ExperimentalForeignApi::class)
    private val OnMouseButtonActionCallback = staticCFunction { window: CPointer<GLFWwindow>?, button: Int, action: Int, mods: Int ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindow3>()
        if (action == GLFW_PRESS) {
            thisRef.get().OnMouseButtonPress(MouseButton.Create(button))
        } else if (action == GLFW_REPEAT) {
            thisRef.get().OnMouseButtonRepeat(MouseButton.Create(button))
        } else if (action == GLFW_RELEASE) {
            thisRef.get().OnMouseButtonRelease(MouseButton.Create(button))
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private val OnMouseWheelScrollCallback = staticCFunction { window: CPointer<GLFWwindow>?, deltaX: Double, deltaY: Double ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindow3>()
        thisRef.get().OnMouseWheelScroll(deltaX, deltaY)
    }

    public constructor(title: String) : super(title)
    public constructor(title: String, width: Int = 1280, height: Int = 720, isResizable: Boolean = false) : super(title, width, height, isResizable)

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        super.close()
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnStart() {
        glfwSetCursorEnterCallback(this.NativeWindow, this.OnMouseCursorEnterCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetCursorPosCallback(this.NativeWindow, this.OnMouseCursorMoveCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetMouseButtonCallback(this.NativeWindow, this.OnMouseButtonActionCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetScrollCallback(this.NativeWindow, this.OnMouseWheelScrollCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwMakeContextCurrent(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
        glfwSwapInterval(1).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnStop() {
        glfwSetCursorEnterCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetCursorPosCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetMouseButtonCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetScrollCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnFrameBegin(info: FrameInfo) {
        glfwPollEvents().also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnFrameEnd(info: FrameInfo) {
        glfwSwapBuffers(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    protected abstract fun OnMouseCursorEnter()

    @OptIn(ExperimentalForeignApi::class)
    protected abstract fun OnMouseCursorLeave()

    @OptIn(ExperimentalForeignApi::class)
    protected abstract fun OnMouseCursorMove(posX: Double, posY: Double)

    @OptIn(ExperimentalForeignApi::class)
    protected abstract fun OnMouseButtonPress(button: MouseButton)

    @OptIn(ExperimentalForeignApi::class)
    protected abstract fun OnMouseButtonRepeat(button: MouseButton)

    @OptIn(ExperimentalForeignApi::class)
    protected abstract fun OnMouseButtonRelease(button: MouseButton)

    @OptIn(ExperimentalForeignApi::class)
    protected abstract fun OnMouseWheelScroll(deltaX: Double, deltaY: Double)

}

public enum class MouseButton(val Value: Int) {
    @OptIn(ExperimentalForeignApi::class)
    Button_1(GLFW_MOUSE_BUTTON_1),
    @OptIn(ExperimentalForeignApi::class)
    Button_2(GLFW_MOUSE_BUTTON_2),
    @OptIn(ExperimentalForeignApi::class)
    Button_3(GLFW_MOUSE_BUTTON_3),
    @OptIn(ExperimentalForeignApi::class)
    Button_4(GLFW_MOUSE_BUTTON_4),
    @OptIn(ExperimentalForeignApi::class)
    Button_6(GLFW_MOUSE_BUTTON_6),
    @OptIn(ExperimentalForeignApi::class)
    Button_7(GLFW_MOUSE_BUTTON_7),
    @OptIn(ExperimentalForeignApi::class)
    Button_8(GLFW_MOUSE_BUTTON_8),
    @OptIn(ExperimentalForeignApi::class)
    Left(GLFW_MOUSE_BUTTON_1),
    @OptIn(ExperimentalForeignApi::class)
    Right(GLFW_MOUSE_BUTTON_2),
    @OptIn(ExperimentalForeignApi::class)
    Middle(GLFW_MOUSE_BUTTON_3);

    internal companion object {
        public fun Create(value: Int): MouseButton {
            return entries.first { it.Value == value }
        }
    }
}
