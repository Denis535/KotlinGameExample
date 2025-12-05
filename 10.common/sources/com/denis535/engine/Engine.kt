//package com.denis535.engine
//
//import org.lwjgl.glfw.GLFW
//
//public class Engine : AutoCloseable {
//
//    private val Window: MainWindow
//    private val OnFixedUpdateCallback: ((FixedUpdateInfo) -> Unit)
//    private val OnUpdateCallback: ((UpdateInfo) -> Unit)
//
//    public var IsRunning: Boolean = false
//        private set(value) {
//            check(field != value)
//            field = value
//        }
//
//    public var Fps: Double = 0.0
//        get() {
//            check(this.IsRunning)
//            return field
//        }
//        private set(value) {
//            check(this.IsRunning)
//            field = value
//        }
//
//    public constructor(window: MainWindow, onFixedUpdateCallback: ((FixedUpdateInfo) -> Unit), onUpdateCallback: ((UpdateInfo) -> Unit)) {
//        this.Window = window.also { require(!it.IsClosed) }
//        this.OnFixedUpdateCallback = onFixedUpdateCallback
//        this.OnUpdateCallback = onUpdateCallback
//    }
//
//    public override fun close() {
//        check(!this.Window.IsClosed)
//        check(!this.IsRunning)
//    }
//
//    public fun Run(fixedDeltaTime: Double = 1.0 / 20.0) {
//        this.IsRunning = true
//        this.Fps = 0.0
//        val info = UpdateInfo()
//        while (!this.Window.IsClosingRequested) {
//            val deltaTime = run {
//                val startTime = this.Window.Time
//                this.OnFrameBegin()
//                this.OnFixedUpdate(info)
//                this.OnUpdate(info)
//                this.OnFrameEnd()
//                val endTime = this.Window.Time
//                endTime - startTime
//            }
//            this.Fps = 1.0 / deltaTime
//            info.FixedUpdateInfo.DeltaTime = fixedDeltaTime
//            info.Time += deltaTime
//            info.DeltaTime = deltaTime
//        }
//        this.IsRunning = false
//    }
//
//    private fun OnFrameBegin() {
//        GLFW.glfwPollEvents().also { GLFW2.ThrowErrorIfNeeded() }
//    }
//
//    private fun OnFixedUpdate(info: UpdateInfo) {
//        if (info.FixedUpdateInfo.Number == 0) {
//            this.OnFixedUpdateCallback(info.FixedUpdateInfo)
//            info.FixedUpdateInfo.Number++
//        } else {
//            while (info.FixedUpdateInfo.Time <= info.Time) {
//                this.OnFixedUpdateCallback(info.FixedUpdateInfo)
//                info.FixedUpdateInfo.Number++
//            }
//        }
//    }
//
//    private fun OnUpdate(info: UpdateInfo) {
////        if (GLFW.glfwGetKey(this.Window.NativeWindowPointer, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(this.Window.NativeWindowPointer, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS) {
////            if (GLFW.glfwGetKey(this.Window.NativeWindowPointer, GLFW.GLFW_KEY_ENTER) == GLFW.GLFW_PRESS) {
////                this.Window.IsFullscreen = !this.Window.IsFullscreen
////            }
////        }
//        this.OnUpdateCallback(info)
//        info.Number++
//    }
//
//    private fun OnFrameEnd() {
//        GLFW.glfwSwapBuffers(this.Window.NativeWindowPointer).also { GLFW2.ThrowErrorIfNeeded() }
//    }
//
//}
//
//public class UpdateInfo {
//
//    public val FixedUpdateInfo: FixedUpdateInfo = FixedUpdateInfo()
//
//    public var Number: Int = 0
//        internal set
//
//    public var Time: Double = 0.0
//        internal set
//
//    public var DeltaTime: Double = 0.0
//        internal set
//
//    internal constructor()
//
//    public override fun toString(): String {
//        return "UpdateInfo(Number=${this.Number}, Time=${this.Time})"
//    }
//
//}
//
//public class FixedUpdateInfo {
//
//    public var Number: Int = 0
//        internal set
//
//    public val Time: Double
//        get() {
//            return this.Number * this.DeltaTime
//        }
//
//    public var DeltaTime: Double = 0.0
//        internal set
//
//    internal constructor()
//
//    public override fun toString(): String {
//        return "FixedUpdateInfo(Number=${this.Number}, Time=${this.Time})"
//    }
//
//}
