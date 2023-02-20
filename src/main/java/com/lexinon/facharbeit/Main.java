package com.lexinon.facharbeit;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        /*System.out.println("Hello World!");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame();
        frame.setTitle("Hello World!");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JButton button = new JButton("test");
        button.setSize(200, 100);
        frame.add(button);*/

        Window window = Window.getOrCreate();
        window.show();
        while(!window.shouldClose()) {
            window.update();
        }
    }

}
