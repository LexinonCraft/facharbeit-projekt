package com.lexinon.facharbeit;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.glViewport;

public class Window {

    private long handle;
    private long monitor;
    private boolean fullscreen = false;
    private int windowedXPos, windowedYPos;
    private int windowedWidth, windowedHeight;
    private int framebufferWidth, framebufferHeight;
    private int fullscreenRefreshRate = 0;
    private boolean sizeChanged = true;
    private String title;
    private final Queue<Runnable> eventQueue = new LinkedBlockingQueue<>();

    private boolean trackMouse = false;
    private double lastMouseXPos = 0, lastMouseYPos = 0;
    private double lastMouseXMovement = 0, lastMouseYMovement = 0;
    private double currentMouseXPos = 0, currentMouseYPos = 0;
    private double currentMouseXMovement = 0, currentMouseYMovement = 0;

    private boolean keyWPressed = false;
    private boolean keyAPressed = false;
    private boolean keySPressed = false;
    private boolean keyDPressed = false;
    private boolean spacebarPressed = false;
    private boolean shiftPressed = false;

    public Window(int windowedWidth, int windowedHeight, String title) {
        this.windowedWidth = windowedWidth;
        this.windowedHeight = windowedHeight;
        this.title = title;
        init();
    }

    private void init() {
        if(!glfwInit())
            throw new IllegalStateException("GLFW could not be initialized!");

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

        handle = glfwCreateWindow(windowedWidth, windowedHeight, title, 0, 0);
        if(handle == 0)
            throw new IllegalStateException("Window could not be created!");

        glfwMakeContextCurrent(handle);
        GL.createCapabilities();
        glViewport(0, 0, 800, 600);

        glfwSetFramebufferSizeCallback(handle, this::framebufferSizeCallback);
        glfwSetKeyCallback(handle, this::keyCallback);
        glfwSetMouseButtonCallback(handle, this::mouseButtonCallback);
        glfwSetCursorPosCallback(handle, this::cursorPosCallback);

        if (glfwRawMouseMotionSupported())
            glfwSetInputMode(handle, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);

        IntBuffer framebufferWidthTemp = BufferUtils.createIntBuffer(1);
        IntBuffer framebufferHeightTemp = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(handle, framebufferWidthTemp, framebufferHeightTemp);
        framebufferSizeCallback(handle, framebufferWidth = framebufferWidthTemp.get(), framebufferHeight = framebufferHeightTemp.get());

        monitor = glfwGetPrimaryMonitor();

        // ...
    }

    public void show() {
        glfwShowWindow(handle);
    }

    public void hide() {
        glfwHideWindow(handle);
    }

    public void update() {
        glfwSwapBuffers(handle);
        glfwPollEvents();

        lastMouseXPos = currentMouseXPos;
        lastMouseYPos = currentMouseYPos;
        lastMouseXMovement = currentMouseXMovement;
        lastMouseYMovement = currentMouseYMovement;
        currentMouseXMovement = 0;
        currentMouseYMovement = 0;

        eventQueue.forEach(Runnable::run);
        eventQueue.clear();
    }

    public double getMouseXMovement() {
        return lastMouseXMovement;
    }

    public double getMouseYMovement() {
        return lastMouseYMovement;
    }

