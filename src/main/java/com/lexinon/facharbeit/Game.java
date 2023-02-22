package com.lexinon.facharbeit;

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
        window.update();
        if(window.shouldClose())
            terminate = true;
    }

}
