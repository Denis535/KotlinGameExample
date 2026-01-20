package com.denis535.kotlin_game_example

import com.denis535.game_engine_pro.*
import com.denis535.game_engine_pro.input.*
import com.denis535.game_engine_pro.windows.*
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
        this.Engine.let { engine ->
            if (clazz.isInstance(engine)) {
                return engine
            }
        }
        this.Engine.Window.let { window ->
            if (clazz.isInstance(window)) {
                return window
            }
        }
        this.Engine.Cursor.let { cursor ->
            if (clazz.isInstance(cursor)) {
                return cursor
            }
        }
        this.Engine.Mouse.let { mouse ->
            if (clazz.isInstance(mouse)) {
                return mouse
            }
        }
        this.Engine.Keyboard.let { keyboard ->
            if (clazz.isInstance(keyboard)) {
                return keyboard
            }
        }
        return null
    }

}

private class ClientEngine2 : ClientEngine {

    private val Program: Program

    public constructor(program: Program) : super(Manifest("Kotlin Game Example"), { MainWindow2() }) {
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

    protected override fun OnMouseFocus(event: MouseFocusEvent) {
    }

    protected override fun OnMouseFocusLost(event: MouseFocusLostEvent) {
    }

    protected override fun OnKeyboardFocus(event: KeyboardFocusEvent) {
    }

    protected override fun OnKeyboardFocusLost(event: KeyboardFocusLostEvent) {
    }

    protected override fun OnMouseMove(event: MouseMoveEvent) {
    }

    protected override fun OnMouseButtonPress(event: MouseButtonEvent) {
    }

    protected override fun OnMouseButtonRelease(event: MouseButtonEvent) {
    }

    protected override fun OnMouseWheelScroll(event: MouseWheelScrollEvent) {
    }

    protected override fun OnKeyboardKeyPress(event: KeyboardKeyEvent) {
        if (event.Key == KeyboardKey.Enter && this.Keyboard.IsKeyPressed(KeyboardKey.RightAlt)) {
            this.Window.IsFullScreen = !this.Window.IsFullScreen
        }
        if (event.Key == KeyboardKey.F1) {
            this.Window.IsResizable = !this.Window.IsResizable
        }
        if (event.Key == KeyboardKey.F2) {
            this.Window.IsMouseGrabbed = !this.Window.IsMouseGrabbed
        }
        if (event.Key == KeyboardKey.F3) {
            this.Window.IsMouseCaptured = !this.Window.IsMouseCaptured
        }
        if (event.Key == KeyboardKey.F4) {
            this.Window.IsMouseLocked = !this.Window.IsMouseLocked
        }

        if (event.Key == KeyboardKey.F3) {
            this.Cursor.IsVisible = !this.Cursor.IsVisible
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

    protected override fun OnKeyboardKeyRepeat(event: KeyboardKeyEvent) {
    }

    protected override fun OnKeyboardKeyRelease(event: KeyboardKeyEvent) {
    }

    protected override fun OnTextInput(event: TextInputEvent) {
    }

}

private class MainWindow2 : MainWindow {

    public constructor() : super(Description.Window("Kotlin Game Example")) {
        this.Raise()
    }

    public override fun close() {
        super.close()
    }

}
