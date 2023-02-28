package com.lexinon.facharbeit;

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
