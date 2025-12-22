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

    private val Window: MainWindow3

    @OptIn(ExperimentalForeignApi::class)
    public constructor() {
        this.Window = object : MainWindow3(this.Window("Kotlin Game Example")) {

            protected override fun OnStart() {
                super.OnStart()
//                println("OnStart")
            }

            protected override fun OnStop() {
//                println("OnStop")
                super.OnStop()
            }

            protected override fun OnFrameBegin(info: FrameInfo) {
                super.OnFrameBegin(info)
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
                super.OnFrameEnd(info)
            }

            protected override fun OnMouseCursorEnter() {
            }

            protected override fun OnMouseCursorLeave() {
            }

            protected override fun OnMouseCursorMove(posX: Double, posY: Double) {
            }

            protected override fun OnMouseButtonPress(button: MouseButton) {
            }

            protected override fun OnMouseButtonRepeat(button: MouseButton) {
            }

            protected override fun OnMouseButtonRelease(button: MouseButton) {
            }

            protected override fun OnMouseWheelScroll(deltaX: Double, deltaY: Double) {
            }

            protected override fun OnKeyPress(key: Key) {
                if (key == Key.F1) {
                    if (this.IsFullScreen) {
                        this.MakeWindowed()
                    } else {
                        this.MakeFullScreen()
                    }
                }
                if (key == Key.F2) {
                    this.IsCursorEnabled = !this.IsCursorEnabled
                }
                if (key == Key.F3) {
                    this.IsCursorVisible = !this.IsCursorVisible
                }
            }

            protected override fun OnKeyRepeat(key: Key) {
            }

            protected override fun OnKeyRelease(key: Key) {
            }

            protected override fun OnCharInput(char: UInt) {
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
