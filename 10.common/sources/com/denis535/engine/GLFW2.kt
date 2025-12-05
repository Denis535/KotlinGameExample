//package com.denis535.engine
//
//import org.lwjgl.system.MemoryUtil
//import org.lwjgl.glfw.GLFW
//
//// Api
//// GLFW
//// GLFW.Functions              - Contains the function pointers loaded from the glfw SharedLibrary.
//
//// Api/Vulkan
//// GLFWVulkan
//// GLFWVulkan.Functions        - Contains the function pointers loaded from GLFW.getLibrary().
//
//// Api/Native/OS
//// GLFWNativeWin32             - Native Win32 API access for Windows.
//// GLFWNativeWin32.Functions   - Contains the function pointers loaded from GLFW.getLibrary().
//// GLFWNativeCocoa             - Native Cocoa API access for macOS.
//// GLFWNativeCocoa.Functions   - Contains the function pointers loaded from GLFW.getLibrary().
//// GLFWNativeX11               - Native X11 API access for Linux (Xorg).
//// GLFWNativeX11.Functions     - Contains the function pointers loaded from GLFW.getLibrary().
//// GLFWNativeWayland           - Native Wayland API access for Linux (Wayland).
//// GLFWNativeWayland.Functions - Contains the function pointers loaded from GLFW.getLibrary().
//
//// Api/Native/OpenGL
//// GLFWNativeWGL               - Native WGL interface for OpenGL on Windows.
//// GLFWNativeWGL.Functions     - Contains the function pointers loaded from GLFW.getLibrary().
//// GLFWNativeGLX               - Native GLX interface for OpenGL on X11.
//// GLFWNativeGLX.Functions     - Contains the function pointers loaded from GLFW.getLibrary().
//// GLFWNativeNSGL              - Native NSGL interface for OpenGL on macOS.
//// GLFWNativeNSGL.Functions    - Contains the function pointers loaded from GLFW.getLibrary().
//// GLFWNativeEGL               - Native EGL interface for OpenGL ES / Vulkan platforms.
//// GLFWNativeEGL.Functions     - Contains the function pointers loaded from GLFW.getLibrary().
//// GLFWNativeOSMesa            - Native OSMesa interface for off-screen OpenGL rendering.
//// GLFWNativeOSMesa.Functions  - Contains the function pointers loaded from GLFW.getLibrary().
//
//// Data/Memory
//// GLFWAllocator               - struct GLFWallocator { GLFWallocatefun allocate; GLFWreallocatefun reallocate; GLFWdeallocatefun deallocate; void * user; }
//// GLFWAllocator.Buffer        - An array of GLFWAllocator structs.
//
//// Data/Monitor
//// GLFWVidMode                 - struct GLFWVidMode { int width; int height; int redBits; int greenBits; int blueBits; int refreshRate; }
//// GLFWVidMode.Buffer          - An array of GLFWVidMode structs.
//// GLFWGammaRamp               - struct GLFWGammaRamp { unsigned short * red; unsigned short * green; unsigned short * blue; unsigned int size; }
//// GLFWGammaRamp.Buffer        - An array of GLFWGammaRamp structs.
//
//// Data/Image
//// GLFWImage                   - struct GLFWimage { int width; int height; unsigned char * pixels; }
//// GLFWImage.Buffer            - An array of GLFWImage structs.
//
//// Data/Input
//// GLFWGamepadState            - struct GLFWGamepadState { unsigned char buttons[15]; float axes[6]; }
//// GLFWGamepadState.Buffer     - An array of GLFWGamepadState structs.
//
//// Utils
//// Callbacks                   - Utility class for GLFW callbacks.
//
//// Callback/Memory
//// GLFWAllocateCallback
//// GLFWAllocateCallbackI
//// GLFWDeallocateCallback
//// GLFWDeallocateCallbackI
//// GLFWReallocateCallback
//// GLFWReallocateCallbackI
//
//// Callback/Monitor
//// GLFWMonitorCallback
//// GLFWMonitorCallbackI
//
//// Callback/Window
//// GLFWWindowFocusCallback
//// GLFWWindowFocusCallbackI
//// GLFWWindowIconifyCallback
//// GLFWWindowIconifyCallbackI
//// GLFWWindowMaximizeCallback
//// GLFWWindowMaximizeCallbackI
//// GLFWWindowCloseCallback
//// GLFWWindowCloseCallbackI
//
//// Callback/Window
//// GLFWWindowRefreshCallback
//// GLFWWindowRefreshCallbackI
//// GLFWWindowContentScaleCallback
//// GLFWWindowContentScaleCallbackI
//// GLFWFramebufferSizeCallback
//// GLFWFramebufferSizeCallbackI
//
//// Callback/Window
//// GLFWWindowPosCallback
//// GLFWWindowPosCallbackI
//// GLFWWindowSizeCallback
//// GLFWWindowSizeCallbackI
//
//// Callback/Cursor
//// GLFWCursorEnterCallback
//// GLFWCursorEnterCallbackI
//// GLFWCursorPosCallback
//// GLFWCursorPosCallbackI
//// GLFWDropCallback
//// GLFWDropCallbackI
//
//// Callback/Mouse
//// GLFWMouseButtonCallback
//// GLFWMouseButtonCallbackI
//// GLFWScrollCallback
//// GLFWScrollCallbackI
//
//// Callback/Keyboard
//// GLFWKeyCallback
//// GLFWKeyCallbackI
//
//// Callback/Joystick
//// GLFWJoystickCallback
//// GLFWJoystickCallbackI
//
//// Callback/Text
//// GLFWCharCallback
//// GLFWCharCallbackI
//// GLFWCharModsCallback
//// GLFWCharModsCallbackI
//// GLFWIMEStatusCallback
//// GLFWIMEStatusCallbackI
//// GLFWPreeditCallback
//// GLFWPreeditCallbackI
//// GLFWPreeditCandidateCallback
//// GLFWPreeditCandidateCallbackI
//
//// Callback/Error
//// GLFWErrorCallback
//// GLFWErrorCallbackI
//
//internal object GLFW2 {
//
//    private val PointerBuffer = MemoryUtil.memAllocPointer(1)
//
//    public fun ThrowErrorIfNeeded() {
//        val (error, desc) = this.GetError()
//        if (error != 0) {
//            val message = if (desc != null) {
//                "GLFW Error: $error, $desc"
//            } else {
//                "GLFW Error: $error"
//            }
//            error(message)
//        }
//    }
//
//    private fun GetError(): Pair<Int, String?> {
//        val error = GLFW.glfwGetError(this.PointerBuffer)
//        val pointer = this.PointerBuffer[0]
//        val description = if (pointer != 0L) MemoryUtil.memUTF8(pointer) else null
//        return Pair(error, description)
//    }
//
//}
