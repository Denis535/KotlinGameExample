package com.denis535.kotlin_game_example

import com.denis535.game_framework_pro.*
import com.denis535.kotlin_game_example.*

@JvmName("main")
fun Main(args: Array<String>) {
    Program().use {}
}

public class Program : AbstractProgram2<Theme, Screen, Router, Application> {

    private val MainWindow: MainWindow
    private val Engine: Engine

    public constructor() {
        this.MainWindow = MainWindow("Kotlin Game Example")
        this.Engine = Engine(this.MainWindow, this::OnFixedUpdate, this::OnUpdate)
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

    private fun OnFixedUpdate(time: Time) {
        println("OnFixedUpdate: " + time.Time)
    }

    private fun OnUpdate(time: Time) {
        println("OnUpdate: " + time.Time)
    }

    public override fun GetDependency(clazz: kotlin.reflect.KClass<*>, argument: Any?): Any? {
        return super.GetDependency(clazz, argument)
    }

}
