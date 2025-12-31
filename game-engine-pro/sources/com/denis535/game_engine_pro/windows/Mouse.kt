package com.denis535.game_engine_pro.windows

import cnames.structs.*
import com.denis535.sdl.*
import kotlinx.cinterop.*

public class Mouse : AutoCloseable {

    private val Window: MainWindow

    @OptIn(ExperimentalForeignApi::class)
    private val NativeWindow: CPointer<SDL_Window>
        get() {
            return this.Window.NativeWindowInternal
        }

    internal constructor(window: MainWindow) {
        this.Window = window
    }

    public override fun close() {
    }

    //    public abstract fun GetMouseCursorPosition(): Pair<Double, Double>
    //    public abstract fun GetMouseButtonPressed(button: MouseButton): Boolean

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
