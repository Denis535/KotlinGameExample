package com.denis535.game_engine_pro.windows

import cnames.structs.*
import com.denis535.sdl.*
import kotlinx.cinterop.*

public open class MainWindow : AutoCloseable {
    public sealed class Desc(public val Title: String) {
        public class FullScreen(title: String) : Desc(title)
        public class Window(title: String, public val Width: Int = 1280, public val Height: Int = 720, public val IsResizable: Boolean = false) : Desc(title)
    }

    @OptIn(ExperimentalForeignApi::class)
    private var _NativeWindow: CPointer<SDL_Window>? = null

    @OptIn(ExperimentalForeignApi::class)
    public val IsClosed: Boolean
        get() {
            return this._NativeWindow == null
        }

    @OptIn(ExperimentalForeignApi::class)
    protected val NativeWindow: CPointer<SDL_Window>
        get() {
            check(!this.IsClosed)
            return this._NativeWindow!!
        }

    @OptIn(ExperimentalForeignApi::class)
    internal val NativeWindowInternal: CPointer<SDL_Window>
        get() {
            check(!this.IsClosed)
            return this._NativeWindow!!
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsFullScreen: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_FULLSCREEN != 0UL
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowFullscreen(this.NativeWindow, value).also { SDL.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Title: String
        get() {
            check(!this.IsClosed)
            val title = SDL_GetWindowTitle(this.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
            return title!!.toKString()
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowTitle(this.NativeWindow, value).also { SDL.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Position: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            memScoped {
                val x = this.alloc<IntVar>()
                val y = this.alloc<IntVar>()
                SDL_GetWindowPosition(this@MainWindow.NativeWindow, x.ptr, y.ptr).also { SDL.ThrowErrorIfNeeded() }
                return Pair(x.value, y.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowPosition(this.NativeWindow, value.first, value.second).also { SDL.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var Size: Pair<Int, Int>
        get() {
            check(!this.IsClosed)
            memScoped {
                val width = this.alloc<IntVar>()
                val height = this.alloc<IntVar>()
                SDL_GetWindowSize(this@MainWindow.NativeWindow, width.ptr, height.ptr).also { SDL.ThrowErrorIfNeeded() }
                return Pair(width.value, height.value)
            }
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowSize(this.NativeWindow, value.first, value.second).also { SDL.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsResizable: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_RESIZABLE != 0UL
        }
        set(value) {
            check(!this.IsClosed)
            SDL_SetWindowResizable(this.NativeWindow, value).also { SDL.ThrowErrorIfNeeded() }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsShown: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_HIDDEN == 0UL
        }
        set(value) {
            check(!this.IsClosed)
            if (value) {
                SDL_ShowWindow(this.NativeWindow)
            } else {
                SDL_HideWindow(this.NativeWindow)
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    public var IsTextInputEnabled: Boolean
        get() {
            check(!this.IsClosed)
            return SDL_TextInputActive(this.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
        }
        set(value) {
            check(!this.IsClosed)
            if (value) {
                SDL_StartTextInput(this.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
            } else {
                SDL_StopTextInput(this.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    public val HasFocus: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_INPUT_FOCUS == 0UL
        }

    @OptIn(ExperimentalForeignApi::class)
    public val HasMouseFocus: Boolean
        get() {
            check(!this.IsClosed)
            val flags = SDL_GetWindowFlags(this.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
            return flags and SDL_WINDOW_MOUSE_FOCUS == 0UL
        }

    public val Cursor: Cursor
        get() {
            check(!this.IsClosed)
            return field
        }

    @OptIn(ExperimentalForeignApi::class)
    public constructor(desc: Desc) {
        this._NativeWindow = run {
            when (desc) {
                is Desc.FullScreen -> {
                    var flags = SDL_WINDOW_VULKAN or SDL_WINDOW_FULLSCREEN
                    SDL_CreateWindow(desc.Title, 0, 0, flags).also { SDL.ThrowErrorIfNeeded() }
                }
                is Desc.Window -> {
                    var flags = SDL_WINDOW_VULKAN
                    if (desc.IsResizable) flags = flags or SDL_WINDOW_RESIZABLE
                    SDL_CreateWindow(desc.Title, desc.Width, desc.Height, flags).also { SDL.ThrowErrorIfNeeded() }.also {
                        SDL_SetWindowPosition(it, SDL_WINDOWPOS_CENTERED.toInt(), SDL_WINDOWPOS_CENTERED.toInt()).also { SDL.ThrowErrorIfNeeded() }
                    }
                }
            }
        }
        this.Cursor = Cursor(this)
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        this.Cursor.close()
        this._NativeWindow = run {
            SDL_DestroyWindow(this.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
            null
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun Raise() {
        SDL_RaiseWindow(this.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun RequestClose() {
        check(!this.IsClosed)
        memScoped {
            val event = this.alloc<SDL_Event>()
            event.type = SDL_EVENT_WINDOW_CLOSE_REQUESTED
            event.window.windowID = SDL_GetWindowID(this@MainWindow.NativeWindow).also { SDL.ThrowErrorIfNeeded() }
            SDL_PushEvent(event.ptr).also { SDL.ThrowErrorIfNeeded() }
        }
    }

}
