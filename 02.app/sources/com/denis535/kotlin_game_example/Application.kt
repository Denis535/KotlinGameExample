package com.denis535.kotlin_game_example

import com.denis535.game_framework_pro.*

public class Application : AbstractApplication2 {

    public val Game: Game

    public constructor() {
        this.Game = Game()
    }

    protected override fun OnClose() {
        this.Game.close()
        super.OnClose()
    }

}
