package com.denis535.kotlin_game_example

import kotlin.reflect.*
import com.denis535.engine.*
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
    private val Engine: Engine

    public constructor() {
        this.MainWindow = MainWindow("Kotlin Game Example")
        this.Engine = Engine(this.MainWindow).apply {
            this.OnFixedUpdateCallback = this@Program::OnFixedUpdate
            this.OnUpdateCallback = this@Program::OnUpdate
            this.OnDrawCallback = this@Program::OnDraw
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

    private fun OnFixedUpdate(info: FixedFrameInfo) {
        println("OnFixedUpdate: ${info.Time}")
    }

    private fun OnUpdate(info: FrameInfo) {
        println("OnUpdate: ${info.Time}")
    }

    private fun OnDraw(info: FrameInfo) {
        println("OnDraw: ${info.Time}")
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
