package com.denis535.game_engine_pro

import com.denis535.sdl.*
import kotlinx.cinterop.*

public abstract class ServerEngine : Engine {

    @OptIn(ExperimentalForeignApi::class)
    public constructor(manifest: Manifest) {
        SDL_Init(0U).also { SDL.ThrowErrorIfNeeded() }
        SDL_SetAppMetadata(manifest.Name, manifest.Version, manifest.Id).also { SDL.ThrowErrorIfNeeded() }
        SDL_SetAppMetadataProperty(SDL_PROP_APP_METADATA_CREATOR_STRING, manifest.Creator).also { SDL.ThrowErrorIfNeeded() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        check(!this.IsClosed)
        check(!this.IsRunning)
        SDL_Quit().also { SDL.ThrowErrorIfNeeded() }
    }

}
