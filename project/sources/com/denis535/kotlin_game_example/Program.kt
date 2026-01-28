package com.denis535.kotlin_game_example

import com.denis535.game_engine_pro.*
import com.denis535.game_engine_pro.display.*
import com.denis535.game_engine_pro.input.*
import com.denis535.game_framework_pro.*
import kotlinx.cinterop.*
import kotlin.reflect.*

public fun Main(args: Array<String>) {
    Program().use {
//        it.RequireDependency<AbstractProgram>(AbstractProgram::class)
//        it.RequireDependency<Program>(Program::class)
//
//        it.RequireDependency<AbstractTheme>(AbstractTheme::class)
//        it.RequireDependency<Theme>(Theme::class)
//
//        it.RequireDependency<AbstractScreen>(AbstractScreen::class)
//        it.RequireDependency<Screen>(Screen::class)
//
//        it.RequireDependency<AbstractRouter>(AbstractRouter::class)
//        it.RequireDependency<Router>(Router::class)
//
//        it.RequireDependency<AbstractApplication>(AbstractApplication::class)
//        it.RequireDependency<Application>(Application::class)
//
//        it.RequireDependency<AbstractGame>(AbstractGame::class)
//        it.RequireDependency<Game>(Game::class)
    }
}

public class Program : AbstractProgram2<Theme, Screen, Router, Application> {

    private val Engine: ClientEngine2

    @OptIn(ExperimentalForeignApi::class)
    public constructor() {
        this.Engine = ClientEngine2(this)
        this.Application = Application()
        this.Router = Router()
        this.Screen = Screen()
        this.Theme = Theme()
        this.Engine.Run()
    }

    protected override fun OnClose() {
        check(!this.Engine.IsRunning)
        this.Theme!!.close()
        this.Screen!!.close()
        this.Router!!.close()
        this.Application!!.close()
        this.Engine.close()
        super.OnClose()
    }

    public override fun GetDependencyInternal(clazz: KClass<*>, argument: Any?): Any? {
        val result = super.GetDependencyInternal(clazz, argument)
        if (result != null) {
            return result
        }
        this.Application?.Game?.let { game ->
            if (clazz.isInstance(game)) {
                return game
            }
        }
        if (clazz == com.denis535.game_engine_pro.Engine::class) {
            return this.Engine
        }
        if (clazz == com.denis535.game_engine_pro.ClientEngine::class) {
            return this.Engine
        }
        if (clazz == ClientEngine2::class) {
            return this.Engine
        }
        if (clazz == Window::class) {
            return this.Engine.Window
        }
        if (clazz == Window2::class) {
            return this.Engine.Window
        }
        if (clazz == Cursor::class) {
            return this.Engine.Cursor
        }
        if (clazz == Touchscreen::class) {
            return this.Engine.Touchscreen
        }
        if (clazz == Mouse::class) {
            return this.Engine.Mouse
        }
        if (clazz == Keyboard::class) {
            return this.Engine.Keyboard
        }
        if (clazz == Gamepad::class) {
            return this.Engine.Gamepads.getOrNull(argument as Int)
        }
        return null
    }

}

private class ClientEngine2 : ClientEngine {

    private val Program: Program

    public constructor(program: Program) : super(Description("Kotlin Game Example"), { Window2() }) {
        this.Program = program
    }

    public override fun close() {
        super.close()
    }

    protected override fun OnStart() {
//        println("OnStart")
    }

    protected override fun OnStop() {
//        println("OnStop")
    }

    protected override fun OnDraw() {
//        println("OnDraw: " + this.Time.Time)
    }

    protected override fun OnUpdate() {
//        println("OnUpdate: " + this.Time.Time)
    }

    protected override fun OnFixedUpdate() {
//        println("OnFixedUpdate: " + this.Time.Time)
    }

    protected override fun OnFocus(event: MouseFocusEvent) {
    }

    protected override fun OnFocus(event: KeyboardFocusEvent) {
    }

    protected override fun OnTouch(event: TouchEvent) {
    }

    protected override fun OnZoom(event: ZoomEvent) {
    }

    protected override fun OnText(event: TextEvent) {
    }

    protected override fun OnMouseMove(event: MouseMoveEvent) {
    }

    protected override fun OnMouseButtonAction(event: MouseButtonActionEvent) {
    }

    protected override fun OnMouseWheelScroll(event: MouseWheelScrollEvent) {
    }

    protected override fun OnKeyboardKeyAction(event: KeyboardKeyActionEvent) {
        if (event.IsPressed) {
            if (event.Key == KeyboardKey.Enter && this.Keyboard.IsKeyPressed(KeyboardKey.RightAlt)) {
                this.Window.IsFullScreen = !this.Window.IsFullScreen
            }

            if (event.Key == KeyboardKey.F1) {
                this.Window.IsMouseLocked = !this.Window.IsMouseLocked
            }
            if (event.Key == KeyboardKey.F2) {
                this.Window.IsMouseGrabbed = !this.Window.IsMouseGrabbed
            }
            if (event.Key == KeyboardKey.F3) {
                this.Window.IsMouseCaptured = !this.Window.IsMouseCaptured
            }

            if (event.Key == KeyboardKey.Digit_0) {
                this.Cursor.Style = null
            }
            if (event.Key == KeyboardKey.Digit_1) {
                this.Cursor.Style = CursorStyle.Arrow
            }
            if (event.Key == KeyboardKey.Digit_2) {
                this.Cursor.Style = CursorStyle.Text
            }
            if (event.Key == KeyboardKey.Digit_3) {
                this.Cursor.Style = CursorStyle.Pointer
            }
            if (event.Key == KeyboardKey.Digit_4) {
                this.Cursor.Style = CursorStyle.Crosshair
            }
            if (event.Key == KeyboardKey.Digit_5) {
                this.Cursor.Style = CursorStyle.Progress
            }
            if (event.Key == KeyboardKey.Digit_6) {
                this.Cursor.Style = CursorStyle.Wait
            }
            if (event.Key == KeyboardKey.Digit_7) {
                this.Cursor.Style = CursorStyle.NotAllowed
            }

            if (event.Key == KeyboardKey.Keypad_0) {
                this.Cursor.Style = CursorStyle.Move
            }
            if (event.Key == KeyboardKey.Keypad_1) {
                this.Cursor.Style = CursorStyle.SingleArrowResize_N
            }
            if (event.Key == KeyboardKey.Keypad_2) {
                this.Cursor.Style = CursorStyle.SingleArrowResize_N_W
            }
            if (event.Key == KeyboardKey.Keypad_3) {
                this.Cursor.Style = CursorStyle.DoubleArrowResize_N_S
            }
            if (event.Key == KeyboardKey.Keypad_4) {
                this.Cursor.Style = CursorStyle.DoubleArrowResize_NW_SE
            }
        }
    }

    protected override fun OnGamepadButtonAction(event: GamepadButtonActionEvent) {
    }

    protected override fun OnGamepadAxisAction(event: GamepadAxisActionEvent) {
    }

}

private class Window2 : Window {

    public constructor() : super(Description.Window("Kotlin Game Example")) {
        this.Raise()
    }

    public override fun close() {
        super.close()
    }

}
