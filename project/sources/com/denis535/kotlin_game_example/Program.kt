package com.denis535.kotlin_game_example

import com.denis535.game_engine_pro.*
import com.denis535.game_engine_pro.display.*
import com.denis535.game_engine_pro.input.*
import com.denis535.game_engine_pro.storage.*
import com.denis535.game_framework_pro.*
import kotlinx.cinterop.*
import kotlin.experimental.*
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

    private val Engine: ClientEngine2

    @OptIn(ExperimentalForeignApi::class)
    public constructor() {
        this.Engine = ClientEngine2(Manifest("Kotlin Game Example", "com.denis535", "example", null, "Denis535")).apply {
            this.Window = Window2(WindowDescription.Window("Kotlin Game Example", IsResizable = true))
            this.Window!!.Show()
            this.Window!!.Raise()
            this.OnStartCallback = {
//                this.Content.GetDirectoryContents("").forEach {
//                    println(it)
//                }
            }
            this.OnStopCallback = {

            }
            this.OnDrawCallback = {
                Utils.Delay(10U)
            }
            this.OnUpdateCallback = {
//            println("Frame: " + Frame.Number)
            }
            this.OnFixedUpdateCallback = {
//            println("FixedFrame: " + FixedFrame.Number)
            }
            this.OnMouseMoveCallback = { event ->
//            println(event.Cursor)
            }
            this.OnMouseButtonActionCallback = { event ->
//            println(event.Button)
            }
            this.OnMouseWheelScrollCallback = { event ->
//            println(event.Scroll.Y)
            }
            this.OnKeyboardKeyActionCallback = { event ->
                if (event.Key == KeyboardKey.Enter && this.Keyboard.IsKeyPressed(KeyboardKey.RightAlt)) {
                    this.Window!!.IsFullScreen = !this.Window!!.IsFullScreen
                }
            }
        }
        this.Application = Application()
        this.Router = Router()
        this.Screen = Screen()
        this.Theme = Theme()
        this.Engine.Run()
    }

    protected override fun OnClose() {
        check(!this.IsClosed)
        this.Theme!!.close()
        this.Screen!!.close()
        this.Router!!.close()
        this.Application!!.close()
        this.Engine.close()
        super.OnClose()
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
        if (clazz == Window2::class) {
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
            if (argument != null) {
                return this.Engine.Gamepads.getOrNull(argument as Int)
            } else {
                return this.Engine.Gamepads.getOrNull(0)
            }
        }
        return null
    }

}

private class ClientEngine2 : ClientEngine {

    public val Content: Content
    public val Storage: Storage

    @OptIn(ExperimentalNativeApi::class)
    public constructor(manifest: Manifest) : super(manifest) {
        this.Content = if (Platform.isDebugBinary) {
            Content("../content-bundle")
        } else {
            Content("Content")
        }
        this.Storage = Storage(manifest.Group, manifest.Name)
    }

    public override fun close() {
        check(!this.IsClosed)
        check(!this.IsRunning)
        this.Storage.close()
        this.Content.close()
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
        this.Content.Process()
    }

    protected override fun OnFrameEnd() {
        super.OnFrameEnd()
    }

}

private class Window2 : Window {

    public constructor(description: WindowDescription) : super(description) {
    }

    public override fun close() {
        super.close()
    }

}
