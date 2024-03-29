package com.lexinon.facharbeit;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11C.glViewport;

public class Window {

    private long handle;
    private long monitor;
    private boolean fullscreen = false;
    private int windowedXPos, windowedYPos;
    private int windowedWidth, windowedHeight;
    private int framebufferWidth, framebufferHeight;
    private int fullscreenRefreshRate = 0;
    private String title;
    private String gpuName = "Unknown GPU";

    private boolean trackMouse = false;
    private double lastMouseXPos = 0, lastMouseYPos = 0;
    private double lastMouseXMovement = 0, lastMouseYMovement = 0;
    private double currentMouseXPos = 0, currentMouseYPos = 0;
    private double currentMouseXMovement = 0, currentMouseYMovement = 0;
    private double lastScroll = 0, currentScroll = 0;

    private boolean keyWPressed = false;
    private boolean keyAPressed = false;
    private boolean keySPressed = false;
    private boolean keyDPressed = false;
    private boolean spacebarPressed = false;
    private boolean shiftPressed = false;
    private boolean ctrlPressed = false;
    private boolean altPressed = false;
    private boolean leftMouseButtonPressed = false;
    private boolean rightMouseButtonPressed = false;

    private boolean keyF1Clicked = false;
    private boolean keyF2Clicked = false;
    private boolean keyF3Clicked = false;

    private boolean keyF5Clicked = false;
    private boolean keyF6Clicked = false;

