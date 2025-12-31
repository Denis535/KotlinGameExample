package com.denis535.game_engine_pro.windows

import cnames.structs.*
import com.denis535.game_engine_pro.*
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

    public val Cursor: Cursor

    public val Mouse: Mouse

    public val Keyboard: Keyboard

    @OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
    public constructor(desc: MainWindowDesc) {
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
        this.Cursor = Cursor(this)
        this.Mouse = Mouse(this)
        this.Keyboard = Keyboard(this)
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        this.Keyboard.close()
        this.Mouse.close()
        this.Cursor.close()
        this._NativeWindow = run {
            SDL_DestroyWindow(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            null
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun Show() {
        check(!this.IsClosed)
        SDL_ShowWindow(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
        SDL_RaiseWindow(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
        this.OnShow()
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun Hide() {
        check(!this.IsClosed)
        this.OnHide()
        SDL_HideWindow(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    internal fun ProcessEvent(event: CPointer<SDL_Event>) {
        check(!this.IsClosed)
        check(event.pointed.window.windowID == SDL_GetWindowID(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() })
        when (event.pointed.type) {
            SDL_EVENT_MOUSE_MOTION -> {
                val motionEvent = event.pointed.motion
                val cursorX = motionEvent.x
                val cursorY = motionEvent.y
                val cursorDeltaX = motionEvent.xrel
                val cursorDeltaY = motionEvent.yrel
                this.OnMouseCursorMove(MouseCursorMoveEvent(cursorX, cursorY, cursorDeltaX, cursorDeltaY))
            }
            SDL_EVENT_MOUSE_BUTTON_DOWN, SDL_EVENT_MOUSE_BUTTON_UP -> {
                val buttonEvent = event.pointed.button
                val cursorX = buttonEvent.x
                val cursorY = buttonEvent.y
                val isButtonPressed = buttonEvent.down
                val button = MouseButton.FromNativeValue(buttonEvent.button)
                val clicks = buttonEvent.clicks.toInt()
                if (button != null) {
                    if (isButtonPressed) {
                        this.OnMouseButtonPress(MouseButtonActionEvent(cursorX, cursorY, button, clicks))
                    } else {
                        this.OnMouseButtonRelease(MouseButtonActionEvent(cursorX, cursorY, button, clicks))
                    }
                }
            }
            SDL_EVENT_MOUSE_WHEEL -> {
                val wheelEvent = event.pointed.wheel
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
                this.OnMouseWheelScroll(MouseWheelScrollEvent(cursorX, cursorY, scrollX, scrollY, scrollIntegerX, scrollIntegerY))
            }
            SDL_EVENT_KEY_DOWN, SDL_EVENT_KEY_UP -> {
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
            SDL_EVENT_TEXT_INPUT -> {
                val textEvent = event.pointed.text
                val text = textEvent.text?.toKStringFromUtf8()
                if (text != null) {
                    this.OnTextInput(text)
                }
            }
        }
    }

    protected abstract fun OnShow()
    protected abstract fun OnHide()

    protected abstract fun OnDraw(info: FrameInfo)

    internal fun OnDrawInternal(info: FrameInfo) {
        this.OnDraw(info)
    }

    protected abstract fun OnMouseCursorMove(event: MouseCursorMoveEvent)
    protected abstract fun OnMouseButtonPress(event: MouseButtonActionEvent)
    protected abstract fun OnMouseButtonRelease(event: MouseButtonActionEvent)
    protected abstract fun OnMouseWheelScroll(event: MouseWheelScrollEvent)

    protected abstract fun OnKeyPress(event: KeyActionEvent)
    protected abstract fun OnKeyRepeat(event: KeyActionEvent)
    protected abstract fun OnKeyRelease(event: KeyActionEvent)

    protected abstract fun OnTextInput(text: String)

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

}

public class MouseCursorMoveEvent(
    public val CursorX: Float,
    public val CursorY: Float,
    public val CursorDeltaX: Float,
    public val CursorDeltaY: Float,
)

public class MouseButtonActionEvent(
    public val CursorX: Float,
    public val CursorY: Float,
    public val Button: MouseButton,
    public val Clicks: Int,
)

public class MouseWheelScrollEvent(
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
