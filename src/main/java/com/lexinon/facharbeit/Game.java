package com.lexinon.facharbeit;

import static org.lwjgl.opengl.GL11C.*;

public class Game {

    private static Game game;

    private Window window;

    private boolean terminate = false;

    public static Game get() {
        return game;
    }

    public static void run() {
        if(game != null)
            throw new IllegalStateException("Game already runs!");
        game = new Game();
        game.init();
        while(!game.terminate) {
            game.tick();
        }
    }

    private void init() {
        window = new Window(800, 600, "Facharbeit Projekt");
        window.show();
    }

    private void tick() {
        glClearColor(0.74609375f, 0.9140625f, 0.95703125f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        window.update();
        if(window.shouldClose())
            terminate = true;
    }

}
