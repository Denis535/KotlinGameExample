package com.denis535.game_engine_pro

import cnames.structs.*
import com.denis535.sdl.*
import kotlinx.cinterop.*
import kotlin.experimental.*

private typealias ECursor = Cursor

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
            check(!this.IsClosed)
            val ticks = SDL_GetTicks().also { Sdl.ThrowErrorIfNeeded() }
            return ticks.toDouble() / 1000.0
        }

    @OptIn(ExperimentalForeignApi::class)
    public override var IsFullScreen: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_FULLSCREEN != 0UL
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowFullscreen(this.NativeWindow, value).also { Sdl.ThrowErrorIfNeeded() }
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
    public override var IsResizable: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_RESIZABLE != 0UL
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowResizable(this.NativeWindow, value).also { Sdl.ThrowErrorIfNeeded() }
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
    public override var Cursor: Cursor
        get() {
            error("Not implemented")
        }
        set(value) {
            val cursor = SDL_CreateSystemCursor(value.ToNativeValue()).also { Sdl.ThrowErrorIfNeeded() }
            SDL_SetCursor(cursor).also { Sdl.ThrowErrorIfNeeded() }
//            SDL_DestroyCursor(cursor).also { Sdl.ThrowErrorIfNeeded() }
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
            SDL_SetWindowMouseGrab(this.NativeWindow, value).also { Sdl.ThrowErrorIfNeeded() }
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
            SDL_CaptureMouse(value).also { Sdl.ThrowErrorIfNeeded() }
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

    @OptIn(ExperimentalForeignApi::class)
    public override var IsTextInputEnabled: Boolean
        get() {
            check(!this.IsClosed)
            return SDL_TextInputActive(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
        }
        set(value) {
            check(!this.IsClosed)
            if (value) {
                SDL_StartTextInput(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            } else {
                SDL_StopTextInput(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            }
        }

    @OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
    public constructor(desc: MainWindowDesc) {
        SDL_Init(SDL_INIT_VIDEO).also { Sdl.ThrowErrorIfNeeded() }
        this._NativeWindow = run {
            when (desc) {
                is MainWindowDesc.FullScreen -> {
                    var flags = 0UL
                    flags = flags or SDL_WINDOW_FULLSCREEN
                    SDL_CreateWindow(desc.Title, 0, 0, flags).also { Sdl.ThrowErrorIfNeeded() }
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
    public override fun RequestClose() {
        check(!this.IsClosed)
        memScoped {
            val event = this.alloc<SDL_Event>()
            event.type = SDL_EVENT_WINDOW_CLOSE_REQUESTED
            event.window.windowID = SDL_GetWindowID(this@MainWindowImpl.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            SDL_PushEvent(event.ptr).also { Sdl.ThrowErrorIfNeeded() }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun RequestQuit() {
        check(!this.IsClosed)
        memScoped {
            val event = this.alloc<SDL_Event>()
            event.type = SDL_EVENT_QUIT
            SDL_PushEvent(event.ptr).also { Sdl.ThrowErrorIfNeeded() }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    protected override fun ProcessEvents(info: FrameInfo): Boolean {
        check(!this.IsClosed)
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
        check(!this.IsClosed)
        if (event.pointed.window.windowID == SDL_GetWindowID(this@MainWindowImpl.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }) {
//            if (event.pointed.type == SDL_EVENT_WINDOW_SHOWN) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_HIDDEN) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_EXPOSED) {
//            }

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

//            if (event.pointed.type == SDL_EVENT_WINDOW_MINIMIZED) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_MAXIMIZED) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_RESTORED) {
//            }

//            if (event.pointed.type == SDL_EVENT_WINDOW_FOCUS_GAINED) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_FOCUS_LOST) {
//            }

//            if (event.pointed.type == SDL_EVENT_WINDOW_MOUSE_ENTER) {
//            }
//            if (event.pointed.type == SDL_EVENT_WINDOW_MOUSE_LEAVE) {
//            }

            if (event.pointed.type == SDL_EVENT_MOUSE_MOTION) {
                val motionEvent = event.pointed.motion
                val isCursorLocked = SDL_GetWindowRelativeMouseMode(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
                val cursorX = motionEvent.x
                val cursorY = motionEvent.y
                val cursorDeltaX = motionEvent.xrel
                val cursorDeltaY = motionEvent.yrel
                this.OnMouseCursorMove(MouseCursorMoveEvent(isCursorLocked, cursorX, cursorY, cursorDeltaX, cursorDeltaY))
            }
            if (event.pointed.type == SDL_EVENT_MOUSE_BUTTON_DOWN || event.pointed.type == SDL_EVENT_MOUSE_BUTTON_UP) {
                val buttonEvent = event.pointed.button
                val isCursorLocked = SDL_GetWindowRelativeMouseMode(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
                val cursorX = buttonEvent.x
                val cursorY = buttonEvent.y
                val isButtonPressed = buttonEvent.down
                val button = buttonEvent.button.ToMouseButton()
                val clicks = buttonEvent.clicks.toInt()
                if (button != null) {
                    if (isButtonPressed) {
                        this.OnMouseButtonPress(MouseButtonActionEvent(isCursorLocked, cursorX, cursorY, button, clicks))
                    } else {
                        this.OnMouseButtonRelease(MouseButtonActionEvent(isCursorLocked, cursorX, cursorY, button, clicks))
                    }
                }
            }
            if (event.pointed.type == SDL_EVENT_MOUSE_WHEEL) {
                val wheelEvent = event.pointed.wheel
                val isCursorLocked = SDL_GetWindowRelativeMouseMode(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
                val cursorX = wheelEvent.mouse_x
                val cursorY = wheelEvent.mouse_y
                val isDirectionNormal = wheelEvent.direction == SDL_MouseWheelDirection.SDL_MOUSEWHEEL_NORMAL
                val scrollX: Float
                val scrollY: Float
                val scrollIntegerX: Int
                val scrollIntegerY: Int
                if (isDirectionNormal) {
                    scrollX = wheelEvent.x
                    scrollY = wheelEvent.y
                    scrollIntegerX = wheelEvent.integer_x
                    scrollIntegerY = wheelEvent.integer_y
                } else {
                    scrollX = -wheelEvent.x
                    scrollY = -wheelEvent.y
                    scrollIntegerX = -wheelEvent.integer_x
                    scrollIntegerY = -wheelEvent.integer_y
                }
                this.OnMouseWheelScroll(MouseWheelScrollEvent(isCursorLocked, cursorX, cursorY, scrollX, scrollY, scrollIntegerX, scrollIntegerY))
            }

            if (event.pointed.type == SDL_EVENT_KEY_DOWN || event.pointed.type == SDL_EVENT_KEY_UP) {
                val keyEvent = event.pointed.key
                val isKeyPressed = keyEvent.down
                val isKeyRepeated = keyEvent.repeat
                val key = keyEvent.scancode.ToKey()
                if (key != null) {
                    if (isKeyPressed) {
                        if (!isKeyRepeated) {
                            this.OnKeyPress(KeyActionEvent(key))
                        } else {
                            this.OnKeyRepeat(KeyActionEvent(key))
                        }
                    } else {
                        this.OnKeyRelease(KeyActionEvent(key))
                    }
                }
            }

            if (event.pointed.type == SDL_EVENT_TEXT_INPUT) {
                val textEvent = event.pointed.text
                val text = textEvent.text?.toKStringFromUtf8()
                if (text != null) {
                    this.OnTextInput(text)
                }
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

@OptIn(ExperimentalForeignApi::class)
private fun SDL_SystemCursor.ToCursor(): Cursor? {
    return when (this) {
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_DEFAULT -> Cursor.Arrow
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_TEXT -> Cursor.Text
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_POINTER -> Cursor.Pointer
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_CROSSHAIR -> Cursor.Crosshair
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_PROGRESS -> Cursor.Progress
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NOT_ALLOWED -> Cursor.NotAllowed
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_MOVE -> Cursor.Move
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_WAIT -> Cursor.Wait

        SDL_SystemCursor.SDL_SYSTEM_CURSOR_N_RESIZE -> Cursor.SingleArrowResize_N
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_S_RESIZE -> Cursor.SingleArrowResize_S
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_W_RESIZE -> Cursor.SingleArrowResize_W
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_E_RESIZE -> Cursor.SingleArrowResize_E

        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NW_RESIZE -> Cursor.SingleArrowResize_N_W
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NE_RESIZE -> Cursor.SingleArrowResize_N_E
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_SW_RESIZE -> Cursor.SingleArrowResize_S_W
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_SE_RESIZE -> Cursor.SingleArrowResize_S_E

        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NS_RESIZE -> Cursor.DoubleArrowResize_N_S
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_EW_RESIZE -> Cursor.DoubleArrowResize_W_E

        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NWSE_RESIZE -> Cursor.DoubleArrowResize_NW_SE
        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NESW_RESIZE -> Cursor.DoubleArrowResize_NE_SW

        else -> null
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun UByte.ToMouseButton(): MouseButton? {
    return when (this.toInt()) {
        SDL_BUTTON_LEFT -> MouseButton.Left
        SDL_BUTTON_RIGHT -> MouseButton.Right
        SDL_BUTTON_MIDDLE -> MouseButton.Middle
        SDL_BUTTON_X1 -> MouseButton.X1
        SDL_BUTTON_X2 -> MouseButton.X2
        else -> null
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun SDL_Scancode.ToKey(): Key? {
    return when (this) {
        SDL_SCANCODE_A -> Key.Letter_A
        SDL_SCANCODE_B -> Key.Letter_B
        SDL_SCANCODE_C -> Key.Letter_C
        SDL_SCANCODE_D -> Key.Letter_D
        SDL_SCANCODE_E -> Key.Letter_E
        SDL_SCANCODE_F -> Key.Letter_F
        SDL_SCANCODE_G -> Key.Letter_G
        SDL_SCANCODE_H -> Key.Letter_H
        SDL_SCANCODE_I -> Key.Letter_I
        SDL_SCANCODE_J -> Key.Letter_J
        SDL_SCANCODE_K -> Key.Letter_K
        SDL_SCANCODE_L -> Key.Letter_L
        SDL_SCANCODE_M -> Key.Letter_M
        SDL_SCANCODE_N -> Key.Letter_N
        SDL_SCANCODE_O -> Key.Letter_O
        SDL_SCANCODE_P -> Key.Letter_P
        SDL_SCANCODE_Q -> Key.Letter_Q
        SDL_SCANCODE_R -> Key.Letter_R
        SDL_SCANCODE_S -> Key.Letter_S
        SDL_SCANCODE_T -> Key.Letter_T
        SDL_SCANCODE_U -> Key.Letter_U
        SDL_SCANCODE_V -> Key.Letter_V
        SDL_SCANCODE_W -> Key.Letter_W
        SDL_SCANCODE_X -> Key.Letter_X
        SDL_SCANCODE_Y -> Key.Letter_Y
        SDL_SCANCODE_Z -> Key.Letter_Z

        SDL_SCANCODE_0 -> Key.Digit_0
        SDL_SCANCODE_1 -> Key.Digit_1
        SDL_SCANCODE_2 -> Key.Digit_2
        SDL_SCANCODE_3 -> Key.Digit_3
        SDL_SCANCODE_4 -> Key.Digit_4
        SDL_SCANCODE_5 -> Key.Digit_5
        SDL_SCANCODE_6 -> Key.Digit_6
        SDL_SCANCODE_7 -> Key.Digit_7
        SDL_SCANCODE_8 -> Key.Digit_8
        SDL_SCANCODE_9 -> Key.Digit_9

        SDL_SCANCODE_UP -> Key.Up
        SDL_SCANCODE_DOWN -> Key.Down
        SDL_SCANCODE_LEFT -> Key.Left
        SDL_SCANCODE_RIGHT -> Key.Right

        SDL_SCANCODE_SPACE -> Key.Space
        SDL_SCANCODE_BACKSPACE -> Key.Backspace
        SDL_SCANCODE_DELETE -> Key.Delete
        SDL_SCANCODE_RETURN -> Key.Enter
        SDL_SCANCODE_TAB -> Key.Tab
        SDL_SCANCODE_ESCAPE -> Key.Escape

        SDL_SCANCODE_LALT -> Key.LeftAlt
        SDL_SCANCODE_LCTRL -> Key.LeftControl
        SDL_SCANCODE_LSHIFT -> Key.LeftShift

        SDL_SCANCODE_RALT -> Key.RightAlt
        SDL_SCANCODE_RCTRL -> Key.RightControl
        SDL_SCANCODE_RSHIFT -> Key.RightShift

        SDL_SCANCODE_CAPSLOCK -> Key.CapsLock
        SDL_SCANCODE_NUMLOCKCLEAR -> Key.NumLock

        SDL_SCANCODE_KP_0 -> Key.Keypad_0
        SDL_SCANCODE_KP_1 -> Key.Keypad_1
        SDL_SCANCODE_KP_2 -> Key.Keypad_2
        SDL_SCANCODE_KP_3 -> Key.Keypad_3
        SDL_SCANCODE_KP_4 -> Key.Keypad_4
        SDL_SCANCODE_KP_5 -> Key.Keypad_5
        SDL_SCANCODE_KP_6 -> Key.Keypad_6
        SDL_SCANCODE_KP_7 -> Key.Keypad_7
        SDL_SCANCODE_KP_8 -> Key.Keypad_8
        SDL_SCANCODE_KP_9 -> Key.Keypad_9
        SDL_SCANCODE_KP_DECIMAL -> Key.Keypad_Decimal
        SDL_SCANCODE_KP_PLUS -> Key.Keypad_Plus
        SDL_SCANCODE_KP_MINUS -> Key.Keypad_Minus
        SDL_SCANCODE_KP_MULTIPLY -> Key.Keypad_Multiply
        SDL_SCANCODE_KP_DIVIDE -> Key.Keypad_Divide
        SDL_SCANCODE_KP_ENTER -> Key.Keypad_Enter

        SDL_SCANCODE_F1 -> Key.F1
        SDL_SCANCODE_F2 -> Key.F2
        SDL_SCANCODE_F3 -> Key.F3
        SDL_SCANCODE_F4 -> Key.F4
        SDL_SCANCODE_F5 -> Key.F5
        SDL_SCANCODE_F6 -> Key.F6
        SDL_SCANCODE_F7 -> Key.F7
        SDL_SCANCODE_F8 -> Key.F8
        SDL_SCANCODE_F9 -> Key.F9
        SDL_SCANCODE_F10 -> Key.F10
        SDL_SCANCODE_F11 -> Key.F11
        SDL_SCANCODE_F12 -> Key.F12

        else -> null
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun Cursor.ToNativeValue(): SDL_SystemCursor {
    return when (this) {
        Cursor.Arrow -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_DEFAULT
        Cursor.Text -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_TEXT
        Cursor.Pointer -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_POINTER
        Cursor.Crosshair -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_CROSSHAIR
        Cursor.Progress -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_PROGRESS
        Cursor.NotAllowed -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NOT_ALLOWED
        Cursor.Move -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_MOVE
        Cursor.Wait -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_WAIT

        Cursor.SingleArrowResize_N -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_N_RESIZE
        Cursor.SingleArrowResize_S -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_S_RESIZE
        Cursor.SingleArrowResize_W -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_W_RESIZE
        Cursor.SingleArrowResize_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_E_RESIZE

        Cursor.SingleArrowResize_N_W -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NW_RESIZE
        Cursor.SingleArrowResize_N_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NE_RESIZE
        Cursor.SingleArrowResize_S_W -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_SW_RESIZE
        Cursor.SingleArrowResize_S_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_SE_RESIZE

        Cursor.DoubleArrowResize_N_S -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NS_RESIZE
        Cursor.DoubleArrowResize_W_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_EW_RESIZE

        Cursor.DoubleArrowResize_NW_SE -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NWSE_RESIZE
        Cursor.DoubleArrowResize_NE_SW -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NESW_RESIZE
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun MouseButton.ToNativeValue(): Int {
    return when (this) {
        MouseButton.Left -> SDL_BUTTON_LEFT
        MouseButton.Right -> SDL_BUTTON_RIGHT
        MouseButton.Middle -> SDL_BUTTON_MIDDLE
        MouseButton.X1 -> SDL_BUTTON_X1
        MouseButton.X2 -> SDL_BUTTON_X2
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun Key.ToNativeValue(): UInt {
    return when (this) {
        Key.Letter_A -> SDL_SCANCODE_A
        Key.Letter_B -> SDL_SCANCODE_B
        Key.Letter_C -> SDL_SCANCODE_C
        Key.Letter_D -> SDL_SCANCODE_D
        Key.Letter_E -> SDL_SCANCODE_E
        Key.Letter_F -> SDL_SCANCODE_F
        Key.Letter_G -> SDL_SCANCODE_G
        Key.Letter_H -> SDL_SCANCODE_H
        Key.Letter_I -> SDL_SCANCODE_I
        Key.Letter_J -> SDL_SCANCODE_J
        Key.Letter_K -> SDL_SCANCODE_K
        Key.Letter_L -> SDL_SCANCODE_L
        Key.Letter_M -> SDL_SCANCODE_M
        Key.Letter_N -> SDL_SCANCODE_N
        Key.Letter_O -> SDL_SCANCODE_O
        Key.Letter_P -> SDL_SCANCODE_P
        Key.Letter_Q -> SDL_SCANCODE_Q
        Key.Letter_R -> SDL_SCANCODE_R
        Key.Letter_S -> SDL_SCANCODE_S
        Key.Letter_T -> SDL_SCANCODE_T
        Key.Letter_U -> SDL_SCANCODE_U
        Key.Letter_V -> SDL_SCANCODE_V
        Key.Letter_W -> SDL_SCANCODE_W
        Key.Letter_X -> SDL_SCANCODE_X
        Key.Letter_Y -> SDL_SCANCODE_Y
        Key.Letter_Z -> SDL_SCANCODE_Z

        Key.Digit_0 -> SDL_SCANCODE_0
        Key.Digit_1 -> SDL_SCANCODE_1
        Key.Digit_2 -> SDL_SCANCODE_2
        Key.Digit_3 -> SDL_SCANCODE_3
        Key.Digit_4 -> SDL_SCANCODE_4
        Key.Digit_5 -> SDL_SCANCODE_5
        Key.Digit_6 -> SDL_SCANCODE_6
        Key.Digit_7 -> SDL_SCANCODE_7
        Key.Digit_8 -> SDL_SCANCODE_8
        Key.Digit_9 -> SDL_SCANCODE_9

        Key.Up -> SDL_SCANCODE_UP
        Key.Down -> SDL_SCANCODE_DOWN
        Key.Left -> SDL_SCANCODE_LEFT
        Key.Right -> SDL_SCANCODE_RIGHT

        Key.Space -> SDL_SCANCODE_SPACE
        Key.Backspace -> SDL_SCANCODE_BACKSPACE
        Key.Delete -> SDL_SCANCODE_DELETE
        Key.Enter -> SDL_SCANCODE_RETURN
        Key.Tab -> SDL_SCANCODE_TAB
        Key.Escape -> SDL_SCANCODE_ESCAPE

        Key.LeftAlt -> SDL_SCANCODE_LALT
        Key.LeftControl -> SDL_SCANCODE_LCTRL
        Key.LeftShift -> SDL_SCANCODE_LSHIFT

        Key.RightAlt -> SDL_SCANCODE_RALT
        Key.RightControl -> SDL_SCANCODE_RCTRL
        Key.RightShift -> SDL_SCANCODE_RSHIFT

        Key.CapsLock -> SDL_SCANCODE_CAPSLOCK
        Key.NumLock -> SDL_SCANCODE_NUMLOCKCLEAR

        Key.Keypad_0 -> SDL_SCANCODE_KP_0
        Key.Keypad_1 -> SDL_SCANCODE_KP_1
        Key.Keypad_2 -> SDL_SCANCODE_KP_2
        Key.Keypad_3 -> SDL_SCANCODE_KP_3
        Key.Keypad_4 -> SDL_SCANCODE_KP_4
        Key.Keypad_5 -> SDL_SCANCODE_KP_5
        Key.Keypad_6 -> SDL_SCANCODE_KP_6
        Key.Keypad_7 -> SDL_SCANCODE_KP_7
        Key.Keypad_8 -> SDL_SCANCODE_KP_8
        Key.Keypad_9 -> SDL_SCANCODE_KP_9
        Key.Keypad_Decimal -> SDL_SCANCODE_KP_DECIMAL
        Key.Keypad_Plus -> SDL_SCANCODE_KP_PLUS
        Key.Keypad_Minus -> SDL_SCANCODE_KP_MINUS
        Key.Keypad_Multiply -> SDL_SCANCODE_KP_MULTIPLY
        Key.Keypad_Divide -> SDL_SCANCODE_KP_DIVIDE
        Key.Keypad_Enter -> SDL_SCANCODE_KP_ENTER

        Key.F1 -> SDL_SCANCODE_F1
        Key.F2 -> SDL_SCANCODE_F2
        Key.F3 -> SDL_SCANCODE_F3
        Key.F4 -> SDL_SCANCODE_F4
        Key.F5 -> SDL_SCANCODE_F5
        Key.F6 -> SDL_SCANCODE_F6
        Key.F7 -> SDL_SCANCODE_F7
        Key.F8 -> SDL_SCANCODE_F8
        Key.F9 -> SDL_SCANCODE_F9
        Key.F10 -> SDL_SCANCODE_F10
        Key.F11 -> SDL_SCANCODE_F11
        Key.F12 -> SDL_SCANCODE_F12
    }
}
