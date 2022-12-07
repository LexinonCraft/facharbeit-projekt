package com.lexinonc.voxeleditor.window;

import static org.lwjgl.glfw.GLFW.*;

public class MouseHandler {

    private Window window;

    public MouseHandler(Window window) {
        this.window = window;
        long handle = window.getHandle();
        glfwSetScrollCallback(handle, this::scrollCallback);
        glfwSetCursorEnterCallback(handle, this::cursorEnterCallback);
        glfwSetCursorPosCallback(handle, this::cursorPosCallback);
        glfwSetMouseButtonCallback(handle, this::mouseButtonCallback);
    }

    private void scrollCallback(long window, double xOffset, double yOffset) {

    }

    private void cursorEnterCallback(long window, boolean entered) {
        if(entered) {

        } else {

        }
    }

    private void cursorPosCallback(long window, double xPos, double yPos) {

    }

    private void mouseButtonCallback(long window, int button, int action, int mods) {

    }

}
