package com.denis535.kotlin_game_example

import kotlin.reflect.*
import com.denis535.game_engine_pro.*
import com.denis535.game_framework_pro.*

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

    private val MainWindow: MainWindow
    private val Engine: AbstractEngine2

    public constructor() {
        this.MainWindow = MainWindow("Kotlin Game Example")
        this.Engine = object : AbstractEngine2(this.MainWindow) {

            protected override fun OnStart() {
                super.OnStart()
                println("OnStart")
            }

            protected override fun OnStop() {
                println("OnStop")
                super.OnStop()
            }

            protected override fun OnFrameBegin(info: FrameInfo) {
                super.OnFrameBegin(info)
            }

            protected override fun OnFixedUpdate(info: FrameInfo) {
                println("OnFixedUpdate: ${info.Time}")
            }

            protected override fun OnUpdate(info: FrameInfo) {
                println("OnUpdate: ${info.Time}")
            }

            protected override fun OnDraw(info: FrameInfo) {
                println("OnDraw: ${info.Time}")
            }

            protected override fun OnFrameEnd(info: FrameInfo) {
                super.OnFrameEnd(info)
            }

        }
        this.Application = Application()
        this.Router = Router()
        this.Screen = Screen()
        this.Theme = Theme()
        run {
            this.MainWindow.Show()
            this.Engine.Run()
        }
    }

    protected override fun OnClose() {
        run {
            this.MainWindow.Hide()
        }
        this.Theme!!.close()
        this.Screen!!.close()
        this.Router!!.close()
        this.Application!!.close()
        this.Engine.close()
        this.MainWindow.close()
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
        return null
    }

}
