package com.denis535.game_engine_pro

import cnames.structs.*
import com.denis535.sdl.*
import kotlinx.cinterop.*
import kotlin.experimental.*

public abstract class MainWindowImpl : MainWindow {

    @OptIn(ExperimentalForeignApi::class)
    private var _NativeWindow: CPointer<SDL_Window>? = null

    @OptIn(ExperimentalForeignApi::class)
    public override val IsClosed: Boolean
        get() {
            return this._NativeWindow == null
        }

    @OptIn(ExperimentalForeignApi::class)
    protected val NativeWindow: CPointer<SDL_Window>
        get() {
            check(!this.IsClosed)
            return this._NativeWindow!!
        }

    @OptIn(ExperimentalForeignApi::class)
    public override val Time: Double
        get() {
            val ticks = SDL_GetTicks().also { Sdl.ThrowErrorIfNeeded() }
            return ticks.toDouble() / 1000.0
        }

    @OptIn(ExperimentalForeignApi::class)
    public override val IsFullScreen: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_FULLSCREEN != 0UL
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var Title: String
        get() {
            check(!this.IsClosed)
            val title = SDL_GetWindowTitle(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            return title!!.toKString()
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowTitle(this.NativeWindow, value).also { Sdl.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var Position: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            memScoped {
                val posX = this.alloc<IntVar>()
                val posY = this.alloc<IntVar>()
                SDL_GetWindowPosition(this@MainWindowImpl.NativeWindow, posX.ptr, posY.ptr).also { Sdl.ThrowErrorIfNeeded() }
                return Pair(posX.value, posY.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowPosition(this.NativeWindow, value.first, value.second).also { Sdl.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var Size: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            memScoped {
                val posX = this.alloc<IntVar>()
                val posY = this.alloc<IntVar>()
                SDL_GetWindowSize(this@MainWindowImpl.NativeWindow, posX.ptr, posY.ptr).also { Sdl.ThrowErrorIfNeeded() }
                return Pair(posX.value, posY.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowSize(this.NativeWindow, value.first, value.second).also { Sdl.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public override val IsVisible: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_HIDDEN == 0UL && flags and SDL_WINDOW_MINIMIZED == 0UL
        }

    @OptIn(ExperimentalForeignApi::class)
    public override val IsFocused: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_INPUT_FOCUS == 0UL && flags and SDL_WINDOW_MOUSE_FOCUS == 0UL
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var IsCursorVisible: Boolean
        get() {
            check(!this.IsClosed)
            return SDL_CursorVisible().also { Sdl.ThrowErrorIfNeeded() }
        }
        set(value) {
            check(!this.IsClosed)
            if (value) {
                SDL_ShowCursor().also { Sdl.ThrowErrorIfNeeded() }
            } else {
                SDL_HideCursor().also { Sdl.ThrowErrorIfNeeded() }
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var IsCursorGrabbed: Boolean
        get() {
            check(!this.IsClosed)
            return SDL_GetWindowMouseGrab(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowMouseGrab(this.NativeWindow, true).also { Sdl.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var IsCursorCaptured: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_MOUSE_CAPTURE != 0uL
        }
        set(value) {
            check(!this.IsClosed)
            SDL_CaptureMouse(true).also { Sdl.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var IsCursorLocked: Boolean
        get() {
            check(!this.IsClosed)
            return SDL_GetWindowRelativeMouseMode(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowRelativeMouseMode(this.NativeWindow, value).also { Sdl.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
    public constructor(desc: MainWindowDesc) {
        SDL_Init(SDL_INIT_VIDEO).also { Sdl.ThrowErrorIfNeeded() }
        this._NativeWindow = run {
            when (desc) {
                is MainWindowDesc.FullScreen -> {
                    val display = SDL_GetPrimaryDisplay().also { Sdl.ThrowErrorIfNeeded() }
                    val displayMode = SDL_GetDesktopDisplayMode(display).also { Sdl.ThrowErrorIfNeeded() }!!
                    val flags = SDL_WINDOW_FULLSCREEN
                    SDL_CreateWindow(desc.Title, displayMode.pointed.w, displayMode.pointed.h, flags).also { Sdl.ThrowErrorIfNeeded() }
                }
                is MainWindowDesc.Window -> {
                    var flags = 0UL
                    if (desc.IsResizable) flags = flags or SDL_WINDOW_RESIZABLE
                    SDL_CreateWindow(desc.Title, desc.Width, desc.Height, flags).also { Sdl.ThrowErrorIfNeeded() }.also {
                        SDL_SetWindowPosition(it, SDL_WINDOWPOS_CENTERED.toInt(), SDL_WINDOWPOS_CENTERED.toInt()).also { Sdl.ThrowErrorIfNeeded() }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        check(!this.IsRunning)
        this._NativeWindow = run {
            SDL_DestroyWindow(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            null
        }
        SDL_Quit().also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun Show() {
        check(!this.IsClosed)
        SDL_ShowWindow(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
        SDL_RaiseWindow(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun Hide() {
        check(!this.IsClosed)
        SDL_HideWindow(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun MakeFullScreen() {
        check(!this.IsClosed)
        SDL_SetWindowFullscreen(this.NativeWindow, true).also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun MakeWindowed(width: Int, height: Int, isResizable: Boolean) {
        check(!this.IsClosed)
        SDL_SetWindowFullscreen(this.NativeWindow, false).also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun Close() {
        memScoped {
            val event = this.alloc<SDL_Event>()
            event.type = SDL_EVENT_WINDOW_CLOSE_REQUESTED
            event.window.windowID = SDL_GetWindowID(this@MainWindowImpl.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            SDL_PushEvent(event.ptr).also { Sdl.ThrowErrorIfNeeded() }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun Quit() {
        memScoped {
            val event = this.alloc<SDL_Event>()
            event.type = SDL_EVENT_QUIT
            SDL_PushEvent(event.ptr).also { Sdl.ThrowErrorIfNeeded() }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun ProcessEvents(info: FrameInfo): Boolean {
        memScoped {
            val event = this.alloc<SDL_Event>()
            while (SDL_PollEvent(event.ptr)) {
                this@MainWindowImpl.OnEvent(event.ptr)
                if (event.window.windowID == SDL_GetWindowID(this@MainWindowImpl.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }) {
                    if (event.type == SDL_EVENT_WINDOW_CLOSE_REQUESTED) {
                        return true
                    }
                }
                if (event.type == SDL_EVENT_QUIT) {
                    return true
                }
            }
        }
        return false
    }

    @OptIn(ExperimentalForeignApi::class)
    protected open fun OnEvent(event: CPointer<SDL_Event>) {
        if (event.pointed.window.windowID == SDL_GetWindowID(this@MainWindowImpl.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }) {
//            if (event.pointed.type == SDL_EVENT_WINDOW_SHOWN) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_HIDDEN) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_EXPOSED) {
//            }
//
//            if (event.pointed.type == SDL_EVENT_WINDOW_MOVED) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_RESIZED) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_PIXEL_SIZE_CHANGED) {
//            }

//            if (event.pointed.type == SDL_EVENT_WINDOW_ENTER_FULLSCREEN) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_LEAVE_FULLSCREEN) {
//            }
//
//            if (event.pointed.type == SDL_EVENT_WINDOW_MINIMIZED) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_MAXIMIZED) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_RESTORED) {
//            }
//
//            if (event.pointed.type == SDL_EVENT_WINDOW_FOCUS_GAINED) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_FOCUS_LOST) {
//            }

            if (event.pointed.type == SDL_EVENT_WINDOW_MOUSE_ENTER) {
                val windowEvent = event.pointed.window
                this.OnMouseCursorEnter()
            }
            if (event.pointed.type == SDL_EVENT_WINDOW_MOUSE_LEAVE) {
                val windowEvent = event.pointed.window
                this.OnMouseCursorLeave()
            }

            if (event.pointed.type == SDL_EVENT_MOUSE_MOTION) {
                val motionEvent = event.pointed.motion
                val isLocked = SDL_GetWindowRelativeMouseMode(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
                val x = motionEvent.x
                val y = motionEvent.y
                val dx = motionEvent.xrel
                val dy = motionEvent.yrel
                val isLeftPressed = motionEvent.state and SDL_BUTTON_LMASK != 0U
                val isRightPressed = motionEvent.state and SDL_BUTTON_RMASK != 0U
                val isMiddlePressed = motionEvent.state and SDL_BUTTON_MMASK != 0U
                val isX1Pressed = motionEvent.state and SDL_BUTTON_X1MASK != 0U
                val isX2Pressed = motionEvent.state and SDL_BUTTON_X2MASK != 0U
            }
            if (event.pointed.type == SDL_EVENT_MOUSE_BUTTON_DOWN || event.pointed.type == SDL_EVENT_MOUSE_BUTTON_UP) {
                val buttonEvent = event.pointed.button
                val isLocked = SDL_GetWindowRelativeMouseMode(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
                val x = buttonEvent.x
                val y = buttonEvent.y
                val isPressed = buttonEvent.down
                val button = buttonEvent.button
                val clicks = buttonEvent.clicks
            }
            if (event.pointed.type == SDL_EVENT_MOUSE_WHEEL) {
                val wheelEvent = event.pointed.wheel
                val isLocked = SDL_GetWindowRelativeMouseMode(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
                val x = wheelEvent.mouse_x
                val y = wheelEvent.mouse_y
                if (wheelEvent.direction == SDL_MouseWheelDirection.SDL_MOUSEWHEEL_NORMAL) {
                    val scrollX = wheelEvent.x
                    val scrollY = wheelEvent.y
                    val scrollIX = wheelEvent.integer_x
                    val scrollIY = wheelEvent.integer_y
                } else {
                    val scrollX = -wheelEvent.x
                    val scrollY = -wheelEvent.y
                    val scrollIX = -wheelEvent.integer_x
                    val scrollIY = -wheelEvent.integer_y
                }
            }

            if (event.pointed.type == SDL_EVENT_KEY_DOWN) {
            }
            if (event.pointed.type == SDL_EVENT_KEY_UP) {
            }

            if (event.pointed.type == SDL_EVENT_TEXT_INPUT) {
            }
        }
    }

//    @OptIn(ExperimentalForeignApi::class)
//    public override fun GetMouseCursorPosition(): Pair<Double, Double> {
//        check(!this.IsClosed)
//        memScoped {
//            val posX = this.alloc<DoubleVar>()
//            val posY = this.alloc<DoubleVar>()
//            glfwGetCursorPos(this@MainWindowImpl.NativeWindow, posX.ptr, posY.ptr).also { Glfw.ThrowErrorIfNeeded() }
//            return Pair(posX.value, posY.value)
//        }
//    }
//
//    @OptIn(ExperimentalForeignApi::class)
//    public override fun SetMouseCursorPosition(pos: Pair<Double, Double>) {
//        check(!this.IsClosed)
//        glfwSetCursorPos(this@MainWindowImpl.NativeWindow, pos.first, pos.second).also { Glfw.ThrowErrorIfNeeded() }
//    }
//
//    @OptIn(ExperimentalForeignApi::class)
//    public override fun GetMouseButtonPressed(button: MouseButton): Boolean {
//        check(!this.IsClosed)
//        return glfwGetMouseButton(this@MainWindowImpl.NativeWindow, button.ToNativeValue()).also { Glfw.ThrowErrorIfNeeded() } == GLFW_PRESS
//    }
//
//    @OptIn(ExperimentalForeignApi::class)
//    public override fun GetKeyPressed(key: Key): Boolean {
//        check(!this.IsClosed)
//        return glfwGetKey(this@MainWindowImpl.NativeWindow, key.ToNativeValue()).also { Glfw.ThrowErrorIfNeeded() } == GLFW_PRESS
//    }

}

//@OptIn(ExperimentalForeignApi::class)
//private fun Int.ToKey(): Key? {
//    return when (this) {
//        GLFW_KEY_A -> Key.Letter_A
//        GLFW_KEY_B -> Key.Letter_B
//        GLFW_KEY_C -> Key.Letter_C
//        GLFW_KEY_D -> Key.Letter_D
//        GLFW_KEY_E -> Key.Letter_E
//        GLFW_KEY_F -> Key.Letter_F
//        GLFW_KEY_G -> Key.Letter_G
//        GLFW_KEY_H -> Key.Letter_H
//        GLFW_KEY_I -> Key.Letter_I
//        GLFW_KEY_J -> Key.Letter_J
//        GLFW_KEY_K -> Key.Letter_K
//        GLFW_KEY_L -> Key.Letter_L
//        GLFW_KEY_M -> Key.Letter_M
//        GLFW_KEY_N -> Key.Letter_N
//        GLFW_KEY_O -> Key.Letter_O
//        GLFW_KEY_P -> Key.Letter_P
//        GLFW_KEY_Q -> Key.Letter_Q
//        GLFW_KEY_R -> Key.Letter_R
//        GLFW_KEY_S -> Key.Letter_S
//        GLFW_KEY_T -> Key.Letter_T
//        GLFW_KEY_U -> Key.Letter_U
//        GLFW_KEY_V -> Key.Letter_V
//        GLFW_KEY_W -> Key.Letter_W
//        GLFW_KEY_X -> Key.Letter_X
//        GLFW_KEY_Y -> Key.Letter_Y
//        GLFW_KEY_Z -> Key.Letter_Z
//
//        GLFW_KEY_0 -> Key.Digit_0
//        GLFW_KEY_1 -> Key.Digit_1
//        GLFW_KEY_2 -> Key.Digit_2
//        GLFW_KEY_3 -> Key.Digit_3
//        GLFW_KEY_4 -> Key.Digit_4
//        GLFW_KEY_5 -> Key.Digit_5
//        GLFW_KEY_6 -> Key.Digit_6
//        GLFW_KEY_7 -> Key.Digit_7
//        GLFW_KEY_8 -> Key.Digit_8
//        GLFW_KEY_9 -> Key.Digit_9
//
//        GLFW_KEY_UP -> Key.Up
//        GLFW_KEY_DOWN -> Key.Down
//        GLFW_KEY_LEFT -> Key.Left
//        GLFW_KEY_RIGHT -> Key.Right
//
//        GLFW_KEY_SPACE -> Key.Space
//        GLFW_KEY_BACKSPACE -> Key.Backspace
//        GLFW_KEY_DELETE -> Key.Delete
//        GLFW_KEY_ENTER -> Key.Enter
//
//        GLFW_KEY_LEFT_ALT -> Key.Left_Alt
//        GLFW_KEY_LEFT_CONTROL -> Key.Left_Control
//        GLFW_KEY_LEFT_SHIFT -> Key.Left_Shift
//
//        GLFW_KEY_RIGHT_ALT -> Key.Right_Alt
//        GLFW_KEY_RIGHT_CONTROL -> Key.Right_Control
//        GLFW_KEY_RIGHT_SHIFT -> Key.Right_Shift
//
//        GLFW_KEY_CAPS_LOCK -> Key.CapsLock
//        GLFW_KEY_TAB -> Key.Tab
//        GLFW_KEY_ESCAPE -> Key.Escape
//
//        GLFW_KEY_KP_0 -> Key.Keypad_0
//        GLFW_KEY_KP_1 -> Key.Keypad_1
//        GLFW_KEY_KP_2 -> Key.Keypad_2
//        GLFW_KEY_KP_3 -> Key.Keypad_3
//        GLFW_KEY_KP_4 -> Key.Keypad_4
//        GLFW_KEY_KP_5 -> Key.Keypad_5
//        GLFW_KEY_KP_6 -> Key.Keypad_6
//        GLFW_KEY_KP_7 -> Key.Keypad_7
//        GLFW_KEY_KP_8 -> Key.Keypad_8
//        GLFW_KEY_KP_9 -> Key.Keypad_9
//
//        GLFW_KEY_KP_ADD -> Key.Keypad_Add
//        GLFW_KEY_KP_SUBTRACT -> Key.Keypad_Subtract
//        GLFW_KEY_KP_MULTIPLY -> Key.Keypad_Multiply
//        GLFW_KEY_KP_DIVIDE -> Key.Keypad_Divide
//        GLFW_KEY_KP_DECIMAL -> Key.Keypad_Decimal
//        GLFW_KEY_KP_ENTER -> Key.Keypad_Enter
//
//        GLFW_KEY_F1 -> Key.F1
//        GLFW_KEY_F2 -> Key.F2
//        GLFW_KEY_F3 -> Key.F3
//        GLFW_KEY_F4 -> Key.F4
//        GLFW_KEY_F5 -> Key.F5
//        GLFW_KEY_F6 -> Key.F6
//        GLFW_KEY_F7 -> Key.F7
//        GLFW_KEY_F8 -> Key.F8
//        GLFW_KEY_F9 -> Key.F9
//        GLFW_KEY_F10 -> Key.F10
//        GLFW_KEY_F11 -> Key.F11
//        GLFW_KEY_F12 -> Key.F12
//
//        else -> null
//    }
//}
//
//@OptIn(ExperimentalForeignApi::class)
//private fun CursorMode.ToNativeValue(): Int {
//    return when (this) {
//        CursorMode.Normal -> GLFW_CURSOR_NORMAL
//        CursorMode.Hidden -> GLFW_CURSOR_HIDDEN
//        CursorMode.Disabled -> GLFW_CURSOR_DISABLED
//    }
//}
//
//@OptIn(ExperimentalForeignApi::class)
//private fun MouseButton.ToNativeValue(): Int {
//    return when (this) {
//        MouseButton.Button_1 -> GLFW_MOUSE_BUTTON_1
//        MouseButton.Button_2 -> GLFW_MOUSE_BUTTON_2
//        MouseButton.Button_3 -> GLFW_MOUSE_BUTTON_3
//        MouseButton.Button_4 -> GLFW_MOUSE_BUTTON_4
//        MouseButton.Button_5 -> GLFW_MOUSE_BUTTON_5
//        MouseButton.Button_6 -> GLFW_MOUSE_BUTTON_6
//        MouseButton.Button_7 -> GLFW_MOUSE_BUTTON_7
//        MouseButton.Button_8 -> GLFW_MOUSE_BUTTON_8
//    }
//}
//
//@OptIn(ExperimentalForeignApi::class)
//private fun Key.ToNativeValue(): Int {
//    return when (this) {
//        Key.Letter_A -> GLFW_KEY_A
//        Key.Letter_B -> GLFW_KEY_B
//        Key.Letter_C -> GLFW_KEY_C
//        Key.Letter_D -> GLFW_KEY_D
//        Key.Letter_E -> GLFW_KEY_E
//        Key.Letter_F -> GLFW_KEY_F
//        Key.Letter_G -> GLFW_KEY_G
//        Key.Letter_H -> GLFW_KEY_H
//        Key.Letter_I -> GLFW_KEY_I
//        Key.Letter_J -> GLFW_KEY_J
//        Key.Letter_K -> GLFW_KEY_K
//        Key.Letter_L -> GLFW_KEY_L
//        Key.Letter_M -> GLFW_KEY_M
//        Key.Letter_N -> GLFW_KEY_N
//        Key.Letter_O -> GLFW_KEY_O
//        Key.Letter_P -> GLFW_KEY_P
//        Key.Letter_Q -> GLFW_KEY_Q
//        Key.Letter_R -> GLFW_KEY_R
//        Key.Letter_S -> GLFW_KEY_S
//        Key.Letter_T -> GLFW_KEY_T
//        Key.Letter_U -> GLFW_KEY_U
//        Key.Letter_V -> GLFW_KEY_V
//        Key.Letter_W -> GLFW_KEY_W
//        Key.Letter_X -> GLFW_KEY_X
//        Key.Letter_Y -> GLFW_KEY_Y
//        Key.Letter_Z -> GLFW_KEY_Z
//
//        Key.Digit_0 -> GLFW_KEY_0
//        Key.Digit_1 -> GLFW_KEY_1
//        Key.Digit_2 -> GLFW_KEY_2
//        Key.Digit_3 -> GLFW_KEY_3
//        Key.Digit_4 -> GLFW_KEY_4
//        Key.Digit_5 -> GLFW_KEY_5
//        Key.Digit_6 -> GLFW_KEY_6
//        Key.Digit_7 -> GLFW_KEY_7
//        Key.Digit_8 -> GLFW_KEY_8
//        Key.Digit_9 -> GLFW_KEY_9
//
//        Key.Up -> GLFW_KEY_UP
//        Key.Down -> GLFW_KEY_DOWN
//        Key.Left -> GLFW_KEY_LEFT
//        Key.Right -> GLFW_KEY_RIGHT
//
//        Key.Space -> GLFW_KEY_SPACE
//        Key.Backspace -> GLFW_KEY_BACKSPACE
//        Key.Delete -> GLFW_KEY_DELETE
//        Key.Enter -> GLFW_KEY_ENTER
//
//        Key.Left_Alt -> GLFW_KEY_LEFT_ALT
//        Key.Left_Control -> GLFW_KEY_LEFT_CONTROL
//        Key.Left_Shift -> GLFW_KEY_LEFT_SHIFT
//
//        Key.Right_Alt -> GLFW_KEY_RIGHT_ALT
//        Key.Right_Control -> GLFW_KEY_RIGHT_CONTROL
//        Key.Right_Shift -> GLFW_KEY_RIGHT_SHIFT
//
//        Key.CapsLock -> GLFW_KEY_CAPS_LOCK
//        Key.Tab -> GLFW_KEY_TAB
//        Key.Escape -> GLFW_KEY_ESCAPE
//
//        Key.Keypad_0 -> GLFW_KEY_KP_0
//        Key.Keypad_1 -> GLFW_KEY_KP_1
//        Key.Keypad_2 -> GLFW_KEY_KP_2
//        Key.Keypad_3 -> GLFW_KEY_KP_3
//        Key.Keypad_4 -> GLFW_KEY_KP_4
//        Key.Keypad_5 -> GLFW_KEY_KP_5
//        Key.Keypad_6 -> GLFW_KEY_KP_6
//        Key.Keypad_7 -> GLFW_KEY_KP_7
//        Key.Keypad_8 -> GLFW_KEY_KP_8
//        Key.Keypad_9 -> GLFW_KEY_KP_9
//
//        Key.Keypad_Add -> GLFW_KEY_KP_ADD
//        Key.Keypad_Subtract -> GLFW_KEY_KP_SUBTRACT
//        Key.Keypad_Multiply -> GLFW_KEY_KP_MULTIPLY
//        Key.Keypad_Divide -> GLFW_KEY_KP_DIVIDE
//        Key.Keypad_Decimal -> GLFW_KEY_KP_DECIMAL
//        Key.Keypad_Enter -> GLFW_KEY_KP_ENTER
//
//        Key.F1 -> GLFW_KEY_F1
//        Key.F2 -> GLFW_KEY_F2
//        Key.F3 -> GLFW_KEY_F3
//        Key.F4 -> GLFW_KEY_F4
//        Key.F5 -> GLFW_KEY_F5
//        Key.F6 -> GLFW_KEY_F6
//        Key.F7 -> GLFW_KEY_F7
//        Key.F8 -> GLFW_KEY_F8
//        Key.F9 -> GLFW_KEY_F9
//        Key.F10 -> GLFW_KEY_F10
//        Key.F11 -> GLFW_KEY_F11
//        Key.F12 -> GLFW_KEY_F12
//    }
//}
