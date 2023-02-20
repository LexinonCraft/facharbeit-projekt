package com.lexinon.facharbeit;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.glViewport;

public class Window {

    private static Window window;

    private long handle;
    private boolean trackMouse = false;

    public static Window getOrCreate() {
        if(window != null)
            return window;
        window = new Window();
        return window.init();
    }

    private Window() {}

    private Window init() {
        if(!glfwInit())
            throw new IllegalStateException("GLFW could not be initialized!");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_FOCUS_ON_SHOW, GLFW_TRUE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_FALSE);
        glfwWindowHint(GLFW_SRGB_CAPABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        handle = glfwCreateWindow(800, 600, "", 0, 0);
        if(handle == 0)
            throw new IllegalStateException("Window could not be created!");

        glfwMakeContextCurrent(handle);
        GL.createCapabilities();
        glViewport(0, 0, 800, 600);

        glfwSetFramebufferSizeCallback(handle, this::framebufferSizeCallback);
        glfwSetKeyCallback(handle, this::keyCallback);
        glfwSetMouseButtonCallback(handle, this::mouseButtonCallback);

        glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        if (glfwRawMouseMotionSupported())
            glfwSetInputMode(handle, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);

        // ...

        return window;
    }

    public Window show() {
        glfwShowWindow(handle);
        return window;
    }

    public Window hide() {
        glfwHideWindow(handle);
        return window;
    }

    public Window update() {
        glfwSwapBuffers(handle);
        glfwPollEvents();
        return window;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    private void framebufferSizeCallback(long handle, int width, int height) {
        glViewport(0, 0, width, height);
    }

    private void keyCallback(long handle, int key, int scancode, int action, int mods) {
        if(key == GLFW_KEY_ESCAPE && trackMouse) {
            trackMouse = false;
            glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }
    }

    private void mouseButtonCallback(long handle, int button, int action, int mods) {
        if(button == GLFW_MOUSE_BUTTON_1 && !trackMouse) {
            trackMouse = true;
            glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }
    }

}
