package com.lexinon.facharbeit;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        GameWindow window = GameWindow.getOrCreate();
        window.show();
        while(!window.shouldClose()) {
            window.update();
        }
    }

}
