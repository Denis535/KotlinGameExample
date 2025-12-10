package com.denis535.engine

import kotlinx.cinterop.*
import glfw.*

internal object GLFW2 {

    public fun ThrowErrorIfNeeded() {
        val (error, desc) = this.GetError()
        if (error != 0) {
            val message = if (desc != null) {
                "GLFW Error: $error, $desc"
            } else {
                "GLFW Error: $error"
            }
            error(message)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun GetError(): Pair<Int, String?> {
        return memScoped {
            val descPtr = this.alloc<CPointerVar<ByteVar>>()
            val error = glfwGetError(descPtr.ptr)
            val description = descPtr.value?.toKString()  // если указатель != null → строка
            Pair(error, description)
        }
    }

}
