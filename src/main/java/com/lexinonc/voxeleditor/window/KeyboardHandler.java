package com.lexinonc.voxeleditor.window;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardHandler {

    private Window window;

    public KeyboardHandler(Window window) {
        this.window = window;
        long handle = window.getHandle();
        glfwSetCharCallback(handle, this::charCallback);
        glfwSetKeyCallback(handle, this::keyCallback);
    }

    private void charCallback(long window, int codePoint) {

    }

    private void keyCallback(long window, int key, int scanCode, int action, int mods) {

    }

}
