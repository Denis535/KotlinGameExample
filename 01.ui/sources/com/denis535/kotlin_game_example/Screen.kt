package com.denis535.kotlin_game_example

import com.denis535.game_framework_pro.*

public class Screen : AbstractScreen2<Router, Application> {

    public constructor() {
        this.Machine.SetRoot(RootWidget().Node, null, null)
    }

    protected override fun OnClose() {
        this.Machine.Root!!.close()
    }

}

public class RootWidget : AbstractWidget2 {

    public constructor() {
        this.NodeMutable.AddChild(MainWidget().Node, null)
        this.NodeMutable.AddChild(GameWidget().Node, null)
    }

    protected override fun OnClose() {
        this.NodeMutable.Children.asReversed().forEach { it.close() }
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
        this.NodeMutable.Children.asReversed().forEach { it.close() }
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
        this.NodeMutable.Children.asReversed().forEach { it.close() }
    }

    protected override fun OnActivate(argument: Any?) {
    }

    protected override fun OnDeactivate(argument: Any?) {
    }

}
