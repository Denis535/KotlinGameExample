package com.denis535.game_engine_pro

import com.denis535.sdl.*
import kotlinx.cinterop.*

public abstract class MainWindowImpl2 : MainWindowImpl {

    @OptIn(ExperimentalForeignApi::class)
    public constructor(desc: MainWindowDesc) : super(desc)

    @OptIn(ExperimentalForeignApi::class)
    public override fun close() {
        super.close()
    }

}
