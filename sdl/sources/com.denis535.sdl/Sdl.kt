package com.denis535.sdl

import kotlinx.cinterop.*

public object Sdl {

    public fun ThrowErrorIfNeeded() {
        val error = this.GetError()
        if (error != null) {
            error("SDL Error: $error")
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun GetError(): String? {
        return SDL_GetError()?.toKString()
    }

}