    private void makeFullScreen() {
        IntBuffer windowedXPosTemp = BufferUtils.createIntBuffer(1);
        IntBuffer windowedYPosTemp = BufferUtils.createIntBuffer(1);
        IntBuffer windowedWidthTemp = BufferUtils.createIntBuffer(1);
        IntBuffer windowedHeightTemp = BufferUtils.createIntBuffer(1);
        glfwGetWindowPos(handle, windowedXPosTemp, windowedYPosTemp);
        glfwGetWindowSize(handle, windowedWidthTemp, windowedHeightTemp);
        windowedXPos = windowedXPosTemp.get();
        windowedYPos = windowedYPosTemp.get();
        windowedWidth= windowedWidthTemp.get();
        windowedHeight = windowedHeightTemp.get();

        GLFWVidMode vidMode = glfwGetVideoMode(monitor);
        if(vidMode == null)
            throw new IllegalStateException("An error occurred while making the window fullscreen!");
        glfwSetWindowMonitor(handle, monitor, 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
        fullscreen = true;
        fullscreenRefreshRate = vidMode.refreshRate();
    }

    private void makeWindowed() {
        glfwSetWindowMonitor(handle, 0, windowedXPos, windowedYPos, windowedWidth, windowedHeight, 0);
        fullscreen = false;
        fullscreenRefreshRate = 0;
    }

    public void toggleFullscreen() {
        if(fullscreen)
            makeWindowed();
        else
            makeFullScreen();
        sizeChanged = true;
    }

    public void toggleFullscreenMonitor() {
        if(!fullscreen)
            return;
        PointerBuffer monitors = glfwGetMonitors();
        if(monitors == null)
            return;
        boolean currentMonitorFound = false;
        for(int i = 0; i < monitors.capacity(); i++) {
            if(monitor == monitors.get(i)) {
                if(i < monitors.capacity() - 1)
                    monitor = monitors.get(i + 1);
                else
                    monitor = monitors.get(0);
                currentMonitorFound = true;
                break;
            }
        }
        if(!currentMonitorFound)
            monitor = glfwGetPrimaryMonitor();
        if(fullscreen) {
            GLFWVidMode vidMode = glfwGetVideoMode(monitor);
            if(vidMode == null)
                throw new IllegalStateException("An error occurred while toggling the fullscreen monitor!");
            glfwSetWindowMonitor(handle, monitor, 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
        }
        sizeChanged = true;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(handle, title);
    }

    public int getFramebufferWidth() {
        return framebufferWidth;
    }

    public int getFramebufferHeight() {
        return framebufferHeight;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void lockSize() {
        sizeChanged = false;
    }

    public boolean hasSizeChanged() {
        return sizeChanged;
    }

    public boolean isKeyWPressed() {
        return keyWPressed;
    }

    public boolean isKeyAPressed() {
        return keyAPressed;
    }

    public boolean isKeySPressed() {
        return keySPressed;
    }

    public boolean isKeyDPressed() {
        return keyDPressed;
    }

    public boolean isSpacebarPressed() {
        return spacebarPressed;
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    private void framebufferSizeCallback(long handle, int width, int height) {
        framebufferWidth = width;
        framebufferHeight = height;
        sizeChanged = true;
        glViewport(0, 0, width, height);
    }

    private void keyCallback(long handle, int key, int scancode, int action, int mods) {
        eventQueue.add(() -> {
            if(key == GLFW_KEY_ESCAPE && trackMouse && action == GLFW_PRESS) {
                trackMouse = false;
                glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }

            if(key == GLFW_KEY_F11 && action == GLFW_PRESS)
                toggleFullscreen();
            if(key == GLFW_KEY_F12 && action == GLFW_PRESS)
                toggleFullscreenMonitor();

            switch(key) {
                case GLFW_KEY_W -> {
                    if (action == GLFW_PRESS)
                        keyWPressed = true;
                    if (action == GLFW_RELEASE)
                        keyWPressed = false;
                }
                case GLFW_KEY_A -> {
                    if (action == GLFW_PRESS)
                        keyAPressed = true;
                    if (action == GLFW_RELEASE)
                        keyAPressed = false;
                }
                case GLFW_KEY_S -> {
                    if (action == GLFW_PRESS)
                        keySPressed = true;
                    if (action == GLFW_RELEASE)
                        keySPressed = false;
                }
                case GLFW_KEY_D -> {
                    if (action == GLFW_PRESS)
                        keyDPressed = true;
                    if (action == GLFW_RELEASE)
                        keyDPressed = false;
                }
                case GLFW_KEY_SPACE -> {
                    if (action == GLFW_PRESS)
                        spacebarPressed = true;
                    if (action == GLFW_RELEASE)
                        spacebarPressed = false;
                }
                case GLFW_KEY_LEFT_SHIFT -> {
                    if (action == GLFW_PRESS)
                        shiftPressed = true;
                    if (action == GLFW_RELEASE)
                        shiftPressed = false;
                }
            }
        });
    }

    private void mouseButtonCallback(long handle, int button, int action, int mods) {
        eventQueue.add(() -> {
            if(button == GLFW_MOUSE_BUTTON_1 && !trackMouse) {
                trackMouse = true;
                glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            }
        });
    }

    private void cursorPosCallback(long handle, double xPos, double yPos) {
        if(trackMouse) {
            currentMouseXMovement = xPos - lastMouseXPos;
            currentMouseYMovement = yPos - lastMouseYPos;
        }
        currentMouseXPos = xPos;
        currentMouseYPos = yPos;
    }

}
