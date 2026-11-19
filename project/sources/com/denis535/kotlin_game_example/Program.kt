package com.denis535.kotlin_game_example

import com.denis535.game_engine_pro.*
import com.denis535.game_engine_pro.display.*
import com.denis535.game_engine_pro.input.*
import com.denis535.game_engine_pro.storage.*
import com.denis535.game_framework_pro.*
import kotlin.experimental.*
import kotlin.reflect.*
import sdl.*

public fun Main(args: Array<String>) {
    Program().use {
        it.RequireDependency<AbstractProgram>(AbstractProgram::class)
        it.RequireDependency<Program>(Program::class)

        it.RequireDependency<AbstractTheme>(AbstractTheme::class)
        it.RequireDependency<Theme>(Theme::class)

        it.RequireDependency<AbstractScreen>(AbstractScreen::class)
        it.RequireDependency<Screen>(Screen::class)

        it.RequireDependency<AbstractRouter>(AbstractRouter::class)
        it.RequireDependency<Router>(Router::class)

        it.RequireDependency<AbstractApplication>(AbstractApplication::class)
        it.RequireDependency<Application>(Application::class)

        it.RequireDependency<AbstractGame>(AbstractGame::class)
        it.RequireDependency<Game>(Game::class)
    }
}

public class Program : AbstractProgram2<Theme, Screen, Router, Application> {

    private val Engine: ClientEngine2
        get() {
            check(!this.IsClosed)
            return field
        }

    public constructor() {
        this.Engine = ClientEngine2().apply {
            this.OnStartCallback = {
                this.Content!!.GetContents("").forEach {
                    println(it)
                }
            }
            this.OnStopCallback = {}
            this.Mouse?.OnMoveCallback = { event ->
            }
            this.Mouse?.OnButtonActionCallback = { event ->
            }
            this.Mouse?.OnWheelScrollCallback = { event ->
            }
            this.Keyboard?.OnKeyActionCallback = { event ->
                if (event.Key == KeyboardKey.Enter && this.Keyboard!!.IsKeyPressed(KeyboardKey.RightAlt)) {
                    this.Window.IsFullScreen = !this.Window.IsFullScreen
                }
            }
            this.OnFixedUpdateCallback = {
//                println("FixedFrame: " + FixedFrame.Number)
            }
            this.OnUpdateCallback = {
//                println("Frame: " + Frame.Number)
            }
        }
        this.Application = Application()
        this.Router = Router()
        this.Screen = Screen()
        this.Theme = Theme()
        run {
            this.Engine.Window.Show()
            this.Engine.Window.Raise()
            this.Engine.Run()
        }
    }

    protected override fun OnClose() {
        check(!this.IsClosed)
        this.Engine.close()
    }

    public override fun GetDependencyInternal(clazz: KClass<*>, argument: Any?): Any? {
        check(!this.IsClosed)
        val result = super.GetDependencyInternal(clazz, argument)
        if (result != null) {
            return result
        }
        this.Application?.Game?.let { game ->
            if (clazz.isInstance(game)) {
                return game
            }
        }

        if (clazz == com.denis535.game_engine_pro.Engine::class) {
            return this.Engine
        }
        if (clazz == com.denis535.game_engine_pro.ClientEngine::class) {
            return this.Engine
        }
        if (clazz == ClientEngine2::class) {
            return this.Engine
        }

        if (clazz == Window::class) {
            return this.Engine.Window
        }
        if (clazz == Cursor::class) {
            return this.Engine.Cursor
        }

        if (clazz == Touchscreen::class) {
            return this.Engine.Touchscreen
        }
        if (clazz == Mouse::class) {
            return this.Engine.Mouse
        }
        if (clazz == Keyboard::class) {
            return this.Engine.Keyboard
        }
        if (clazz == Gamepad::class) {
            return if (argument != null) {
                this.Engine.Gamepads?.getOrNull(argument as Int)
            } else {
                this.Engine.Gamepads?.getOrNull(0)
            }
        }

        if (clazz == Content::class) {
            return this.Engine.Content
        }
        if (clazz == Storage::class) {
            return this.Engine.Storage
        }

        return null
    }

}

private class ClientEngine2 : ClientEngine {

    @OptIn(ExperimentalNativeApi::class)
    public constructor() : super(
        manifest = Manifest("Kotlin Game Example", "com.denis535", "example", null, "Denis535"),
        window = { Window("Kotlin Game Example", isResizable = true) },
        video = null,
        audio = null
    ) {
        println("SDL Version: " + SDL.Version)
        println("SDL Version (Compiled): " + SDL.VersionCompiled)
        this.Touchscreen = Touchscreen()
        this.Mouse = Mouse()
        this.Keyboard = Keyboard()
        this.Gamepads = listOf(Gamepad())
        this.Content = if (Platform.isDebugBinary) {
            Content("../content/Content")
        } else {
            Content("Content")
        }
        this.Storage = Storage(this.Manifest.Group, this.Manifest.Name)
    }

    public override fun close() {
        check(!this.IsClosed)
        check(!this.IsRunning)
        super.close()
    }

    protected override fun OnStart() {
        super.OnStart()
    }

    protected override fun OnStop() {
        super.OnStop()
    }

    protected override fun OnFrameBegin() {
        super.OnFrameBegin()
    }

    protected override fun OnFrameEnd() {
        super.OnFrameEnd()
    }

}
