//package com.denis535.game_engine_pro
//
//import cnames.structs.*
//import com.denis535.sdl.*
//import kotlinx.cinterop.*
//
//public class Cursor(public val Window: MainWindow) {
//
//    @OptIn(ExperimentalForeignApi::class)
//    private var _SDL_Cursor: CPointer<SDL_Cursor>? = null
//
//    @OptIn(ExperimentalForeignApi::class)
//    public var Style: CursorStyle
//        get() {
//            error("Not implemented")
//        }
//        set(value) {
//            if (this._SDL_Cursor != null) {
//                SDL_DestroyCursor(this._SDL_Cursor).also { Sdl.ThrowErrorIfNeeded() }
//                this._SDL_Cursor = null
//            }
//            this._SDL_Cursor = SDL_CreateSystemCursor(value.ToNativeValue()).also { Sdl.ThrowErrorIfNeeded() }
//            SDL_SetCursor(this._SDL_Cursor).also { Sdl.ThrowErrorIfNeeded() }
//        }
//
//    @OptIn(ExperimentalForeignApi::class)
//    public var IsCursorVisible: Boolean
//        get() {
//            return SDL_CursorVisible().also { Sdl.ThrowErrorIfNeeded() }
//        }
//        set(value) {
//            if (value) {
//                SDL_ShowCursor().also { Sdl.ThrowErrorIfNeeded() }
//            } else {
//                SDL_HideCursor().also { Sdl.ThrowErrorIfNeeded() }
//            }
//        }
//
//    @OptIn(ExperimentalForeignApi::class)
//    public var IsCursorGrabbed: Boolean
//        get() {
//            return SDL_GetWindowMouseGrab(this.Window.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
//        }
//        set(value) {
//            SDL_SetWindowMouseGrab(this.Window.NativeWindow, value).also { Sdl.ThrowErrorIfNeeded() }
//        }
//
//    @OptIn(ExperimentalForeignApi::class)
//    public var IsCursorCaptured: Boolean
//        get() {
//            val flags = SDL_GetWindowFlags(this.Window.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
//            return flags and SDL_WINDOW_MOUSE_CAPTURE != 0uL
//        }
//        set(value) {
//            SDL_CaptureMouse(value).also { Sdl.ThrowErrorIfNeeded() }
//        }
//
//    @OptIn(ExperimentalForeignApi::class)
//    public var IsCursorLocked: Boolean
//        get() {
//            return SDL_GetWindowRelativeMouseMode(this.Window.NativeWindow).also { Sdl.ThrowErrorIfNeeded() }
//        }
//        set(value) {
//            SDL_SetWindowRelativeMouseMode(this.Window.NativeWindow, value).also { Sdl.ThrowErrorIfNeeded() }
//        }
//
//}
//
//public enum class CursorStyle {
//    Arrow,
//    Text,
//    Pointer,
//    Crosshair,
//    Progress,
//    NotAllowed,
//    Move,
//    Wait,
//
//    SingleArrowResize_N,
//    SingleArrowResize_S,
//    SingleArrowResize_W,
//    SingleArrowResize_E,
//
//    SingleArrowResize_N_W,
//    SingleArrowResize_N_E,
//    SingleArrowResize_S_W,
//    SingleArrowResize_S_E,
//
//    DoubleArrowResize_N_S,
//    DoubleArrowResize_W_E,
//
//    DoubleArrowResize_NW_SE,
//    DoubleArrowResize_NE_SW,
//}
//
//@OptIn(ExperimentalForeignApi::class)
//private fun SDL_SystemCursor.ToCursorStyle(): CursorStyle? {
//    return when (this) {
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_DEFAULT -> CursorStyle.Arrow
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_TEXT -> CursorStyle.Text
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_POINTER -> CursorStyle.Pointer
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_CROSSHAIR -> CursorStyle.Crosshair
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_PROGRESS -> CursorStyle.Progress
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NOT_ALLOWED -> CursorStyle.NotAllowed
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_MOVE -> CursorStyle.Move
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_WAIT -> CursorStyle.Wait
//
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_N_RESIZE -> CursorStyle.SingleArrowResize_N
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_S_RESIZE -> CursorStyle.SingleArrowResize_S
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_W_RESIZE -> CursorStyle.SingleArrowResize_W
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_E_RESIZE -> CursorStyle.SingleArrowResize_E
//
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NW_RESIZE -> CursorStyle.SingleArrowResize_N_W
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NE_RESIZE -> CursorStyle.SingleArrowResize_N_E
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_SW_RESIZE -> CursorStyle.SingleArrowResize_S_W
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_SE_RESIZE -> CursorStyle.SingleArrowResize_S_E
//
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NS_RESIZE -> CursorStyle.DoubleArrowResize_N_S
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_EW_RESIZE -> CursorStyle.DoubleArrowResize_W_E
//
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NWSE_RESIZE -> CursorStyle.DoubleArrowResize_NW_SE
//        SDL_SystemCursor.SDL_SYSTEM_CURSOR_NESW_RESIZE -> CursorStyle.DoubleArrowResize_NE_SW
//
//        else -> null
//    }
//}
//
//@OptIn(ExperimentalForeignApi::class)
//private fun CursorStyle.ToNativeValue(): SDL_SystemCursor {
//    return when (this) {
//        CursorStyle.Arrow -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_DEFAULT
//        CursorStyle.Text -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_TEXT
//        CursorStyle.Pointer -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_POINTER
//        CursorStyle.Crosshair -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_CROSSHAIR
//        CursorStyle.Progress -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_PROGRESS
//        CursorStyle.NotAllowed -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NOT_ALLOWED
//        CursorStyle.Move -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_MOVE
//        CursorStyle.Wait -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_WAIT
//
//        CursorStyle.SingleArrowResize_N -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_N_RESIZE
//        CursorStyle.SingleArrowResize_S -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_S_RESIZE
//        CursorStyle.SingleArrowResize_W -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_W_RESIZE
//        CursorStyle.SingleArrowResize_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_E_RESIZE
//
//        CursorStyle.SingleArrowResize_N_W -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NW_RESIZE
//        CursorStyle.SingleArrowResize_N_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NE_RESIZE
//        CursorStyle.SingleArrowResize_S_W -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_SW_RESIZE
//        CursorStyle.SingleArrowResize_S_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_SE_RESIZE
//
//        CursorStyle.DoubleArrowResize_N_S -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NS_RESIZE
//        CursorStyle.DoubleArrowResize_W_E -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_EW_RESIZE
//
//        CursorStyle.DoubleArrowResize_NW_SE -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NWSE_RESIZE
//        CursorStyle.DoubleArrowResize_NE_SW -> SDL_SystemCursor.SDL_SYSTEM_CURSOR_NESW_RESIZE
//    }
//}
