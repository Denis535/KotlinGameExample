package com.denis535.kotlin_game_example

import com.denis535.game_framework_pro.*

public class Screen : AbstractScreen2<Router, Application> {

    public constructor() {
        this.Machine.SetRoot(RootWidget().BaseObject, null, null)
    }

    protected override fun OnClose() {
        this.Machine.Root!!.close()
    }

}

public class RootWidget : AbstractWidget2 {

    public constructor() {
        this.BaseObject.AddChild(MainWidget().BaseObject, null)
        this.BaseObject.AddChild(GameWidget().BaseObject, null)
    }

    protected override fun OnClose() {
        this.BaseObject.Children.asReversed().forEach { it.close() }
    }

    protected override fun OnActivate(argument: Any?) {
    }

    protected override fun OnDeactivate(argument: Any?) {
    }

}

public class MainWidget : AbstractViewableWidget2 {
    internal class View {
        public constructor()
    }

    public constructor() {
        this.View = View()
    }

    protected override fun OnClose() {
        this.BaseObject.Children.asReversed().forEach { it.close() }
    }

    protected override fun OnActivate(argument: Any?) {
    }

    protected override fun OnDeactivate(argument: Any?) {
    }

}

public class GameWidget : AbstractViewableWidget2 {
    internal class View {
        public constructor()
    }

    public constructor() {
        this.View = View()
    }

    protected override fun OnClose() {
        this.BaseObject.Children.asReversed().forEach { it.close() }
    }

    protected override fun OnActivate(argument: Any?) {
    }

    protected override fun OnDeactivate(argument: Any?) {
    }

}
