package com.denis535.game_engine_pro

import com.denis535.sdl.*
import kotlinx.cinterop.*
import kotlin.experimental.*

public object Engine {

    @OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
    public fun Initialize(id: String?, name: String? = null, version: String? = null, creator: String? = null) {
        SDL_Init(SDL_INIT_VIDEO).also { Sdl.ThrowErrorIfNeeded() }
        SDL_SetAppMetadata(name, version, id).also { Sdl.ThrowErrorIfNeeded() }
        SDL_SetAppMetadataProperty(SDL_PROP_APP_METADATA_CREATOR_STRING, creator).also { Sdl.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun Deinitialize() {
        SDL_Quit().also { Sdl.ThrowErrorIfNeeded() }
    }

}
