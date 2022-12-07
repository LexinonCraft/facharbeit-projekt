package com.lexinonc.voxeleditor.window;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window implements AutoCloseable {

    private final long handle;
    private MouseHandler mouseHandler;
    private KeyboardHandler keyboardHandler;
    private int windowWidth, windowHeight;
    private int framebufferWidth, framebufferHeight;
    private boolean shouldClose = false;
    private boolean focused;
    private boolean visible;

    public Window(int width, int height, String title) {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_AUTO_ICONIFY, GLFW_FALSE);
        glfwWindowHint(GLFW_FOCUS_ON_SHOW, GLFW_TRUE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_FALSE);
        glfwWindowHint(GLFW_SRGB_CAPABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        handle = glfwCreateWindow(width, height, title, 0, 0);
        if(handle == 0)
            throw new IllegalStateException(String.format("Window with title %s could not be created", title));
        registerCallbacks();

        glfwMakeContextCurrent(handle);
        GL.createCapabilities();

        updateFramebufferSize();
        updateWindowSize();
        checkFocusStatus();
    }

    public void refresh() {
        glClearColor(1f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT);
        glfwSwapBuffers(handle);
        glfwPollEvents();
    }

    @Override
    public void close() {
        glfwDestroyWindow(handle);
    }

    private void registerCallbacks() {
        mouseHandler = new MouseHandler(this);
        keyboardHandler = new KeyboardHandler(this);
        glfwSetFramebufferSizeCallback(handle, this::framebufferSizeCallback);
        glfwSetWindowSizeCallback(handle, this::windowSizeCallback);
        glfwSetWindowCloseCallback(handle, this::windowCloseCallback);
        glfwSetWindowFocusCallback(handle, this::windowFocusCallback);
        //glfwSetWindowRefreshCallback(handle, this::refreshCallback);
    }

    /*private void refreshCallback(long myHandle) {
        glClearColor(1f, 0f, 0f, 1f);
        glClear(GL_COLOR_BUFFER_BIT);
        glfwSwapBuffers(handle);
    }*/

    private void framebufferSizeCallback(long myHandle, int width, int height) {
        updateFramebufferSize(width, height);
    }

    private void windowSizeCallback(long myHandle, int width, int height) {
        updateWindowSize(width, height);
    }

    private void windowCloseCallback(long myHandle) {
        requestClose();
        glfwSetWindowShouldClose(handle, false);
    }

    private void windowFocusCallback(long myHandle, boolean nowFocused) {
        if(nowFocused)
            nowFocused();
        else
            nowUnfocused();
    }

    private void updateFramebufferSize(int width, int height) {
        framebufferWidth = width;
        framebufferHeight = height;
        glViewport(0, 0, framebufferWidth, framebufferHeight);
    }

    public void updateFramebufferSize() {
        IntBuffer tempFramebufferWidth = BufferUtils.createIntBuffer(1);
        IntBuffer tempFramebufferHeight = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(handle, tempFramebufferWidth, tempFramebufferHeight);
        updateFramebufferSize(tempFramebufferWidth.get(), tempFramebufferHeight.get());
    }

    private void updateWindowSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    public void updateWindowSize() {
        IntBuffer tempWindowWidth = BufferUtils.createIntBuffer(1);
        IntBuffer tempWindowHeight = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(handle, tempWindowWidth, tempWindowHeight);
        updateWindowSize(tempWindowWidth.get(), tempWindowHeight.get());
    }

    public void requestClose() {
        shouldClose = true;
    }

    public void cancelCloseRequest() {
        shouldClose = false;
    }

    private void nowFocused() {
        focused = true;
        // ...
    }

    private void nowUnfocused() {
        focused = false;
        // ...
    }

    public void focus() {
        glfwFocusWindow(handle);
    }

    public boolean checkFocusStatus() {
        return focused = (glfwGetWindowAttrib(handle, GLFW_FOCUSED) == GLFW_TRUE);
    }

    public long getHandle() {
        return handle;
    }

    public MouseHandler getMouseHandler() {
        return mouseHandler;
    }
    public KeyboardHandler getKeyboardHandler() {
        return keyboardHandler;
    }

    public int getWindowWidth() {
        return windowWidth;
    }
    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowWidth(int newWidth) {
        setWindowSize(newWidth, windowHeight);
    }
    public void setWindowHeight(int newHeight) {
        setWindowSize(windowWidth, newHeight);
    }
    public void setWindowSize(int newWidth, int newHeight) {
        glfwSetWindowSize(handle, newWidth, newHeight);
        updateWindowSize();
    }

    public int getFramebufferWidth() {
        return framebufferWidth;
    }
    public int getFramebufferHeight() {
        return framebufferHeight;
    }

    public boolean isCloseRequested() {
        return shouldClose;
    }

    public boolean isFocused() {
        return focused;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean nowVisible) {
        visible = nowVisible;
        if(visible)
            glfwShowWindow(handle);
        else
            glfwHideWindow(handle);
    }
}
