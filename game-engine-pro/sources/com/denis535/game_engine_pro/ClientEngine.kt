package com.denis535.game_engine_pro

import com.denis535.game_engine_pro.windows.*
import com.denis535.sdl.*
import kotlinx.cinterop.*

public abstract class ClientEngine : Engine {

    public val Mouse: Mouse
        get() {
            check(!this.IsClosed)
            return field
        }

    public val Keyboard: Keyboard
        get() {
            check(!this.IsClosed)
            return field
        }

    public var Window: MainWindow? = null
        get() {
            check(!this.IsClosed)
            return field
        }
        protected set(value) {
            check(!this.IsClosed)
            field = value
        }

    @OptIn(ExperimentalForeignApi::class)
    public constructor(manifest: Manifest) : super() {
        SDL_Init(SDL_INIT_VIDEO).also { Sdl.ThrowErrorIfNeeded() }
        SDL_SetAppMetadata(manifest.Name, manifest.Version, manifest.Id).also { Sdl.ThrowErrorIfNeeded() }
        SDL_SetAppMetadataProperty(SDL_PROP_APP_METADATA_CREATOR_STRING, manifest.Creator).also { Sdl.ThrowErrorIfNeeded() }
        this.Mouse = Mouse()
        this.Keyboard = Keyboard()
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        check(!this.IsRunning)
        this.Keyboard.close()
        this.Mouse.close()
        SDL_Quit().also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    internal override fun ProcessEvent(event: CPointer<SDL_Event>): Boolean {
        if (super.ProcessEvent(event)) {
            return true
        }
        this.Window!!.let { window ->
            if (event.pointed.window.windowID == SDL_GetWindowID(window.NativeWindowInternal).also { Sdl.ThrowErrorIfNeeded() }) {
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
                        val key = KeyboardKey.FromNativeValue(keyEvent.scancode)
                        if (key != null) {
                            if (isKeyPressed) {
                                if (!isKeyRepeated) {
                                    this.OnKeyboardKeyPress(KeyboardKeyActionEvent(key))
                                } else {
                                    this.OnKeyboardKeyRepeat(KeyboardKeyActionEvent(key))
                                }
                            } else {
                                this.OnKeyboardKeyRelease(KeyboardKeyActionEvent(key))
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
                    SDL_EVENT_WINDOW_CLOSE_REQUESTED -> {
                        return true
                    }
                }
            }
        }
        return false
    }

    @OptIn(ExperimentalForeignApi::class)
    internal override fun ProcessFrame(info: FrameInfo, fixedDeltaTime: Float) {
        super.ProcessFrame(info, fixedDeltaTime)
        this.OnDraw(info)
    }

    protected abstract fun OnDraw(info: FrameInfo)

    protected abstract fun OnMouseCursorMove(event: MouseCursorMoveEvent)
    protected abstract fun OnMouseButtonPress(event: MouseButtonActionEvent)
    protected abstract fun OnMouseButtonRelease(event: MouseButtonActionEvent)
    protected abstract fun OnMouseWheelScroll(event: MouseWheelScrollEvent)

    protected abstract fun OnKeyboardKeyPress(event: KeyboardKeyActionEvent)
    protected abstract fun OnKeyboardKeyRepeat(event: KeyboardKeyActionEvent)
    protected abstract fun OnKeyboardKeyRelease(event: KeyboardKeyActionEvent)

    protected abstract fun OnTextInput(text: String)

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

public class KeyboardKeyActionEvent(
    public val Key: KeyboardKey
)
