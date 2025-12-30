package com.denis535.game_engine_pro.windows

import com.denis535.sdl.*
import kotlinx.cinterop.*

public object MessageWindow {

    @OptIn(ExperimentalForeignApi::class)
    public fun ShowInfoMessage(title: String?, message: String?) {
        SDL_ShowSimpleMessageBox(SDL_MESSAGEBOX_INFORMATION, title, message, null).also { SDL_ClearError() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun ShowWarningMessage(title: String?, message: String?) {
        SDL_ShowSimpleMessageBox(SDL_MESSAGEBOX_WARNING, title, message, null).also { SDL_ClearError() }
    }

    @OptIn(ExperimentalForeignApi::class)
    public fun ShowErrorMessage(title: String?, message: String?) {
        SDL_ShowSimpleMessageBox(SDL_MESSAGEBOX_ERROR, title, message, null).also { SDL_ClearError() }
    }

}
