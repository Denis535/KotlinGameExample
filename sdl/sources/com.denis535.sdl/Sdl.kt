package com.denis535.sdl

import kotlinx.cinterop.*

public object Sdl {

    @OptIn(ExperimentalForeignApi::class)
    public fun ThrowErrorIfNeeded() {
        val error = SDL_GetError()
        if (error != null && error.pointed.value.toInt() != 0) {
            error(error.toKString())
        }
    }

}
