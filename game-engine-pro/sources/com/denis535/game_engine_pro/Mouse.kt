package com.denis535.game_engine_pro

import com.denis535.sdl.*
import kotlinx.cinterop.*

public class Mouse : AutoCloseable {

    internal constructor() {
    }

    public override fun close() {
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun GetCursorPosition(): Pair<Float, Float> {
        memScoped {
            val cursorX = this.alloc<FloatVar>()
            val cursorY = this.alloc<FloatVar>()
            SDL_GetMouseState(cursorX.ptr, cursorY.ptr).also { SDL.ThrowErrorIfNeeded() }
            return Pair(cursorX.value, cursorY.value)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun GetCursorDelta(): Pair<Float, Float> {
        memScoped {
            val cursorDeltaX = this.alloc<FloatVar>()
            val cursorDeltaY = this.alloc<FloatVar>()
            SDL_GetRelativeMouseState(cursorDeltaX.ptr, cursorDeltaY.ptr).also { SDL.ThrowErrorIfNeeded() }
            return Pair(cursorDeltaX.value, cursorDeltaY.value)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun IsButtonPressed(button: MouseButton): Boolean {
        val state = SDL_GetMouseState(null, null).also { SDL.ThrowErrorIfNeeded() }
        return state and button.ToNativeMask() != 0u
    }

}

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

    @OptIn(ExperimentalForeignApi::class)
    internal fun ToNativeMask(): UInt {
        return when (this) {
            Left -> SDL_BUTTON_LMASK
            Right -> SDL_BUTTON_RMASK
            Middle -> SDL_BUTTON_MMASK
            X1 -> SDL_BUTTON_X1MASK
            X2 -> SDL_BUTTON_X2MASK
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
