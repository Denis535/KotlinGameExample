package com.denis535.kotlin_game_example

import com.denis535.game_engine_pro.*
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

    private val Engine: Engine

    @OptIn(ExperimentalForeignApi::class)
    public constructor() {
        this.Engine = Engine2(this)
        this.Engine.Window = MainWindow2(this)
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
        this.Engine.Window!!.close()
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
        this.Engine.Window?.let { window ->
            if (clazz.isInstance(window)) {
                return window
            }
        }
        this.Engine.Window?.Cursor?.let { cursor ->
            if (clazz.isInstance(cursor)) {
                return cursor
            }
        }
        return null
    }

}

private class Engine2 : Engine {

    private val Program: Program

    public constructor(program: Program) : super("Kotlin Game Example") {
        this.Program = program
    }

    public override fun close() {
        super.close()
    }

    protected override fun OnStart(info: FrameInfo) {
    }

    protected override fun OnStop(info: FrameInfo) {
    }

    protected override fun OnFixedUpdate(info: FrameInfo) {
    }

    protected override fun OnUpdate(info: FrameInfo) {
    }

}

private class MainWindow2 : MainWindow {

    private val Program: Program

    public constructor(program: Program) : super(MainWindowDesc.Window("Kotlin Game Example")) {
        this.Program = program
    }

    public override fun close() {
        super.close()
    }

    protected override fun OnShow() {
    }

    protected override fun OnHide() {
    }

    protected override fun OnDraw(info: FrameInfo) {
    }

    protected override fun OnMouseCursorMove(event: MouseCursorMoveEvent) {
    }

    protected override fun OnMouseButtonPress(event: MouseButtonActionEvent) {
    }

    protected override fun OnMouseButtonRelease(event: MouseButtonActionEvent) {
    }

    protected override fun OnMouseWheelScroll(event: MouseWheelScrollEvent) {
    }

    protected override fun OnKeyboardKeyPress(event: KeyboardKeyActionEvent) {
        if (event.Key == KeyboardKey.F1) {
            this.IsFullScreen = !this.IsFullScreen
        }

        if (event.Key == KeyboardKey.F2) {
            this.IsResizable = !this.IsResizable
        }

        if (event.Key == KeyboardKey.F3) {
            this.Cursor.IsVisible = !this.Cursor.IsVisible
        }
        if (event.Key == KeyboardKey.F4) {
            this.Cursor.IsGrabbed = !this.Cursor.IsGrabbed
        }
        if (event.Key == KeyboardKey.F5) {
            this.Cursor.IsCaptured = !this.Cursor.IsCaptured
        }
        if (event.Key == KeyboardKey.F6) {
            this.Cursor.IsLocked = !this.Cursor.IsLocked
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

    protected override fun OnKeyboardKeyRepeat(event: KeyboardKeyActionEvent) {
    }

    protected override fun OnKeyboardKeyRelease(event: KeyboardKeyActionEvent) {
    }

    protected override fun OnTextInput(text: String) {
    }

}
