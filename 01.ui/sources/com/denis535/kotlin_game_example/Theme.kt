package com.denis535.kotlin_game_example

import com.denis535.game_framework_pro.*

public class Theme : AbstractTheme2<Router, Application> {

    public constructor() {
        this.Machine.SetRoot(MainPlayList().State, null, null)
        this.Machine.SetRoot(GamePlayList().State, null, null)
    }

    protected override fun OnClose() {
        this.Machine.SetRoot(null, null, null)
        super.OnClose()
    }

}

public class MainPlayList : AbstractPlayList {

    public constructor()

    protected override fun OnClose() {
    }

    protected override fun OnActivate(argument: Any?) {
    }

    protected override fun OnDeactivate(argument: Any?) {
    }

}

public class GamePlayList : AbstractPlayList {

    public constructor()

    protected override fun OnClose() {
    }

    protected override fun OnActivate(argument: Any?) {
    }

    protected override fun OnDeactivate(argument: Any?) {
    }

}
