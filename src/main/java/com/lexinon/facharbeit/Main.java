package com.lexinon.facharbeit;

/**
 * In the beginning there was the {@code Main} class.
 */
public class Main {

    public static void main(String[] args) {
        try {
            Game.run();
        } catch(OutOfMemoryError e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
