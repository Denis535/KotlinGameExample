package com.denis535.game_engine_pro

import cnames.structs.*
import glfw.*
import kotlinx.cinterop.*
import kotlin.experimental.*

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
            val button2 = MouseButton.Create(button)
            if (button2 != null) thisRef.get().OnMouseButtonPress(button2)
        } else if (action == GLFW_REPEAT) {
            val button2 = MouseButton.Create(button)
            if (button2 != null) thisRef.get().OnMouseButtonRepeat(button2)
        } else if (action == GLFW_RELEASE) {
            val button2 = MouseButton.Create(button)
            if (button2 != null) thisRef.get().OnMouseButtonRelease(button2)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private val OnMouseWheelScrollCallback = staticCFunction { window: CPointer<GLFWwindow>?, deltaX: Double, deltaY: Double ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindow3>()
        thisRef.get().OnMouseWheelScroll(deltaX, deltaY)
    }

    @OptIn(ExperimentalForeignApi::class)
    private val OnKeyCallback = staticCFunction { window: CPointer<GLFWwindow>?, key: Int, scancode: Int, action: Int, mods: Int ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindow3>()
        if (action == GLFW_PRESS) {
            val key2 = Key.Create(key)
            if (key2 != null) thisRef.get().OnKeyPress(key2)
        } else if (action == GLFW_REPEAT) {
            val key2 = Key.Create(key)
            if (key2 != null) thisRef.get().OnKeyRepeat(key2)
        } else if (action == GLFW_RELEASE) {
            val key2 = Key.Create(key)
            if (key2 != null) thisRef.get().OnKeyRelease(key2)
        }
    }

    @OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
    private val OnCharCallback = staticCFunction { window: CPointer<GLFWwindow>?, codepoint: UInt ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindow3>()
        thisRef.get().OnCharInput(Char.toChars(codepoint.toInt()).concatToString())
    }

    public constructor(title: String) : super(title)
    public constructor(title: String, width: Int = 1280, height: Int = 720, isResizable: Boolean = false) : super(title, width, height, isResizable)

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        super.close()
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnStart() {
        glfwMakeContextCurrent(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
        glfwSwapInterval(1).also { GLFW.ThrowErrorIfNeeded() }

        glfwSetCursorEnterCallback(this.NativeWindow, this.OnMouseCursorEnterCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetCursorPosCallback(this.NativeWindow, this.OnMouseCursorMoveCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetMouseButtonCallback(this.NativeWindow, this.OnMouseButtonActionCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetScrollCallback(this.NativeWindow, this.OnMouseWheelScrollCallback).also { GLFW.ThrowErrorIfNeeded() }

        glfwSetKeyCallback(this.NativeWindow, this.OnKeyCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetCharCallback(this.NativeWindow, this.OnCharCallback).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun OnStop() {
        glfwSetCursorEnterCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetCursorPosCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetMouseButtonCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetScrollCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }

        glfwSetKeyCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetCharCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
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

    @OptIn(ExperimentalForeignApi::class)
    protected abstract fun OnKeyPress(key: Key)

    @OptIn(ExperimentalForeignApi::class)
    protected abstract fun OnKeyRepeat(key: Key)

    @OptIn(ExperimentalForeignApi::class)
    protected abstract fun OnKeyRelease(key: Key)

    @OptIn(ExperimentalForeignApi::class)
    protected abstract fun OnCharInput(char: String)

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
        public fun Create(value: Int): MouseButton? {
            return entries.firstOrNull { it.Value == value }
        }
    }
}

public enum class Key(val Value: Int) {
    @OptIn(ExperimentalForeignApi::class)
    Letter_A(GLFW_KEY_A),
    @OptIn(ExperimentalForeignApi::class)
    Letter_B(GLFW_KEY_B),
    @OptIn(ExperimentalForeignApi::class)
    Letter_C(GLFW_KEY_C),
    @OptIn(ExperimentalForeignApi::class)
    Letter_D(GLFW_KEY_D),
    @OptIn(ExperimentalForeignApi::class)
    Letter_E(GLFW_KEY_E),
    @OptIn(ExperimentalForeignApi::class)
    Letter_F(GLFW_KEY_F),
    @OptIn(ExperimentalForeignApi::class)
    Letter_G(GLFW_KEY_G),
    @OptIn(ExperimentalForeignApi::class)
    Letter_H(GLFW_KEY_H),
    @OptIn(ExperimentalForeignApi::class)
    Letter_I(GLFW_KEY_I),
    @OptIn(ExperimentalForeignApi::class)
    Letter_J(GLFW_KEY_J),
    @OptIn(ExperimentalForeignApi::class)
    Letter_K(GLFW_KEY_K),
    @OptIn(ExperimentalForeignApi::class)
    Letter_L(GLFW_KEY_L),
    @OptIn(ExperimentalForeignApi::class)
    Letter_M(GLFW_KEY_M),
    @OptIn(ExperimentalForeignApi::class)
    Letter_N(GLFW_KEY_N),
    @OptIn(ExperimentalForeignApi::class)
    Letter_O(GLFW_KEY_O),
    @OptIn(ExperimentalForeignApi::class)
    Letter_P(GLFW_KEY_P),
    @OptIn(ExperimentalForeignApi::class)
    Letter_Q(GLFW_KEY_Q),
    @OptIn(ExperimentalForeignApi::class)
    Letter_R(GLFW_KEY_R),
    @OptIn(ExperimentalForeignApi::class)
    Letter_S(GLFW_KEY_S),
    @OptIn(ExperimentalForeignApi::class)
    Letter_T(GLFW_KEY_T),
    @OptIn(ExperimentalForeignApi::class)
    Letter_U(GLFW_KEY_U),
    @OptIn(ExperimentalForeignApi::class)
    Letter_V(GLFW_KEY_V),
    @OptIn(ExperimentalForeignApi::class)
    Letter_W(GLFW_KEY_W),
    @OptIn(ExperimentalForeignApi::class)
    Letter_X(GLFW_KEY_X),
    @OptIn(ExperimentalForeignApi::class)
    Letter_Y(GLFW_KEY_Y),
    @OptIn(ExperimentalForeignApi::class)
    Letter_Z(GLFW_KEY_Z),

    @OptIn(ExperimentalForeignApi::class)
    Digit_0(GLFW_KEY_0),
    @OptIn(ExperimentalForeignApi::class)
    Digit_1(GLFW_KEY_1),
    @OptIn(ExperimentalForeignApi::class)
    Digit_2(GLFW_KEY_2),
    @OptIn(ExperimentalForeignApi::class)
    Digit_3(GLFW_KEY_3),
    @OptIn(ExperimentalForeignApi::class)
    Digit_4(GLFW_KEY_4),
    @OptIn(ExperimentalForeignApi::class)
    Digit_5(GLFW_KEY_5),
    @OptIn(ExperimentalForeignApi::class)
    Digit_6(GLFW_KEY_6),
    @OptIn(ExperimentalForeignApi::class)
    Digit_7(GLFW_KEY_7),
    @OptIn(ExperimentalForeignApi::class)
    Digit_8(GLFW_KEY_8),
    @OptIn(ExperimentalForeignApi::class)
    Digit_9(GLFW_KEY_9),

    @OptIn(ExperimentalForeignApi::class)
    Up(GLFW_KEY_UP),
    @OptIn(ExperimentalForeignApi::class)
    Down(GLFW_KEY_DOWN),
    @OptIn(ExperimentalForeignApi::class)
    Left(GLFW_KEY_LEFT),
    @OptIn(ExperimentalForeignApi::class)
    Right(GLFW_KEY_RIGHT),

    @OptIn(ExperimentalForeignApi::class)
    Space(GLFW_KEY_SPACE),
    @OptIn(ExperimentalForeignApi::class)
    Backspace(GLFW_KEY_BACKSPACE),
    @OptIn(ExperimentalForeignApi::class)
    Delete(GLFW_KEY_DELETE),
    @OptIn(ExperimentalForeignApi::class)
    Enter(GLFW_KEY_ENTER),

    @OptIn(ExperimentalForeignApi::class)
    Left_Alt(GLFW_KEY_LEFT_ALT),
    @OptIn(ExperimentalForeignApi::class)
    Left_Control(GLFW_KEY_LEFT_CONTROL),
    @OptIn(ExperimentalForeignApi::class)
    Left_Shift(GLFW_KEY_LEFT_SHIFT),

    @OptIn(ExperimentalForeignApi::class)
    Right_Alt(GLFW_KEY_RIGHT_ALT),
    @OptIn(ExperimentalForeignApi::class)
    Right_Control(GLFW_KEY_RIGHT_CONTROL),
    @OptIn(ExperimentalForeignApi::class)
    Right_Shift(GLFW_KEY_RIGHT_SHIFT),

    @OptIn(ExperimentalForeignApi::class)
    CapsLock(GLFW_KEY_CAPS_LOCK),
    @OptIn(ExperimentalForeignApi::class)
    Tab(GLFW_KEY_TAB),
    @OptIn(ExperimentalForeignApi::class)
    Escape(GLFW_KEY_ESCAPE),

    @OptIn(ExperimentalForeignApi::class)
    Keypad_0(GLFW_KEY_KP_0),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_1(GLFW_KEY_KP_1),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_2(GLFW_KEY_KP_2),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_3(GLFW_KEY_KP_3),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_4(GLFW_KEY_KP_4),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_5(GLFW_KEY_KP_5),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_6(GLFW_KEY_KP_6),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_7(GLFW_KEY_KP_7),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_8(GLFW_KEY_KP_8),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_9(GLFW_KEY_KP_9),

    @OptIn(ExperimentalForeignApi::class)
    Keypad_Add(GLFW_KEY_KP_ADD),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_Subtract(GLFW_KEY_KP_SUBTRACT),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_Multiply(GLFW_KEY_KP_MULTIPLY),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_Divide(GLFW_KEY_KP_DIVIDE),
    @OptIn(ExperimentalForeignApi::class)
    Keypad_Decimal(GLFW_KEY_KP_DECIMAL),

    @OptIn(ExperimentalForeignApi::class)
    Keypad_Enter(GLFW_KEY_KP_ENTER),

    @OptIn(ExperimentalForeignApi::class)
    F1(GLFW_KEY_F1),
    @OptIn(ExperimentalForeignApi::class)
    F2(GLFW_KEY_F2),
    @OptIn(ExperimentalForeignApi::class)
    F3(GLFW_KEY_F3),
    @OptIn(ExperimentalForeignApi::class)
    F4(GLFW_KEY_F4),
    @OptIn(ExperimentalForeignApi::class)
    F5(GLFW_KEY_F5),
    @OptIn(ExperimentalForeignApi::class)
    F6(GLFW_KEY_F6),
    @OptIn(ExperimentalForeignApi::class)
    F7(GLFW_KEY_F7),
    @OptIn(ExperimentalForeignApi::class)
    F8(GLFW_KEY_F8),
    @OptIn(ExperimentalForeignApi::class)
    F9(GLFW_KEY_F9),
    @OptIn(ExperimentalForeignApi::class)
    F10(GLFW_KEY_F10),
    @OptIn(ExperimentalForeignApi::class)
    F11(GLFW_KEY_F11),
    @OptIn(ExperimentalForeignApi::class)
    F12(GLFW_KEY_F12);

    internal companion object {
        public fun Create(value: Int): Key? {
            return Key.entries.firstOrNull { it.Value == value }
        }
    }
}
