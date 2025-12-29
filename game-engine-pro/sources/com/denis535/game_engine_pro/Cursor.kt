package com.denis535.game_engine_pro

import cnames.structs.*
import com.denis535.sdl.*
import kotlinx.cinterop.*

public class Cursor : AutoCloseable {

    private val Window: MainWindow

    @OptIn(ExperimentalForeignApi::class)
    private val NativeWindow: CPointer<SDL_Window>
        get() {
            return this.Window.NativeWindowInternal
        }

    @OptIn(ExperimentalForeignApi::class)
    private var NativeCursor: CPointer<SDL_Cursor>? = null

    @OptIn(ExperimentalForeignApi::class)
    public var Style: CursorStyle?
        get() {
            error("Not implemented")
        }
        set(value) {
            val prevNativeCursor = this.NativeCursor
            this.NativeCursor = if (value != null) {
                SDL_CreateSystemCursor(value.ToNativeValue()).also { Sdl.ThrowErrorIfNeeded() }
            } else {
                null
            }
            SDL_SetCursor(this.NativeCursor).also { Sdl.ThrowErrorIfNeeded() }
            if (prevNativeCursor != null) {
                SDL_DestroyCursor(prevNativeCursor).also { Sdl.ThrowErrorIfNeeded() }
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsVisible: Boolean
        get() {
            return SDL_CursorVisible().also { Sdl.ThrowErrorIfNeeded() }
        }
        set(value) {
            if (value) {
                SDL_ShowCursor().also { Sdl.ThrowErrorIfNeeded() }
            } else {
                SDL_HideCursor().also { Sdl.ThrowErrorIfNeeded() }
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsGrabbed: Boolean
        get() {
            return SDL_GetWindowMouseGrab(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
        }
        set(value) {
            SDL_SetWindowMouseGrab(this.NativeWindow, value).also { Sdl.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsCaptured: Boolean
        get() {
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_MOUSE_CAPTURE != 0uL
        }
        set(value) {
            SDL_CaptureMouse(value).also { Sdl.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsLocked: Boolean
        get() {
            return SDL_GetWindowRelativeMouseMode(this.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
        }
        set(value) {
            SDL_SetWindowRelativeMouseMode(this.NativeWindow, value).also { Sdl.ThrowErrorIfNeeded() }
        }

    internal constructor(window: MainWindow) {
        this.Window = window
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        if (this.NativeCursor != null) {
            SDL_DestroyCursor(this.NativeCursor).also { Sdl.ThrowErrorIfNeeded() }
        }
    }

}

public enum class CursorStyle {
    Arrow,
    Text,
    Pointer,
    Crosshair,
    Progress,
    Wait,
    NotAllowed,

    Move,

    SingleArrowResize_N,
    SingleArrowResize_S,
    SingleArrowResize_W,
    SingleArrowResize_E,

    SingleArrowResize_N_W,
    SingleArrowResize_N_E,
    SingleArrowResize_S_W,
    SingleArrowResize_S_E,

    DoubleArrowResize_N_S,
    DoubleArrowResize_W_E,

    DoubleArrowResize_NW_SE,
    DoubleArrowResize_NE_SW;

    @OptIn(ExperimentalForeignApi::class)
    internal fun ToNativeValue(): SDL_SystemCursor {
        return when (this) {
            Arrow -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_DEFAULT
            Text -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_TEXT
            Pointer -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_POINTER
            Crosshair -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_CROSSHAIR
            Progress -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_PROGRESS
            Wait -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_WAIT
            NotAllowed -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NOT_ALLOWED

            Move -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_MOVE

            SingleArrowResize_N -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_N_RESIZE
            SingleArrowResize_S -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_S_RESIZE
            SingleArrowResize_W -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_W_RESIZE
            SingleArrowResize_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_E_RESIZE

            SingleArrowResize_N_W -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NW_RESIZE
            SingleArrowResize_N_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NE_RESIZE
            SingleArrowResize_S_W -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_SW_RESIZE
            SingleArrowResize_S_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_SE_RESIZE

            DoubleArrowResize_N_S -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NS_RESIZE
            DoubleArrowResize_W_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_EW_RESIZE

            DoubleArrowResize_NW_SE -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NWSE_RESIZE
            DoubleArrowResize_NE_SW -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NESW_RESIZE
        }
    }

    public companion object {
        @OptIn(ExperimentalForeignApi::class)
        internal fun FromNativeValue(value: SDL_SystemCursor): CursorStyle? {
            return when (value) {
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_DEFAULT -> Arrow
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_TEXT -> Text
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_POINTER -> Pointer
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_CROSSHAIR -> Crosshair
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_PROGRESS -> Progress
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_WAIT -> Wait
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_NOT_ALLOWED -> NotAllowed

                SDL_SystemCursor.SDL_SYSTEM_CURSOR_MOVE -> Move

                SDL_SystemCursor.SDL_SYSTEM_CURSOR_N_RESIZE -> SingleArrowResize_N
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_S_RESIZE -> SingleArrowResize_S
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_W_RESIZE -> SingleArrowResize_W
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_E_RESIZE -> SingleArrowResize_E

                SDL_SystemCursor.SDL_SYSTEM_CURSOR_NW_RESIZE -> SingleArrowResize_N_W
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_NE_RESIZE -> SingleArrowResize_N_E
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_SW_RESIZE -> SingleArrowResize_S_W
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_SE_RESIZE -> SingleArrowResize_S_E

                SDL_SystemCursor.SDL_SYSTEM_CURSOR_NS_RESIZE -> DoubleArrowResize_N_S
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_EW_RESIZE -> DoubleArrowResize_W_E

                SDL_SystemCursor.SDL_SYSTEM_CURSOR_NWSE_RESIZE -> DoubleArrowResize_NW_SE
                SDL_SystemCursor.SDL_SYSTEM_CURSOR_NESW_RESIZE -> DoubleArrowResize_NE_SW

                else -> null
            }
        }
    }
}
