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

    private long maxAmountOfVoxels;

    public Overlay(Game game) {
        mesh = new ScreenMesh();
        this.game = game;
        this.shader = game.screenShader;
        this.window = game.getWindow();
        long worldEdgeLength = 1L << (game.getConfig().getDepth() + game.getConfig().getEdgeLengthExponent());
        maxAmountOfVoxels = worldEdgeLength * worldEdgeLength * worldEdgeLength;
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
        writeLineRt(String.format("Resolution:%dx%d", window.getFramebufferWidth(), window.getFramebufferHeight()));
        writeLineRt(String.format("GPU:%s", window.getGpuName()));
        writeLineRt(String.format("Fps:%s", (int) Metrics.getFps()));

        writeLineLt("#OF");
        writeLineLt("Inner nodes=" + Metrics.getNumInnerOctreeNodes());
        writeLineLt("Empty leaf nodes=" + Metrics.getNumEmptyOctreeLeafNodes());
        writeLineLt("Voxel arrays/Non-empty leaf nodes=" + Metrics.getNumVoxelArrays());
        writeLineLt("Non-empty voxels=" + Metrics.getNumNonEmptyVoxels());
        writeLineLt("Triangles=" + Metrics.getNumTriangles());
        writeLineLt("");
        writeLineLt("INSTANCE SIZE OF");
        writeLineLt("InnerOctreeNode=" + Metrics.INNER_OCTREE_NODE_INSTANCE_SIZE);
        writeLineLt("EmptyOctreeLeafNode=" + Metrics.EMPTY_OCTREE_LEAF_NODE_INSTANCE_SIZE);
        writeLineLt("NonEmptyOctreeLeafNode=" + Metrics.NON_EMPTY_OCTREE_LEAF_NODE_INSTANCE_SIZE);
        writeLineLt("Mesh=" + Metrics.MESH_INSTANCE_SIZE);
        writeLineLt("");
        writeLineLt("CONFIGURATION");
        writeLineLt("Octree depth=" + game.getConfig().getDepth());
        writeLineLt("Voxel array edge length=" + (1 << game.getConfig().getEdgeLengthExponent()));
        writeLineLt("=>Voxel array volume=" + (1 << (3 * game.getConfig().getEdgeLengthExponent())));
        writeLineLt("=>Maximal amount of voxels=" + maxAmountOfVoxels);
        writeLineLt("Occlusion test=" + (game.getConfig().doOcclusionTest() ? "on" : "off"));
        writeLineLt("World type=" + switch(game.getConfig().getWorldType()) {
            case TERRAIN -> "Terrain (seed=" + game.getConfig().getSeed() + ")";
            case EMPTY -> "Empty";
            case FLAT -> "Flat";
        });

        writeLineRb("https://github.com/LexinonCraft/facharbeit-projekt");
        writeLineRb("(C) Tiggemann 2023");

        writeLineLb("Selected Material:" + game.getSelectedMaterial().getName());
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

    public void deleteMesh() {
        mesh.delete();
    }

}
