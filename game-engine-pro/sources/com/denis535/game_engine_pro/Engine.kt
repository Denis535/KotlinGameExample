package com.denis535.game_engine_pro

import com.denis535.game_engine_pro.windows.*
import com.denis535.sdl.*
import kotlinx.cinterop.*

public abstract class Engine : AutoCloseable {

    @OptIn(ExperimentalForeignApi::class)
    public val IsClosed: Boolean
        get() {
            return SDL_WasInit(SDL_INIT_VIDEO) == 0U
        }

    @OptIn(ExperimentalForeignApi::class)
    public val Time: Double
        get() {
            check(!this.IsClosed)
            val ticks = SDL_GetTicks().also { Sdl.ThrowErrorIfNeeded() }
            return ticks.toDouble() / 1000.0
        }

    public var IsRunning: Boolean = false
        private set(value) {
            check(!this.IsClosed)
            check(field != value)
            field = value
        }

    public var Window: MainWindow? = null
        get() {
            check(!this.IsClosed)
            return field
        }
        set(value) {
            check(!this.IsClosed)
            if (field != null) {
                require(value == null)
                field = value
            } else {
                require(value != null)
                field = value
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    public constructor(id: String?, name: String? = null, version: String? = null, creator: String? = null) {
        check(SDL_WasInit(SDL_INIT_VIDEO) == 0U)
        SDL_Init(SDL_INIT_VIDEO).also { Sdl.ThrowErrorIfNeeded() }
        SDL_SetAppMetadata(name, version, id).also { Sdl.ThrowErrorIfNeeded() }
        SDL_SetAppMetadataProperty(SDL_PROP_APP_METADATA_CREATOR_STRING, creator).also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        check(!this.IsRunning)
        this.Window?.close()
        SDL_Quit().also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun Run(fixedDeltaTime: Float = 1.0f / 20.0f) {
        check(!this.IsClosed)
        check(!this.IsRunning)
        val info = FrameInfo()
        this.IsRunning = true
        this.OnStart(info)
        this.Window?.Show()
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
        this.Window?.Hide()
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
    private fun ProcessEvent(event: CPointer<SDL_Event>): Boolean {
        this@Engine.Window?.let { window ->
            if (event.pointed.window.windowID == SDL_GetWindowID(window.NativeWindowInternal).also { Sdl.ThrowErrorIfNeeded() }) {
                window.ProcessEvent(event)
                when (event.pointed.type) {
                    SDL_EVENT_WINDOW_CLOSE_REQUESTED -> {
                        return true
                    }
                }
            }
        }
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
        this.Window?.OnDrawInternal(info)
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
