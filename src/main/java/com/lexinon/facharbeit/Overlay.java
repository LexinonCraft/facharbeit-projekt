package com.lexinon.facharbeit;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Overlay {

    private final ScreenMesh mesh;
    private final Game game;
    private final ScreenShader shader;
    private final Window window;
    private final List<ScreenObject> screenObjectList = new ArrayList<>();

    private int ltCurrentPosition, rtCurrentPosition, lbCurrentPosition, rbCurrentPosition;

    public Overlay(Game game) {
        mesh = new ScreenMesh();
        this.game = game;
        this.shader = game.screenShader;
        this.window = game.getWindow();
    }

    public void draw() {
        ltCurrentPosition = window.getFramebufferHeight() - ScreenShader.CHARACTER_SIZE;
        rtCurrentPosition = window.getFramebufferHeight() - ScreenShader.CHARACTER_SIZE;
        lbCurrentPosition = 0;
        rbCurrentPosition = 0;
        screenObjectList.clear();
        writeOverlayText();

        mesh.display(screenObjectList, window);
        mesh.draw(window.getFramebufferWidth(), window.getFramebufferHeight(), shader);
    }

    private void writeOverlayText() {
        writeLineRt("Facharbeitsprojekt");
        writeLineRt(String.format("Position:(%d,%d,%d)", (int) game.getCamera().getEye().x, (int) game.getCamera().getEye().y, (int) game.getCamera().getEye().z));
    }

    private void writeLineLt(String text) {
        for(int i = 0; i < text.length(); i++) {
            screenObjectList.add(new ScreenObject(new Vector2f(i * ScreenShader.CHARACTER_SIZE, ltCurrentPosition), Character.getTexture(text.charAt(i))));
        }
        ltCurrentPosition -= ScreenShader.CHARACTER_SIZE;
    }

    private void writeLineRt(String text) {
        for(int i = 0; i < text.length(); i++) {
            screenObjectList.add(new ScreenObject(new Vector2f(window.getFramebufferWidth() - text.length() * ScreenShader.CHARACTER_SIZE + i * ScreenShader.CHARACTER_SIZE, rtCurrentPosition), Character.getTexture(text.charAt(i))));
        }
        rtCurrentPosition -= ScreenShader.CHARACTER_SIZE;
    }

    private void writeLineLb(String text) {
        for(int i = 0; i < text.length(); i++) {
            screenObjectList.add(new ScreenObject(new Vector2f(i * ScreenShader.CHARACTER_SIZE, lbCurrentPosition), Character.getTexture(text.charAt(i))));
        }
        lbCurrentPosition += ScreenShader.CHARACTER_SIZE;
    }

    private void writeLineRb(String text) {
        for(int i = 0; i < text.length(); i++) {
            screenObjectList.add(new ScreenObject(new Vector2f(window.getFramebufferWidth() - text.length() * ScreenShader.CHARACTER_SIZE + i * ScreenShader.CHARACTER_SIZE, rbCurrentPosition), Character.getTexture(text.charAt(i))));
        }
        rbCurrentPosition += ScreenShader.CHARACTER_SIZE;
    }

}
