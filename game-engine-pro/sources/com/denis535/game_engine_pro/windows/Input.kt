package com.denis535.game_engine_pro.windows

import cnames.structs.*
import com.denis535.sdl.*
import kotlinx.cinterop.*

public class Input : AutoCloseable {

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
    //
    //    public abstract fun GetKeyPressed(key: Key): Boolean

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
    internal fun ToNativeValue(): SDL_Scancode {
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