    private int selectedNum = -1;

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
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);

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
        glfwSetScrollCallback(handle, this::scrollCallback);
        glfwSetErrorCallback(Window::errorCallback);

        if (glfwRawMouseMotionSupported())
            glfwSetInputMode(handle, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);

        IntBuffer framebufferWidthTemp = BufferUtils.createIntBuffer(1);
        IntBuffer framebufferHeightTemp = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(handle, framebufferWidthTemp, framebufferHeightTemp);
        framebufferSizeCallback(handle, framebufferWidth = framebufferWidthTemp.get(), framebufferHeight = framebufferHeightTemp.get());

        monitor = glfwGetPrimaryMonitor();

        gpuName = glGetString(GL_RENDERER);
    }

    public void destroy() {
        GL.destroy();
        glfwDestroyWindow(handle);
    }

    public void show() {
        glfwShowWindow(handle);
    }

    public void hide() {
        glfwHideWindow(handle);
    }

    public void update() {
        keyF1Clicked = false;
        keyF2Clicked = false;
        keyF3Clicked = false;
        keyF5Clicked = false;
        keyF6Clicked = false;

        selectedNum = -1;

        Metrics.switchBenchmarkProfile(BenchmarkProfile.WAITING_FOR_GPU);
        glfwSwapBuffers(handle);
        Metrics.switchBenchmarkProfile(BenchmarkProfile.OTHER);
        glfwPollEvents();

        lastMouseXPos = currentMouseXPos;
        lastMouseYPos = currentMouseYPos;
        lastMouseXMovement = currentMouseXMovement;
        lastMouseYMovement = currentMouseYMovement;
        currentMouseXMovement = 0;
        currentMouseYMovement = 0;

        lastScroll = currentScroll;
        currentScroll = 0;
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

    public String getGpuName() {
        return gpuName;
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

    public boolean isCtrlPressed() {
        return ctrlPressed;
    }

    public boolean isAltPressed() {
        return altPressed;
    }

    public boolean isLeftMouseButtonPressed() {
        return leftMouseButtonPressed;
    }

    public boolean isRightMouseButtonPressed() {
        return rightMouseButtonPressed;
    }

    public boolean isKeyF1Clicked() {
        return keyF1Clicked;
    }

    public boolean isKeyF2Clicked() {
        return keyF2Clicked;
    }

    public boolean isKeyF3Clicked() {
        return keyF3Clicked;
    }

    public boolean isKeyF5Clicked() {
        return keyF5Clicked;
    }

    public boolean isKeyF6Clicked() {
        return keyF6Clicked;
    }

    public int getSelectedNum() {
        return selectedNum;
    }

    public double getScroll() {
        return lastScroll;
    }
    private void framebufferSizeCallback(long handle, int width, int height) {
        framebufferWidth = width;
        framebufferHeight = height;
        glViewport(0, 0, width, height);
    }

    private void keyCallback(long handle, int key, int scancode, int action, int mods) {
        if(key == GLFW_KEY_ESCAPE && trackMouse && action == GLFW_PRESS) {
            trackMouse = false;
            glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }

        if(key == GLFW_KEY_F10 && action == GLFW_PRESS)
            System.gc();
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
            case GLFW_KEY_LEFT_CONTROL -> {
                if (action == GLFW_PRESS)
                    ctrlPressed = true;
                if (action == GLFW_RELEASE)
                    ctrlPressed = false;
            }
            case GLFW_KEY_LEFT_ALT -> {
                if (action == GLFW_PRESS)
                    altPressed = true;
                if (action == GLFW_RELEASE)
                    altPressed = false;
            }
            case GLFW_KEY_F1 -> {
                if (action == GLFW_PRESS)
                    keyF1Clicked = true;
            }
            case GLFW_KEY_F2 -> {
                if (action == GLFW_PRESS)
                    keyF2Clicked = true;
            }
            case GLFW_KEY_F3 -> {
                if (action == GLFW_PRESS)
                    keyF3Clicked = true;
            }
            case GLFW_KEY_F5 -> {
                if (action == GLFW_PRESS)
                    keyF5Clicked = true;
            }
            case GLFW_KEY_F6 -> {
                if (action == GLFW_PRESS)
                    keyF6Clicked = true;
            }
            case GLFW_KEY_1 -> {
                if(action == GLFW_PRESS) {
                    selectedNum = ctrlPressed ? 9 : 0;
                }
            }
            case GLFW_KEY_2 -> {
                if(action == GLFW_PRESS) {
                    selectedNum = ctrlPressed ? 10 : 1;
                }
            }
            case GLFW_KEY_3 -> {
                if(action == GLFW_PRESS) {
                    selectedNum = ctrlPressed ? 11 : 2;
                }
            }
            case GLFW_KEY_4 -> {
                if(action == GLFW_PRESS) {
                    selectedNum = ctrlPressed ? 12 : 3;
                }
            }
            case GLFW_KEY_5 -> {
                if(action == GLFW_PRESS) {
                    selectedNum = ctrlPressed ? 13 : 4;
                }
            }
            case GLFW_KEY_6 -> {
                if(action == GLFW_PRESS) {
                    selectedNum = ctrlPressed ? 14 : 5;
                }
            }
            case GLFW_KEY_7 -> {
                if(action == GLFW_PRESS) {
                    selectedNum = ctrlPressed ? 15 : 6;
                }
            }
            case GLFW_KEY_8 -> {
                if(action == GLFW_PRESS) {
                    selectedNum = ctrlPressed ? 16 : 7;
                }
            }
            case GLFW_KEY_9 -> {
                if(action == GLFW_PRESS) {
                    selectedNum = ctrlPressed ? 17 : 8;
                }
            }
        }
    }

    private void mouseButtonCallback(long handle, int button, int action, int mods) {
        if(button == GLFW_MOUSE_BUTTON_1 && !trackMouse) {
            trackMouse = true;
            glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            return;
        }

        switch(button) {
            case GLFW_MOUSE_BUTTON_1 -> leftMouseButtonPressed = action != GLFW_RELEASE;
            case GLFW_MOUSE_BUTTON_2 -> rightMouseButtonPressed = action != GLFW_RELEASE;
        }
    }

    private void cursorPosCallback(long handle, double xPos, double yPos) {
        if(trackMouse) {
            currentMouseXMovement = xPos - lastMouseXPos;
            currentMouseYMovement = yPos - lastMouseYPos;
        }
        currentMouseXPos = xPos;
        currentMouseYPos = yPos;
    }

    private void scrollCallback(long handle, double xOffset, double yOffset) {
        currentScroll += yOffset;
    }

    private static void errorCallback(int length, long message) {
        System.err.println(MemoryUtil.memUTF8(message, length));
    }

}
