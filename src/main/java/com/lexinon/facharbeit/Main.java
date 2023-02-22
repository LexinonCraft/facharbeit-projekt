package com.lexinon.facharbeit;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Window window = new Window(800, 600, "GNU Blockman");
        window.show();
        while(!window.shouldClose()) {
            window.update();
        }
    }

}
