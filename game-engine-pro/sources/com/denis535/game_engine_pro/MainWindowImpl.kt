package com.denis535.game_engine_pro

import cnames.structs.*
import glfw.*
import kotlinx.cinterop.*
import kotlin.experimental.*

public abstract class MainWindowImpl : MainWindow {

    @OptIn(ExperimentalForeignApi::class)
    private val OnMouseCursorEnterCallback = staticCFunction { window: CPointer<GLFWwindow>?, isEnter: Int ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindowImpl2>().get().also { check(!it.IsClosed) }
        if (isEnter == GLFW_TRUE) {
            thisRef.OnMouseCursorEnter()
        } else {
            thisRef.OnMouseCursorLeave()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private val OnMouseCursorMoveCallback = staticCFunction { window: CPointer<GLFWwindow>?, posX: Double, posY: Double ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindowImpl2>().get().also { check(!it.IsClosed) }
        thisRef.OnMouseCursorMove(Pair(posX, posY))
    }

    @OptIn(ExperimentalForeignApi::class)
    private val OnMouseButtonActionCallback = staticCFunction { window: CPointer<GLFWwindow>?, button: Int, action: Int, mods: Int ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindowImpl2>().get().also { check(!it.IsClosed) }
        if (action == GLFW_PRESS) {
            val button2 = button.ToMouseButton()
            if (button2 != null) thisRef.OnMouseButtonPress(button2)
        } else if (action == GLFW_REPEAT) {
            val button2 = button.ToMouseButton()
            if (button2 != null) thisRef.OnMouseButtonRepeat(button2)
        } else if (action == GLFW_RELEASE) {
            val button2 = button.ToMouseButton()
            if (button2 != null) thisRef.OnMouseButtonRelease(button2)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private val OnMouseWheelScrollCallback = staticCFunction { window: CPointer<GLFWwindow>?, deltaX: Double, deltaY: Double ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindowImpl2>().get().also { check(!it.IsClosed) }
        thisRef.OnMouseWheelScroll(Pair(deltaX, deltaY))
    }

    @OptIn(ExperimentalForeignApi::class)
    private val OnKeyActionCallback = staticCFunction { window: CPointer<GLFWwindow>?, key: Int, scancode: Int, action: Int, mods: Int ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindowImpl2>().get().also { check(!it.IsClosed) }
        if (action == GLFW_PRESS) {
            val key2 = key.ToKey()
            if (key2 != null) thisRef.OnKeyPress(key2)
        } else if (action == GLFW_REPEAT) {
            val key2 = key.ToKey()
            if (key2 != null) thisRef.OnKeyRepeat(key2)
        } else if (action == GLFW_RELEASE) {
            val key2 = key.ToKey()
            if (key2 != null) thisRef.OnKeyRelease(key2)
        }
    }

    @OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
    private val OnCharActionCallback = staticCFunction { window: CPointer<GLFWwindow>?, codepoint: UInt ->
        val thisPtr = glfwGetWindowUserPointer(window)!!.also { GLFW.ThrowErrorIfNeeded() }
        val thisRef = thisPtr.asStableRef<MainWindowImpl2>().get().also { check(!it.IsClosed) }
        thisRef.OnCharInput(codepoint)
    }

    @OptIn(ExperimentalForeignApi::class)
    private var _NativeWindow: CPointer<GLFWwindow>? = null

    @OptIn(ExperimentalForeignApi::class)
    public override val IsClosed: Boolean
        get() {
            return this._NativeWindow == null
        }

    @OptIn(ExperimentalForeignApi::class)
    protected val NativeWindow: CPointer<GLFWwindow>
        get() {
            check(!this.IsClosed)
            return this._NativeWindow!!
        }

    @OptIn(ExperimentalForeignApi::class)
    public override val Time: Double
        get() {
            return glfwGetTime().also { GLFW.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public override val IsFullScreen: Boolean
        get() {
            check(!this.IsClosed)
            val monitor = glfwGetWindowMonitor(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
            return monitor != null
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var Title: String
        get() {
            check(!this.IsClosed)
            error("Not implemented")
        }
        set(value) {
            check(!this.IsClosed)
            glfwSetWindowTitle(this.NativeWindow, value).also { GLFW.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var Position: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            memScoped {
                val posX = this.alloc<IntVar>()
                val posY = this.alloc<IntVar>()
                glfwGetWindowPos(this@MainWindowImpl.NativeWindow, posX.ptr, posY.ptr).also { GLFW.ThrowErrorIfNeeded() }
                return Pair(posX.value, posY.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            glfwSetWindowPos(this.NativeWindow, value.first, value.second).also { GLFW.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var Size: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            memScoped {
                val width = this.alloc<IntVar>()
                val height = this.alloc<IntVar>()
                glfwGetWindowSize(this@MainWindowImpl.NativeWindow, width.ptr, height.ptr).also { GLFW.ThrowErrorIfNeeded() }
                return Pair(width.value, height.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            glfwSetWindowSize(this.NativeWindow, value.first, value.second).also { GLFW.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public override val IsVisible: Boolean
        get() {
            check(!this.IsClosed)
            return glfwGetWindowAttrib(this.NativeWindow, GLFW_VISIBLE).also { GLFW.ThrowErrorIfNeeded() } == GLFW_TRUE
        }

    @OptIn(ExperimentalForeignApi::class)
    public override val IsIconified: Boolean
        get() {
            check(!this.IsClosed)
            return glfwGetWindowAttrib(this.NativeWindow, GLFW_ICONIFIED).also { GLFW.ThrowErrorIfNeeded() } == GLFW_TRUE
        }

    @OptIn(ExperimentalForeignApi::class)
    public override val IsFocused: Boolean
        get() {
            check(!this.IsClosed)
            return glfwGetWindowAttrib(this.NativeWindow, GLFW_FOCUSED).also { GLFW.ThrowErrorIfNeeded() } == GLFW_TRUE
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var CursorMode: CursorMode
        get() {
            check(!this.IsClosed)
            return glfwGetInputMode(this.NativeWindow, GLFW_CURSOR).also { GLFW.ThrowErrorIfNeeded() }.ToCursorMode()
        }
        set(value) {
            check(!this.IsClosed)
            glfwSetInputMode(this.NativeWindow, GLFW_CURSOR, value.ToNativeValue()).also { GLFW.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var IsClosingRequested: Boolean
        get() {
            check(!this.IsClosed)
            return glfwWindowShouldClose(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() } == GLFW_TRUE
        }
        set(value) {
            check(!this.IsClosed)
            glfwSetWindowShouldClose(this.NativeWindow, if (value) GLFW_TRUE else GLFW_FALSE).also { GLFW.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
    public constructor(desc: MainWindowDesc) {
        glfwInit().also { GLFW.ThrowErrorIfNeeded() }
        this._NativeWindow = run {
            val monitor = glfwGetPrimaryMonitor()!!.also { GLFW.ThrowErrorIfNeeded() }
            val videoMode = glfwGetVideoMode(monitor)!!.also { GLFW.ThrowErrorIfNeeded() }
            glfwDefaultWindowHints().also { GLFW.ThrowErrorIfNeeded() }
            // OpenGL
            glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_NATIVE_CONTEXT_API)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6)
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
            if (Platform.isDebugBinary) glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE)
            // Frame
            glfwWindowHint(GLFW_RED_BITS, 8)
            glfwWindowHint(GLFW_GREEN_BITS, 8)
            glfwWindowHint(GLFW_BLUE_BITS, 8)
            glfwWindowHint(GLFW_ALPHA_BITS, 8)
            glfwWindowHint(GLFW_DEPTH_BITS, 24)
            glfwWindowHint(GLFW_STENCIL_BITS, 8)
            when (desc) {
                is MainWindowDesc.FullScreen -> {
                    glfwWindowHint(GLFW_POSITION_X, 0)
                    glfwWindowHint(GLFW_POSITION_Y, 0)
                    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
                    glfwWindowHint(GLFW_REFRESH_RATE, videoMode.pointed.refreshRate)
                    glfwCreateWindow(videoMode.pointed.width, videoMode.pointed.height, desc.Title, monitor, null)!!.also { GLFW.ThrowErrorIfNeeded() }
                }
                is MainWindowDesc.Window -> {
                    glfwWindowHint(GLFW_POSITION_X, (videoMode.pointed.width - desc.Width) / 2)
                    glfwWindowHint(GLFW_POSITION_Y, (videoMode.pointed.height - desc.Height) / 2)
                    glfwWindowHint(GLFW_RESIZABLE, if (desc.IsResizable) GLFW_TRUE else GLFW_FALSE)
                    glfwWindowHint(GLFW_REFRESH_RATE, videoMode.pointed.refreshRate)
                    glfwCreateWindow(desc.Width, desc.Height, desc.Title, null, null)!!.also { GLFW.ThrowErrorIfNeeded() }
                }
            }
        }
        run {
            val thisRef = StableRef.create(this)
            val thisPtr = thisRef.asCPointer()
            glfwSetWindowUserPointer(this.NativeWindow, thisPtr).also { GLFW.ThrowErrorIfNeeded() }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        check(!this.IsRunning)
        run {
            val thisPtr = glfwGetWindowUserPointer(this.NativeWindow)!!.also { GLFW.ThrowErrorIfNeeded() }
            val thisRef = thisPtr.asStableRef<MainWindow>()
            thisRef.dispose()
            glfwSetWindowUserPointer(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        }
        this._NativeWindow = run {
            glfwDestroyWindow(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
            null
        }
        glfwTerminate().also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun Show() {
        check(!this.IsClosed)
        glfwSetCursorEnterCallback(this.NativeWindow, this.OnMouseCursorEnterCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetCursorPosCallback(this.NativeWindow, this.OnMouseCursorMoveCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetMouseButtonCallback(this.NativeWindow, this.OnMouseButtonActionCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetScrollCallback(this.NativeWindow, this.OnMouseWheelScrollCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetKeyCallback(this.NativeWindow, this.OnKeyActionCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetCharCallback(this.NativeWindow, this.OnCharActionCallback).also { GLFW.ThrowErrorIfNeeded() }
        glfwShowWindow(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
        glfwFocusWindow(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun Hide() {
        check(!this.IsClosed)
        glfwHideWindow(this.NativeWindow).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetCursorEnterCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetCursorPosCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetMouseButtonCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetScrollCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetKeyCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetCharCallback(this.NativeWindow, null).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun MakeFullScreen() {
        check(!this.IsClosed)
        val monitor = glfwGetPrimaryMonitor().also { GLFW.ThrowErrorIfNeeded() }
        val videoMode = glfwGetVideoMode(monitor)!!.also { GLFW.ThrowErrorIfNeeded() }
        val posX = 0
        val posY = 0
        val width = videoMode.pointed.width
        val height = videoMode.pointed.height
        val refreshRate = videoMode.pointed.refreshRate
        glfwSetWindowAttrib(this.NativeWindow, GLFW_RESIZABLE, GLFW_FALSE).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetWindowMonitor(this.NativeWindow, monitor, posX, posY, width, height, refreshRate).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun MakeWindowed(width: Int, height: Int, isResizable: Boolean) {
        check(!this.IsClosed)
        val monitor = glfwGetPrimaryMonitor().also { GLFW.ThrowErrorIfNeeded() }
        val videoMode = glfwGetVideoMode(monitor)!!.also { GLFW.ThrowErrorIfNeeded() }
        val posX = (videoMode.pointed.width - width) / 2
        val posY = (videoMode.pointed.height - height) / 2
        val refreshRate = videoMode.pointed.refreshRate
        glfwSetWindowAttrib(this.NativeWindow, GLFW_RESIZABLE, if (isResizable) GLFW_TRUE else GLFW_FALSE).also { GLFW.ThrowErrorIfNeeded() }
        glfwSetWindowMonitor(this.NativeWindow, null, posX, posY, width, height, refreshRate).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun GetMouseCursorPosition(): Pair<Double, Double> {
        check(!this.IsClosed)
        memScoped {
            val posX = this.alloc<DoubleVar>()
            val posY = this.alloc<DoubleVar>()
            glfwGetCursorPos(this@MainWindowImpl.NativeWindow, posX.ptr, posY.ptr).also { GLFW.ThrowErrorIfNeeded() }
            return Pair(posX.value, posY.value)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun SetMouseCursorPosition(pos: Pair<Double, Double>) {
        check(!this.IsClosed)
        glfwSetCursorPos(this@MainWindowImpl.NativeWindow, pos.first, pos.second).also { GLFW.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun GetMouseButtonPressed(button: MouseButton): Boolean {
        check(!this.IsClosed)
        return glfwGetMouseButton(this@MainWindowImpl.NativeWindow, button.ToNativeValue()).also { GLFW.ThrowErrorIfNeeded() } == GLFW_PRESS
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun GetKeyPressed(key: Key): Boolean {
        check(!this.IsClosed)
        return glfwGetKey(this@MainWindowImpl.NativeWindow, key.ToNativeValue()).also { GLFW.ThrowErrorIfNeeded() } == GLFW_PRESS
    }

}

@OptIn(ExperimentalForeignApi::class)
private fun Int.ToCursorMode(): CursorMode {
    return when (this) {
        GLFW_CURSOR_NORMAL -> CursorMode.Normal
        GLFW_CURSOR_HIDDEN -> CursorMode.Hidden
        GLFW_CURSOR_DISABLED -> CursorMode.Disabled
        else -> error("CursorMode $this is not supported")
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun Int.ToMouseButton(): MouseButton? {
    return when (this) {
        GLFW_MOUSE_BUTTON_1 -> MouseButton.Button_1
        GLFW_MOUSE_BUTTON_2 -> MouseButton.Button_2
        GLFW_MOUSE_BUTTON_3 -> MouseButton.Button_3
        GLFW_MOUSE_BUTTON_4 -> MouseButton.Button_4
        GLFW_MOUSE_BUTTON_5 -> MouseButton.Button_5
        GLFW_MOUSE_BUTTON_6 -> MouseButton.Button_6
        GLFW_MOUSE_BUTTON_7 -> MouseButton.Button_7
        GLFW_MOUSE_BUTTON_8 -> MouseButton.Button_8
        else -> null
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun Int.ToKey(): Key? {
    return when (this) {
        GLFW_KEY_A -> Key.Letter_A
        GLFW_KEY_B -> Key.Letter_B
        GLFW_KEY_C -> Key.Letter_C
        GLFW_KEY_D -> Key.Letter_D
        GLFW_KEY_E -> Key.Letter_E
        GLFW_KEY_F -> Key.Letter_F
        GLFW_KEY_G -> Key.Letter_G
        GLFW_KEY_H -> Key.Letter_H
        GLFW_KEY_I -> Key.Letter_I
        GLFW_KEY_J -> Key.Letter_J
        GLFW_KEY_K -> Key.Letter_K
        GLFW_KEY_L -> Key.Letter_L
        GLFW_KEY_M -> Key.Letter_M
        GLFW_KEY_N -> Key.Letter_N
        GLFW_KEY_O -> Key.Letter_O
        GLFW_KEY_P -> Key.Letter_P
        GLFW_KEY_Q -> Key.Letter_Q
        GLFW_KEY_R -> Key.Letter_R
        GLFW_KEY_S -> Key.Letter_S
        GLFW_KEY_T -> Key.Letter_T
        GLFW_KEY_U -> Key.Letter_U
        GLFW_KEY_V -> Key.Letter_V
        GLFW_KEY_W -> Key.Letter_W
        GLFW_KEY_X -> Key.Letter_X
        GLFW_KEY_Y -> Key.Letter_Y
        GLFW_KEY_Z -> Key.Letter_Z

        GLFW_KEY_0 -> Key.Digit_0
        GLFW_KEY_1 -> Key.Digit_1
        GLFW_KEY_2 -> Key.Digit_2
        GLFW_KEY_3 -> Key.Digit_3
        GLFW_KEY_4 -> Key.Digit_4
        GLFW_KEY_5 -> Key.Digit_5
        GLFW_KEY_6 -> Key.Digit_6
        GLFW_KEY_7 -> Key.Digit_7
        GLFW_KEY_8 -> Key.Digit_8
        GLFW_KEY_9 -> Key.Digit_9

        GLFW_KEY_UP -> Key.Up
        GLFW_KEY_DOWN -> Key.Down
        GLFW_KEY_LEFT -> Key.Left
        GLFW_KEY_RIGHT -> Key.Right

        GLFW_KEY_SPACE -> Key.Space
        GLFW_KEY_BACKSPACE -> Key.Backspace
        GLFW_KEY_DELETE -> Key.Delete
        GLFW_KEY_ENTER -> Key.Enter

        GLFW_KEY_LEFT_ALT -> Key.Left_Alt
        GLFW_KEY_LEFT_CONTROL -> Key.Left_Control
        GLFW_KEY_LEFT_SHIFT -> Key.Left_Shift

        GLFW_KEY_RIGHT_ALT -> Key.Right_Alt
        GLFW_KEY_RIGHT_CONTROL -> Key.Right_Control
        GLFW_KEY_RIGHT_SHIFT -> Key.Right_Shift

        GLFW_KEY_CAPS_LOCK -> Key.CapsLock
        GLFW_KEY_TAB -> Key.Tab
        GLFW_KEY_ESCAPE -> Key.Escape

        GLFW_KEY_KP_0 -> Key.Keypad_0
        GLFW_KEY_KP_1 -> Key.Keypad_1
        GLFW_KEY_KP_2 -> Key.Keypad_2
        GLFW_KEY_KP_3 -> Key.Keypad_3
        GLFW_KEY_KP_4 -> Key.Keypad_4
        GLFW_KEY_KP_5 -> Key.Keypad_5
        GLFW_KEY_KP_6 -> Key.Keypad_6
        GLFW_KEY_KP_7 -> Key.Keypad_7
        GLFW_KEY_KP_8 -> Key.Keypad_8
        GLFW_KEY_KP_9 -> Key.Keypad_9

        GLFW_KEY_KP_ADD -> Key.Keypad_Add
        GLFW_KEY_KP_SUBTRACT -> Key.Keypad_Subtract
        GLFW_KEY_KP_MULTIPLY -> Key.Keypad_Multiply
        GLFW_KEY_KP_DIVIDE -> Key.Keypad_Divide
        GLFW_KEY_KP_DECIMAL -> Key.Keypad_Decimal
        GLFW_KEY_KP_ENTER -> Key.Keypad_Enter

        GLFW_KEY_F1 -> Key.F1
        GLFW_KEY_F2 -> Key.F2
        GLFW_KEY_F3 -> Key.F3
        GLFW_KEY_F4 -> Key.F4
        GLFW_KEY_F5 -> Key.F5
        GLFW_KEY_F6 -> Key.F6
        GLFW_KEY_F7 -> Key.F7
        GLFW_KEY_F8 -> Key.F8
        GLFW_KEY_F9 -> Key.F9
        GLFW_KEY_F10 -> Key.F10
        GLFW_KEY_F11 -> Key.F11
        GLFW_KEY_F12 -> Key.F12

        else -> null
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun CursorMode.ToNativeValue(): Int {
    return when (this) {
        CursorMode.Normal -> GLFW_CURSOR_NORMAL
        CursorMode.Hidden -> GLFW_CURSOR_HIDDEN
        CursorMode.Disabled -> GLFW_CURSOR_DISABLED
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun MouseButton.ToNativeValue(): Int {
    return when (this) {
        MouseButton.Button_1 -> GLFW_MOUSE_BUTTON_1
        MouseButton.Button_2 -> GLFW_MOUSE_BUTTON_2
        MouseButton.Button_3 -> GLFW_MOUSE_BUTTON_3
        MouseButton.Button_4 -> GLFW_MOUSE_BUTTON_4
        MouseButton.Button_5 -> GLFW_MOUSE_BUTTON_5
        MouseButton.Button_6 -> GLFW_MOUSE_BUTTON_6
        MouseButton.Button_7 -> GLFW_MOUSE_BUTTON_7
        MouseButton.Button_8 -> GLFW_MOUSE_BUTTON_8
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun Key.ToNativeValue(): Int {
    return when (this) {
        Key.Letter_A -> GLFW_KEY_A
        Key.Letter_B -> GLFW_KEY_B
        Key.Letter_C -> GLFW_KEY_C
        Key.Letter_D -> GLFW_KEY_D
        Key.Letter_E -> GLFW_KEY_E
        Key.Letter_F -> GLFW_KEY_F
        Key.Letter_G -> GLFW_KEY_G
        Key.Letter_H -> GLFW_KEY_H
        Key.Letter_I -> GLFW_KEY_I
        Key.Letter_J -> GLFW_KEY_J
        Key.Letter_K -> GLFW_KEY_K
        Key.Letter_L -> GLFW_KEY_L
        Key.Letter_M -> GLFW_KEY_M
        Key.Letter_N -> GLFW_KEY_N
        Key.Letter_O -> GLFW_KEY_O
        Key.Letter_P -> GLFW_KEY_P
        Key.Letter_Q -> GLFW_KEY_Q
        Key.Letter_R -> GLFW_KEY_R
        Key.Letter_S -> GLFW_KEY_S
        Key.Letter_T -> GLFW_KEY_T
        Key.Letter_U -> GLFW_KEY_U
        Key.Letter_V -> GLFW_KEY_V
        Key.Letter_W -> GLFW_KEY_W
        Key.Letter_X -> GLFW_KEY_X
        Key.Letter_Y -> GLFW_KEY_Y
        Key.Letter_Z -> GLFW_KEY_Z

        Key.Digit_0 -> GLFW_KEY_0
        Key.Digit_1 -> GLFW_KEY_1
        Key.Digit_2 -> GLFW_KEY_2
        Key.Digit_3 -> GLFW_KEY_3
        Key.Digit_4 -> GLFW_KEY_4
        Key.Digit_5 -> GLFW_KEY_5
        Key.Digit_6 -> GLFW_KEY_6
        Key.Digit_7 -> GLFW_KEY_7
        Key.Digit_8 -> GLFW_KEY_8
        Key.Digit_9 -> GLFW_KEY_9

        Key.Up -> GLFW_KEY_UP
        Key.Down -> GLFW_KEY_DOWN
        Key.Left -> GLFW_KEY_LEFT
        Key.Right -> GLFW_KEY_RIGHT

        Key.Space -> GLFW_KEY_SPACE
        Key.Backspace -> GLFW_KEY_BACKSPACE
        Key.Delete -> GLFW_KEY_DELETE
        Key.Enter -> GLFW_KEY_ENTER

        Key.Left_Alt -> GLFW_KEY_LEFT_ALT
        Key.Left_Control -> GLFW_KEY_LEFT_CONTROL
        Key.Left_Shift -> GLFW_KEY_LEFT_SHIFT

        Key.Right_Alt -> GLFW_KEY_RIGHT_ALT
        Key.Right_Control -> GLFW_KEY_RIGHT_CONTROL
        Key.Right_Shift -> GLFW_KEY_RIGHT_SHIFT

        Key.CapsLock -> GLFW_KEY_CAPS_LOCK
        Key.Tab -> GLFW_KEY_TAB
        Key.Escape -> GLFW_KEY_ESCAPE

        Key.Keypad_0 -> GLFW_KEY_KP_0
        Key.Keypad_1 -> GLFW_KEY_KP_1
        Key.Keypad_2 -> GLFW_KEY_KP_2
        Key.Keypad_3 -> GLFW_KEY_KP_3
        Key.Keypad_4 -> GLFW_KEY_KP_4
        Key.Keypad_5 -> GLFW_KEY_KP_5
        Key.Keypad_6 -> GLFW_KEY_KP_6
        Key.Keypad_7 -> GLFW_KEY_KP_7
        Key.Keypad_8 -> GLFW_KEY_KP_8
        Key.Keypad_9 -> GLFW_KEY_KP_9

        Key.Keypad_Add -> GLFW_KEY_KP_ADD
        Key.Keypad_Subtract -> GLFW_KEY_KP_SUBTRACT
        Key.Keypad_Multiply -> GLFW_KEY_KP_MULTIPLY
        Key.Keypad_Divide -> GLFW_KEY_KP_DIVIDE
        Key.Keypad_Decimal -> GLFW_KEY_KP_DECIMAL
        Key.Keypad_Enter -> GLFW_KEY_KP_ENTER

        Key.F1 -> GLFW_KEY_F1
        Key.F2 -> GLFW_KEY_F2
        Key.F3 -> GLFW_KEY_F3
        Key.F4 -> GLFW_KEY_F4
        Key.F5 -> GLFW_KEY_F5
        Key.F6 -> GLFW_KEY_F6
        Key.F7 -> GLFW_KEY_F7
        Key.F8 -> GLFW_KEY_F8
        Key.F9 -> GLFW_KEY_F9
        Key.F10 -> GLFW_KEY_F10
        Key.F11 -> GLFW_KEY_F11
        Key.F12 -> GLFW_KEY_F12
    }
}
