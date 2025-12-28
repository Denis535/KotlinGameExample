package com.denis535.kotlin_game_example

import com.denis535.game_engine_pro.*
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

    private val Window: MainWindow

    @OptIn(ExperimentalForeignApi::class)
    public constructor() {
        this.Window = object : MainWindowImpl2(MainWindowDesc.FullScreen("Kotlin Game Example")) {

            protected override fun OnStart() {
//                println("OnStart")
            }

            protected override fun OnStop() {
//                println("OnStop")
            }

            protected override fun OnFrameBegin(info: FrameInfo) {
            }

            protected override fun OnFixedUpdate(info: FrameInfo) {
//                println("OnFixedUpdate: ${info.Time}")
            }

            protected override fun OnUpdate(info: FrameInfo) {
//                println("OnUpdate: ${info.Time}")
            }

            protected override fun OnDraw(info: FrameInfo) {
//                println("OnDraw: ${info.Time}")
            }

            protected override fun OnFrameEnd(info: FrameInfo) {
            }

            protected override fun OnMouseCursorMove(pos: Pair<Double, Double>) {
            }

            protected override fun OnMouseButtonPress(button: MouseButton) {
            }

            protected override fun OnMouseButtonRepeat(button: MouseButton) {
            }

            protected override fun OnMouseButtonRelease(button: MouseButton) {
            }

            protected override fun OnMouseWheelScroll(delta: Pair<Double, Double>) {
            }

            protected override fun OnKeyPress(key: Key) {
//                if (key == Key.F1) {
//                    if (this.IsFullScreen) {
//                        this.MakeWindowed()
//                    } else {
//                        this.MakeFullScreen()
//                    }
//                }
//                if (key == Key.F2) {
//                    this.CursorMode = if (this.CursorMode != ECursorMode.Normal) ECursorMode.Normal else ECursorMode.Hidden
//                }
//                if (key == Key.F3) {
//                    this.CursorMode = if (this.CursorMode != ECursorMode.Normal) ECursorMode.Normal else ECursorMode.Disabled
//                }
            }

            protected override fun OnKeyRepeat(key: Key) {
            }

            protected override fun OnKeyRelease(key: Key) {
            }

            protected override fun OnTextInput(char: UInt) {
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
