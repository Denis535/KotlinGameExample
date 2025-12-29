package com.denis535.game_engine_pro

public sealed class MainWindowDesc(public val Title: String) {
    public class FullScreen(title: String) : MainWindowDesc(title)
    public class Window(title: String, public val Width: Int = 1280, public val Height: Int = 720, public val IsResizable: Boolean = false) : MainWindowDesc(title)
}
