package com.denis535.kotlin_game_example

import com.denis535.game_framework_pro.*
import com.denis535.kotlin_game_example.*

@JvmName("main")
fun Main(args: Array<String>) {
    Program().use {}
}

public class Program : AbstractProgram2<Theme, Screen, Router, Application> {

    public constructor() {
        Engine.Initialize()
        this.Application = Application()
        this.Router = Router()
        this.Screen = Screen()
        this.Theme = Theme()
        Engine.CreateWindow("Kotlin Game Example")
        MainLoop.Run()
        Engine.DestroyWindow()
    }

    protected override fun OnClose() {
        this.Theme!!.close()
        this.Screen!!.close()
        this.Router!!.close()
        this.Application!!.close()
        Engine.Deinitialize()
        super.OnClose()
    }

}
