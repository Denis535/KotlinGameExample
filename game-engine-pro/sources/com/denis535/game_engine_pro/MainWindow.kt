package com.denis535.game_engine_pro

import cnames.structs.*
import com.denis535.sdl.*
import kotlinx.cinterop.*
import kotlin.experimental.*

public abstract class MainWindow : AutoCloseable {

    @OptIn(ExperimentalForeignApi::class)
    private var _NativeWindow: CPointer<SDL_Window>? = null

    @OptIn(ExperimentalForeignApi::class)
    public val IsClosed: Boolean
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
    internal val NativeWindowInternal: CPointer<SDL_Window>
        get() {
            return this.NativeWindow
        }

    @OptIn(ExperimentalForeignApi::class)
    public val Time: Double
        get() {
            check(!this.IsClosed)
            val ticks = SDL_GetTicks().also { Sdl.ThrowErrorIfNeeded() }
            return ticks.toDouble() / 1000.0
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsFullScreen: Boolean
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
    public var Title: String
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
    public var Position: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            memScoped {
                val posX = this.alloc<IntVar>()
                val posY = this.alloc<IntVar>()
                SDL_GetWindowPosition(this@MainWindow.NativeWindow, posX.ptr, posY.ptr).also { Sdl.ThrowErrorIfNeeded() }
                return Pair(posX.value, posY.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowPosition(this.NativeWindow, value.first, value.second).also { Sdl.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Size: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            memScoped {
                val posX = this.alloc<IntVar>()
                val posY = this.alloc<IntVar>()
                SDL_GetWindowSize(this@MainWindow.NativeWindow, posX.ptr, posY.ptr).also { Sdl.ThrowErrorIfNeeded() }
                return Pair(posX.value, posY.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowSize(this.NativeWindow, value.first, value.second).also { Sdl.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsResizable: Boolean
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
    public val IsVisible: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_HIDDEN == 0UL && flags and SDL_WINDOW_MINIMIZED == 0UL
        }

    @OptIn(ExperimentalForeignApi::class)
    public val IsFocused: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_INPUT_FOCUS == 0UL && flags and SDL_WINDOW_MOUSE_FOCUS == 0UL
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsTextInputEnabled: Boolean
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

    public val Cursor: Cursor = Cursor(this)

    public var IsRunning: Boolean = false
        private set(value) {
            check(!this.IsClosed)
            check(field != value)
            field = value
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
    public open fun Show() {
        check(!this.IsClosed)
        SDL_ShowWindow(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
        SDL_RaiseWindow(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public open fun Hide() {
        check(!this.IsClosed)
        SDL_HideWindow(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun RequestClose() {
        check(!this.IsClosed)
        memScoped {
            val event = this.alloc<SDL_Event>()
            event.type = SDL_EVENT_WINDOW_CLOSE_REQUESTED
            event.window.windowID = SDL_GetWindowID(this@MainWindow.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            SDL_PushEvent(event.ptr).also { Sdl.ThrowErrorIfNeeded() }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun RequestQuit() {
        check(!this.IsClosed)
        memScoped {
            val event = this.alloc<SDL_Event>()
            event.type = SDL_EVENT_QUIT
            SDL_PushEvent(event.ptr).also { Sdl.ThrowErrorIfNeeded() }
        }
    }

    public fun Run(fixedDeltaTime: Float = 1.0f / 20.0f) {
        check(!this.IsClosed)
        check(!this.IsRunning)
        this.IsRunning = true
        val info = FrameInfo()
        this.OnStart()
        while (true) {
            val startTime = this.Time
            run {
                val isClosingRequested = this.ProcessEvents(info)
                if (isClosingRequested) {
                    break
                }
                this.OnFrameBegin(info)
                if (info.FixedFrameInfo.Number == 0) {
                    this.OnFixedUpdate(info)
                    info.FixedFrameInfo.Number++
                    info.FixedFrameInfo.DeltaTime = fixedDeltaTime
                } else {
                    while (info.FixedFrameInfo.Time <= info.Time) {
                        this.OnFixedUpdate(info)
                        info.FixedFrameInfo.Number++
                        info.FixedFrameInfo.DeltaTime = fixedDeltaTime
                    }
                }
                this.OnUpdate(info)
                this.OnDraw(info)
                this.OnFrameEnd(info)
            }
            val endTime = this.Time
            val deltaTime = (endTime - startTime).toFloat()
            info.Number++
            info.Time += deltaTime
            info.DeltaTime = deltaTime
        }
        this.OnStop()
        this.IsRunning = false
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun ProcessEvents(info: FrameInfo): Boolean {
        check(!this.IsClosed)
        memScoped {
            val event = this.alloc<SDL_Event>()
            while (SDL_PollEvent(event.ptr)) {
                this@MainWindow.OnEvent(event.ptr)
                if (event.window.windowID == SDL_GetWindowID(this@MainWindow.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }) {
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

    protected abstract fun OnStart()
    protected abstract fun OnStop()

    @OptIn(ExperimentalForeignApi::class)
    protected open fun OnEvent(event: CPointer<SDL_Event>) {
        check(!this.IsClosed)
        if (event.pointed.window.windowID == SDL_GetWindowID(this@MainWindow.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }) {
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
                val button = MouseButton.FromNativeValue(buttonEvent.button)
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
                val key = Key.FromNativeValue(keyEvent.scancode)
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

    protected abstract fun OnFrameBegin(info: FrameInfo)
    protected abstract fun OnFixedUpdate(info: FrameInfo)
    protected abstract fun OnUpdate(info: FrameInfo)
    protected abstract fun OnDraw(info: FrameInfo)
    protected abstract fun OnFrameEnd(info: FrameInfo)

    protected abstract fun OnMouseCursorMove(event: MouseCursorMoveEvent)
    protected abstract fun OnMouseButtonPress(event: MouseButtonActionEvent)
    protected abstract fun OnMouseButtonRelease(event: MouseButtonActionEvent)
    protected abstract fun OnMouseWheelScroll(event: MouseWheelScrollEvent)

    protected abstract fun OnKeyPress(event: KeyActionEvent)
    protected abstract fun OnKeyRepeat(event: KeyActionEvent)
    protected abstract fun OnKeyRelease(event: KeyActionEvent)

    protected abstract fun OnTextInput(text: String)

    //    public abstract fun GetMouseCursorPosition(): Pair<Double, Double>
    //    public abstract fun GetMouseButtonPressed(button: MouseButton): Boolean
    //
    //    public abstract fun GetKeyPressed(key: Key): Boolean

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

public class MouseCursorMoveEvent(
    public val IsCursorLocked: Boolean,
    public val CursorX: Float,
    public val CursorY: Float,
    public val CursorDeltaX: Float,
    public val CursorDeltaY: Float,
)

public class MouseButtonActionEvent(
    public val IsCursorLocked: Boolean,
    public val CursorX: Float,
    public val CursorY: Float,
    public val Button: MouseButton,
    public val Clicks: Int,
)

public class MouseWheelScrollEvent(
    public val IsCursorLocked: Boolean,
    public val CursorX: Float,
    public val CursorY: Float,
    public val ScrollX: Float,
    public val ScrollY: Float,
    public val ScrollIntegerX: Int,
    public val ScrollIntegerY: Int,
)

public class KeyActionEvent(
    public val Key: Key
)

public enum class MouseButton {
    Left,
    Right,
    Middle,
    X1,
    X2;

    @OptIn(ExperimentalForeignApi::class)
    internal fun ToNativeValue(): Int {
        return when (this) {
            Left -> SDL_BUTTON_LEFT
            Right -> SDL_BUTTON_RIGHT
            Middle -> SDL_BUTTON_MIDDLE
            X1 -> SDL_BUTTON_X1
            X2 -> SDL_BUTTON_X2
        }
    }

    public companion object {
        @OptIn(ExperimentalForeignApi::class)
        internal fun FromNativeValue(value: UByte): MouseButton? {
            return when (value.toInt()) {
                SDL_BUTTON_LEFT -> Left
                SDL_BUTTON_RIGHT -> Right
                SDL_BUTTON_MIDDLE -> Middle
                SDL_BUTTON_X1 -> X1
                SDL_BUTTON_X2 -> X2
                else -> null
            }
        }
    }
}

public enum class Key {
    Letter_A,
    Letter_B,
    Letter_C,
    Letter_D,
    Letter_E,
    Letter_F,
    Letter_G,
    Letter_H,
    Letter_I,
    Letter_J,
    Letter_K,
    Letter_L,
    Letter_M,
    Letter_N,
    Letter_O,
    Letter_P,
    Letter_Q,
    Letter_R,
    Letter_S,
    Letter_T,
    Letter_U,
    Letter_V,
    Letter_W,
    Letter_X,
    Letter_Y,
    Letter_Z,

    Digit_0,
    Digit_1,
    Digit_2,
    Digit_3,
    Digit_4,
    Digit_5,
    Digit_6,
    Digit_7,
    Digit_8,
    Digit_9,

    Up,
    Down,
    Left,
    Right,

    Space,
    Backspace,
    Delete,
    Enter,
    Tab,
    Escape,

    LeftAlt,
    LeftControl,
    LeftShift,

    RightAlt,
    RightControl,
    RightShift,

    CapsLock,
    NumLock,

    Keypad_0,
    Keypad_1,
    Keypad_2,
    Keypad_3,
    Keypad_4,
    Keypad_5,
    Keypad_6,
    Keypad_7,
    Keypad_8,
    Keypad_9,
    Keypad_Decimal,
    Keypad_Plus,
    Keypad_Minus,
    Keypad_Multiply,
    Keypad_Divide,
    Keypad_Enter,

    F1,
    F2,
    F3,
    F4,
    F5,
    F6,
    F7,
    F8,
    F9,
    F10,
    F11,
    F12;

    @OptIn(ExperimentalForeignApi::class)
    internal fun ToNativeValue(): UInt {
        return when (this) {
            Letter_A -> SDL_SCANCODE_A
            Letter_B -> SDL_SCANCODE_B
            Letter_C -> SDL_SCANCODE_C
            Letter_D -> SDL_SCANCODE_D
            Letter_E -> SDL_SCANCODE_E
            Letter_F -> SDL_SCANCODE_F
            Letter_G -> SDL_SCANCODE_G
            Letter_H -> SDL_SCANCODE_H
            Letter_I -> SDL_SCANCODE_I
            Letter_J -> SDL_SCANCODE_J
            Letter_K -> SDL_SCANCODE_K
            Letter_L -> SDL_SCANCODE_L
            Letter_M -> SDL_SCANCODE_M
            Letter_N -> SDL_SCANCODE_N
            Letter_O -> SDL_SCANCODE_O
            Letter_P -> SDL_SCANCODE_P
            Letter_Q -> SDL_SCANCODE_Q
            Letter_R -> SDL_SCANCODE_R
            Letter_S -> SDL_SCANCODE_S
            Letter_T -> SDL_SCANCODE_T
            Letter_U -> SDL_SCANCODE_U
            Letter_V -> SDL_SCANCODE_V
            Letter_W -> SDL_SCANCODE_W
            Letter_X -> SDL_SCANCODE_X
            Letter_Y -> SDL_SCANCODE_Y
            Letter_Z -> SDL_SCANCODE_Z

            Digit_0 -> SDL_SCANCODE_0
            Digit_1 -> SDL_SCANCODE_1
            Digit_2 -> SDL_SCANCODE_2
            Digit_3 -> SDL_SCANCODE_3
            Digit_4 -> SDL_SCANCODE_4
            Digit_5 -> SDL_SCANCODE_5
            Digit_6 -> SDL_SCANCODE_6
            Digit_7 -> SDL_SCANCODE_7
            Digit_8 -> SDL_SCANCODE_8
            Digit_9 -> SDL_SCANCODE_9

            Up -> SDL_SCANCODE_UP
            Down -> SDL_SCANCODE_DOWN
            Left -> SDL_SCANCODE_LEFT
            Right -> SDL_SCANCODE_RIGHT

            Space -> SDL_SCANCODE_SPACE
            Backspace -> SDL_SCANCODE_BACKSPACE
            Delete -> SDL_SCANCODE_DELETE
            Enter -> SDL_SCANCODE_RETURN
            Tab -> SDL_SCANCODE_TAB
            Escape -> SDL_SCANCODE_ESCAPE

            LeftAlt -> SDL_SCANCODE_LALT
            LeftControl -> SDL_SCANCODE_LCTRL
            LeftShift -> SDL_SCANCODE_LSHIFT

            RightAlt -> SDL_SCANCODE_RALT
            RightControl -> SDL_SCANCODE_RCTRL
            RightShift -> SDL_SCANCODE_RSHIFT

            CapsLock -> SDL_SCANCODE_CAPSLOCK
            NumLock -> SDL_SCANCODE_NUMLOCKCLEAR

            Keypad_0 -> SDL_SCANCODE_KP_0
            Keypad_1 -> SDL_SCANCODE_KP_1
            Keypad_2 -> SDL_SCANCODE_KP_2
            Keypad_3 -> SDL_SCANCODE_KP_3
            Keypad_4 -> SDL_SCANCODE_KP_4
            Keypad_5 -> SDL_SCANCODE_KP_5
            Keypad_6 -> SDL_SCANCODE_KP_6
            Keypad_7 -> SDL_SCANCODE_KP_7
            Keypad_8 -> SDL_SCANCODE_KP_8
            Keypad_9 -> SDL_SCANCODE_KP_9
            Keypad_Decimal -> SDL_SCANCODE_KP_DECIMAL
            Keypad_Plus -> SDL_SCANCODE_KP_PLUS
            Keypad_Minus -> SDL_SCANCODE_KP_MINUS
            Keypad_Multiply -> SDL_SCANCODE_KP_MULTIPLY
            Keypad_Divide -> SDL_SCANCODE_KP_DIVIDE
            Keypad_Enter -> SDL_SCANCODE_KP_ENTER

            F1 -> SDL_SCANCODE_F1
            F2 -> SDL_SCANCODE_F2
            F3 -> SDL_SCANCODE_F3
            F4 -> SDL_SCANCODE_F4
            F5 -> SDL_SCANCODE_F5
            F6 -> SDL_SCANCODE_F6
            F7 -> SDL_SCANCODE_F7
            F8 -> SDL_SCANCODE_F8
            F9 -> SDL_SCANCODE_F9
            F10 -> SDL_SCANCODE_F10
            F11 -> SDL_SCANCODE_F11
            F12 -> SDL_SCANCODE_F12
        }
    }

    public companion object {
        @OptIn(ExperimentalForeignApi::class)
        internal fun FromNativeValue(value: SDL_Scancode): Key? {
            return when (value) {
                SDL_SCANCODE_A -> Letter_A
                SDL_SCANCODE_B -> Letter_B
                SDL_SCANCODE_C -> Letter_C
                SDL_SCANCODE_D -> Letter_D
                SDL_SCANCODE_E -> Letter_E
                SDL_SCANCODE_F -> Letter_F
                SDL_SCANCODE_G -> Letter_G
                SDL_SCANCODE_H -> Letter_H
                SDL_SCANCODE_I -> Letter_I
                SDL_SCANCODE_J -> Letter_J
                SDL_SCANCODE_K -> Letter_K
                SDL_SCANCODE_L -> Letter_L
                SDL_SCANCODE_M -> Letter_M
                SDL_SCANCODE_N -> Letter_N
                SDL_SCANCODE_O -> Letter_O
                SDL_SCANCODE_P -> Letter_P
                SDL_SCANCODE_Q -> Letter_Q
                SDL_SCANCODE_R -> Letter_R
                SDL_SCANCODE_S -> Letter_S
                SDL_SCANCODE_T -> Letter_T
                SDL_SCANCODE_U -> Letter_U
                SDL_SCANCODE_V -> Letter_V
                SDL_SCANCODE_W -> Letter_W
                SDL_SCANCODE_X -> Letter_X
                SDL_SCANCODE_Y -> Letter_Y
                SDL_SCANCODE_Z -> Letter_Z

                SDL_SCANCODE_0 -> Digit_0
                SDL_SCANCODE_1 -> Digit_1
                SDL_SCANCODE_2 -> Digit_2
                SDL_SCANCODE_3 -> Digit_3
                SDL_SCANCODE_4 -> Digit_4
                SDL_SCANCODE_5 -> Digit_5
                SDL_SCANCODE_6 -> Digit_6
                SDL_SCANCODE_7 -> Digit_7
                SDL_SCANCODE_8 -> Digit_8
                SDL_SCANCODE_9 -> Digit_9

                SDL_SCANCODE_UP -> Up
                SDL_SCANCODE_DOWN -> Down
                SDL_SCANCODE_LEFT -> Left
                SDL_SCANCODE_RIGHT -> Right

                SDL_SCANCODE_SPACE -> Space
                SDL_SCANCODE_BACKSPACE -> Backspace
                SDL_SCANCODE_DELETE -> Delete
                SDL_SCANCODE_RETURN -> Enter
                SDL_SCANCODE_TAB -> Tab
                SDL_SCANCODE_ESCAPE -> Escape

                SDL_SCANCODE_LALT -> LeftAlt
                SDL_SCANCODE_LCTRL -> LeftControl
                SDL_SCANCODE_LSHIFT -> LeftShift

                SDL_SCANCODE_RALT -> RightAlt
                SDL_SCANCODE_RCTRL -> RightControl
                SDL_SCANCODE_RSHIFT -> RightShift

                SDL_SCANCODE_CAPSLOCK -> CapsLock
                SDL_SCANCODE_NUMLOCKCLEAR -> NumLock

                SDL_SCANCODE_KP_0 -> Keypad_0
                SDL_SCANCODE_KP_1 -> Keypad_1
                SDL_SCANCODE_KP_2 -> Keypad_2
                SDL_SCANCODE_KP_3 -> Keypad_3
                SDL_SCANCODE_KP_4 -> Keypad_4
                SDL_SCANCODE_KP_5 -> Keypad_5
                SDL_SCANCODE_KP_6 -> Keypad_6
                SDL_SCANCODE_KP_7 -> Keypad_7
                SDL_SCANCODE_KP_8 -> Keypad_8
                SDL_SCANCODE_KP_9 -> Keypad_9
                SDL_SCANCODE_KP_DECIMAL -> Keypad_Decimal
                SDL_SCANCODE_KP_PLUS -> Keypad_Plus
                SDL_SCANCODE_KP_MINUS -> Keypad_Minus
                SDL_SCANCODE_KP_MULTIPLY -> Keypad_Multiply
                SDL_SCANCODE_KP_DIVIDE -> Keypad_Divide
                SDL_SCANCODE_KP_ENTER -> Keypad_Enter

                SDL_SCANCODE_F1 -> F1
                SDL_SCANCODE_F2 -> F2
                SDL_SCANCODE_F3 -> F3
                SDL_SCANCODE_F4 -> F4
                SDL_SCANCODE_F5 -> F5
                SDL_SCANCODE_F6 -> F6
                SDL_SCANCODE_F7 -> F7
                SDL_SCANCODE_F8 -> F8
                SDL_SCANCODE_F9 -> F9
                SDL_SCANCODE_F10 -> F10
                SDL_SCANCODE_F11 -> F11
                SDL_SCANCODE_F12 -> F12

                else -> null
            }
        }
    }
}
