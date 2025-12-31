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
    private class Engine2 : Engine {

        public constructor() : super("Kotlin Game Example")

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

        public constructor() : super(MainWindowDesc.Window("Kotlin Game Example"))

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

        protected override fun OnKeyPress(event: KeyActionEvent) {
            if (event.Key == Key.F1) {
                this.IsFullScreen = !this.IsFullScreen
            }

            if (event.Key == Key.F2) {
                this.IsResizable = !this.IsResizable
            }

            if (event.Key == Key.F3) {
                this.Cursor.IsVisible = !this.Cursor.IsVisible
            }
            if (event.Key == Key.F4) {
                this.Cursor.IsGrabbed = !this.Cursor.IsGrabbed
            }
            if (event.Key == Key.F5) {
                this.Cursor.IsCaptured = !this.Cursor.IsCaptured
            }
            if (event.Key == Key.F6) {
                this.Cursor.IsLocked = !this.Cursor.IsLocked
            }

            if (event.Key == Key.Digit_1) {
                this.Cursor.Style = CursorStyle.Arrow
            }
            if (event.Key == Key.Digit_2) {
                this.Cursor.Style = CursorStyle.Text
            }
            if (event.Key == Key.Digit_3) {
                this.Cursor.Style = CursorStyle.Pointer
            }
            if (event.Key == Key.Digit_4) {
                this.Cursor.Style = CursorStyle.Crosshair
            }
            if (event.Key == Key.Digit_5) {
                this.Cursor.Style = CursorStyle.Progress
            }
            if (event.Key == Key.Digit_6) {
                this.Cursor.Style = CursorStyle.Wait
            }
            if (event.Key == Key.Digit_7) {
                this.Cursor.Style = CursorStyle.NotAllowed
            }
            if (event.Key == Key.Keypad_0) {
                this.Cursor.Style = CursorStyle.Move
            }
            if (event.Key == Key.Keypad_1) {
                this.Cursor.Style = CursorStyle.SingleArrowResize_N
            }
            if (event.Key == Key.Keypad_2) {
                this.Cursor.Style = CursorStyle.SingleArrowResize_N_W
            }
            if (event.Key == Key.Keypad_3) {
                this.Cursor.Style = CursorStyle.DoubleArrowResize_N_S
            }
            if (event.Key == Key.Keypad_4) {
                this.Cursor.Style = CursorStyle.DoubleArrowResize_NW_SE
            }
        }

        protected override fun OnKeyRepeat(event: KeyActionEvent) {
        }

        protected override fun OnKeyRelease(event: KeyActionEvent) {
        }

        protected override fun OnTextInput(text: String) {
        }

    }

    private val Engine: Engine

    @OptIn(ExperimentalForeignApi::class)
    public constructor() {
        this.Engine = Engine2()
        this.Engine.Window = MainWindow2()
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
        this.Engine.Window?.Mouse?.let { mouse ->
            if (clazz.isInstance(mouse)) {
                return mouse
            }
        }
        this.Engine.Window?.Keyboard?.let { keyboard ->
            if (clazz.isInstance(keyboard)) {
                return keyboard
            }
        }
        return null
    }

}
