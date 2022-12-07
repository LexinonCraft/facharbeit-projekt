package com.lexinonc.voxeleditor;

import com.lexinonc.voxeleditor.window.Window;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

    public static void main(String[] args) {
        if(!glfwInit())
            throw new IllegalStateException("GLFW couldn't be initialized!");
        glfwSetErrorCallback(Main::glfwErrorCallback);

        Window window = new Window(800, 600, "Hiiiiiiiiii");
        window.setVisible(true);

        while(!window.isCloseRequested()) {
            window.refresh();
        }

        glfwTerminate();

        /*glfwInit();

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RED_BITS, mode.redBits());
        glfwWindowHint(GLFW_RED_BITS, mode.greenBits());
        glfwWindowHint(GLFW_RED_BITS, mode.blueBits());
        glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate());
        glfwWindowHint(GLFW_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        long window = glfwCreateWindow(mode.width(), mode.height(), "Hello World", 0, 0);

        glfwSetKeyCallback(window, (long window_, int key, int scancode, int action, int mods) -> {
            if(action != GLFW_PRESS)
                return;

            switch(key) {
                case GLFW_KEY_Z:
                    double[] xPos = {0.};
                    double[] yPos = {0.};
                    glfwGetCursorPos(window, xPos, yPos);
                    System.out.println(xPos[0] + " " + yPos[0]);
                    break;
                case GLFW_KEY_M:
                    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                    glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_FALSE);
                    break;
                case GLFW_KEY_N:
                    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                    glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
                    break;
            }
        });

        while(!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            glfwSwapBuffers(window);
        }*/
    }

    private static void glfwErrorCallback(int errorCode, long messagePointer) {
        String message = MemoryUtil.memUTF8(messagePointer);
        throw new IllegalStateException(String.format("GLFW error with code %d: %s", errorCode, message));
    }

}
