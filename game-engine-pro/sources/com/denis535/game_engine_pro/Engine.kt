package com.denis535.game_engine_pro

import com.denis535.game_engine_pro.windows.*
import com.denis535.sdl.*
import kotlinx.cinterop.*

public abstract class Engine : AutoCloseable {
    public class Manifest(public val Id: String?, public val Name: String? = null, public val Version: String? = null, public val Creator: String? = null)

    @OptIn(ExperimentalForeignApi::class)
    public val IsClosed: Boolean
        get() {
            return SDL_WasInit(0U) == 0U
        }

    @OptIn(ExperimentalForeignApi::class)
    public val Time: Double
        get() {
            check(!this.IsClosed)
            val ticks = SDL_GetTicks().also { Sdl.ThrowErrorIfNeeded() }
            return ticks.toDouble() / 1000.0
        }

    public var IsRunning: Boolean = false
        get() {
            check(!this.IsClosed)
            return field
        }
        private set(value) {
            check(!this.IsClosed)
            field = value
        }

    @OptIn(ExperimentalForeignApi::class)
    internal constructor() {
        check(SDL_WasInit(SDL_INIT_VIDEO) == 0U)
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun Run(fixedDeltaTime: Float = 1.0f / 20.0f) {
        check(!this.IsClosed)
        check(!this.IsRunning)
        val info = FrameInfo()
        this.IsRunning = true
        this.OnStart(info)
        while (true) {
            val startTime = this.Time
            if (this.ProcessEvents()) {
                break
            }
            this.Update(info, fixedDeltaTime)
            val endTime = this.Time
            val deltaTime = (endTime - startTime).toFloat()
            info.Number++
            info.Time += deltaTime
            info.DeltaTime = deltaTime
        }
        this.OnStop(info)
        this.IsRunning = false
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun ProcessEvents(): Boolean {
        memScoped {
            val event = this.alloc<SDL_Event>()
            while (SDL_PollEvent(event.ptr)) {
                if (this@Engine.ProcessEvent(event.ptr)) {
                    return true
                }
            }
        }
        return false
    }

    @OptIn(ExperimentalForeignApi::class)
    internal open fun ProcessEvent(event: CPointer<SDL_Event>): Boolean {
        when (event.pointed.type) {
            SDL_EVENT_QUIT -> {
                return true
            }
        }
        return false
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun Update(info: FrameInfo, fixedDeltaTime: Float) {
        if (info.FixedFrameInfo.Number == 0) {
            this.OnFixedUpdate(info)
            info.FixedFrameInfo.Number++
            info.FixedFrameInfo.DeltaTime = fixedDeltaTime
        } else {
            while (info.FixedFrameInfo.Time <= info.Time) {
                this.OnFixedUpdate(info)
                info.FixedFrameInfo.Number++
                info.FixedFrameInfo.DeltaTime = fixedDeltaTime
            }
        }
        this.OnUpdate(info)
    }

    protected abstract fun OnStart(info: FrameInfo)
    protected abstract fun OnStop(info: FrameInfo)

    protected abstract fun OnFixedUpdate(info: FrameInfo)
    protected abstract fun OnUpdate(info: FrameInfo)

    @OptIn(ExperimentalForeignApi::class)
    public fun RequestQuit() {
        check(!this.IsClosed)
        check(this.IsRunning)
        memScoped {
            val event = this.alloc<SDL_Event>()
            event.type = SDL_EVENT_QUIT
            SDL_PushEvent(event.ptr).also { Sdl.ThrowErrorIfNeeded() }
        }
    }

}

public abstract class ServerEngine : Engine {

    @OptIn(ExperimentalForeignApi::class)
    public constructor(manifest: Manifest) : super() {
        SDL_Init(0U).also { Sdl.ThrowErrorIfNeeded() }
        SDL_SetAppMetadata(manifest.Name, manifest.Version, manifest.Id).also { Sdl.ThrowErrorIfNeeded() }
        SDL_SetAppMetadataProperty(SDL_PROP_APP_METADATA_CREATOR_STRING, manifest.Creator).also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        check(!this.IsRunning)
        SDL_Quit().also { Sdl.ThrowErrorIfNeeded() }
    }

}

public abstract class ClientEngine : Engine {

    public val Mouse: Mouse
        get() {
            check(!this.IsClosed)
            return field
        }

    public val Keyboard: Keyboard
        get() {
            check(!this.IsClosed)
            return field
        }

    public var Window: MainWindow? = null
        get() {
            check(!this.IsClosed)
            return field
        }
        protected set(value) {
            check(!this.IsClosed)
            field = value
        }

    @OptIn(ExperimentalForeignApi::class)
    public constructor(manifest: Manifest) : super() {
        SDL_Init(SDL_INIT_VIDEO).also { Sdl.ThrowErrorIfNeeded() }
        SDL_SetAppMetadata(manifest.Name, manifest.Version, manifest.Id).also { Sdl.ThrowErrorIfNeeded() }
        SDL_SetAppMetadataProperty(SDL_PROP_APP_METADATA_CREATOR_STRING, manifest.Creator).also { Sdl.ThrowErrorIfNeeded() }
        this.Mouse = Mouse()
        this.Keyboard = Keyboard()
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        check(!this.IsRunning)
        this.Keyboard.close()
        this.Mouse.close()
        SDL_Quit().also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    internal override fun ProcessEvent(event: CPointer<SDL_Event>): Boolean {
        if (super.ProcessEvent(event)) {
            return true
        }
        this.Window!!.let { window ->
            if (event.pointed.window.windowID == SDL_GetWindowID(window.NativeWindowInternal).also { Sdl.ThrowErrorIfNeeded() }) {
                window.ProcessEvent(event)
                when (event.pointed.type) {
                    SDL_EVENT_WINDOW_CLOSE_REQUESTED -> {
                        return true
                    }
                }
            }
        }
        return false
    }

    protected override fun OnStart(info: FrameInfo) {
        this.Window!!.Show()
    }

    protected override fun OnStop(info: FrameInfo) {
        this.Window!!.Hide()
    }

}

public class FrameInfo {

    public val FixedFrameInfo: FixedFrameInfo = FixedFrameInfo()

    public var Number: Int = 0
        internal set

    public var Time: Float = 0.0f
        internal set

    public var DeltaTime: Float = 0.0f
        internal set

    public val Fps: Float
        get() {
            return if (this.DeltaTime > 0.0f) 1.0f / this.DeltaTime else 0.0f
        }

    internal constructor()

    public override fun toString(): String {
        return "FrameInfo(Number=${this.Number}, Time=${this.Time})"
    }

}

public class FixedFrameInfo {

    public var Number: Int = 0
        internal set

    public val Time: Float
        get() {
            return this.Number * this.DeltaTime
        }

    public var DeltaTime: Float = 0.0f
        internal set

    internal constructor()

    public override fun toString(): String {
        return "FixedFrameInfo(Number=${this.Number}, Time=${this.Time})"
    }

}
