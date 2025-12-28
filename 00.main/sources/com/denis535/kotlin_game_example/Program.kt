package com.denis535.kotlin_game_example

import com.denis535.game_engine_pro.*
import com.denis535.game_framework_pro.*
import kotlinx.cinterop.*
import kotlin.reflect.*

public fun Main(args: Array<String>) {
    Program().use { //        it.RequireDependency<AbstractProgram>(AbstractProgram::class)
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

    private val Window: MainWindow

    @OptIn(ExperimentalForeignApi::class)
    public constructor() {
        this.Window = object : MainWindowImpl2(MainWindowDesc.Window("Kotlin Game Example")) {

            protected override fun OnStart() {
            }

            protected override fun OnStop() {
            }

            protected override fun OnFrameBegin(info: FrameInfo) {
            }

            protected override fun OnFixedUpdate(info: FrameInfo) {
            }

            protected override fun OnUpdate(info: FrameInfo) {
            }

            protected override fun OnDraw(info: FrameInfo) {
            }

            protected override fun OnFrameEnd(info: FrameInfo) {
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
                    this.IsCursorVisible = !this.IsCursorVisible
                }
                if (event.Key == Key.F4) {
                    this.IsCursorGrabbed = !this.IsCursorGrabbed
                }
                if (event.Key == Key.F5) {
                    this.IsCursorCaptured = !this.IsCursorCaptured
                }
                if (event.Key == Key.F6) {
                    this.IsCursorLocked = !this.IsCursorLocked
                }
            }

            protected override fun OnKeyRepeat(event: KeyActionEvent) {
            }

            protected override fun OnKeyRelease(event: KeyActionEvent) {
            }

            protected override fun OnTextInput(text: String) {
            }

        }
        this.Application = Application()
        this.Router = Router()
        this.Screen = Screen()
        this.Theme = Theme()
        run {
            this.Window.Show()
            this.Window.Run()
        }
    }

    protected override fun OnClose() {
        run {
            this.Window.Hide()
        }
        this.Theme!!.close()
        this.Screen!!.close()
        this.Router!!.close()
        this.Application!!.close()
        this.Window.close()
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
        this.Window.let { window ->
            if (clazz.isInstance(window)) {
                return window
            }
        }
        return null
    }

}
