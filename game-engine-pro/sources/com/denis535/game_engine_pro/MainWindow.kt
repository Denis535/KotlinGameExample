package com.denis535.game_engine_pro

public abstract class MainWindow : AutoCloseable {

    public abstract val IsClosed: Boolean

    public abstract val Time: Double

    public abstract val IsFullScreen: Boolean

    public abstract var Title: String

    public abstract var Position: Pair<Int, Int>

    public abstract var Size: Pair<Int, Int>

    public abstract val IsVisible: Boolean

    public abstract val IsFocused: Boolean

    public abstract var IsTextInputEnabled: Boolean

    public abstract var IsCursorVisible: Boolean

    public abstract var IsCursorGrabbed: Boolean

    public abstract var IsCursorCaptured: Boolean

    public abstract var IsCursorLocked: Boolean

    public var IsRunning: Boolean = false
        private set(value) {
            check(!this.IsClosed)
            check(field != value)
            field = value
        }

    public constructor()

    public abstract fun Show()
    public abstract fun Hide()

    public abstract fun MakeFullScreen()
    public abstract fun MakeWindowed(width: Int = 1280, height: Int = 720, isResizable: Boolean = false)

    public abstract fun RequestClose()
    public abstract fun RequestQuit()

    public fun Run(fixedDeltaTime: Float = 1.0f / 20.0f) {
        check(!this.IsClosed)
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

    protected abstract fun OnStart()
    protected abstract fun OnStop()

    protected abstract fun ProcessEvents(info: FrameInfo): Boolean

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
    //    public abstract fun SetMouseCursorPosition(pos: Pair<Double, Double>)
    //    public abstract fun GetMouseButtonPressed(button: MouseButton): Boolean
    //
    //    public abstract fun GetKeyPressed(key: Key): Boolean

}

public sealed class MainWindowDesc(public val Title: String) {
    public class FullScreen(title: String) : MainWindowDesc(title)
    public class Window(title: String, public val Width: Int = 1280, public val Height: Int = 720, public val IsResizable: Boolean = false) : MainWindowDesc(title)
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
    public val Button: Button,
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

public enum class Button {
    Left,
    Right,
    Middle,
    X1,
    X2,
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
}
