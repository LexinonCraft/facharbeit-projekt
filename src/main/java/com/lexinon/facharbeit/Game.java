package com.lexinon.facharbeit;

import org.joml.Vector3f;
import org.joml.Vector3i;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20C.*;

public class Game {

    private static Game game;

    private Window window;
    public Camera camera;
    private VoxelTextureAtlas voxelTextureAtlas;
    private ScreenTextureAtlas screenTextureAtlas;

    public VoxelShader voxelShader;
    public BoxShader boxShader;
    public ScreenShader screenShader;
    private Octree octree;
    private PerlinNoiseGenerator noiseGenerator;
    private BoxMesh boxMesh;
    private Overlay overlay;

    private MeshBuilder meshBuilder;

    private float movementSpeed = 1f;
    private float selectionDistance = 5f;
    private boolean hideOverlay = false;

    private long lastTime;
    private int destroyCooldown = 0;
    private int placeCooldown = 0;
    private boolean shouldTerminate = false;

    public static int testCounter = 0;

    public static Game get() {
        return game;
    }

    public static void run() {
        if(game != null)
            throw new IllegalStateException("Game already runs!");
        game = new Game();
        game.init();
        while(!game.shouldTerminate) {
            game.tick();
        }
        game.terminate();
    }

    private void init() {
        window = new Window(800, 600, "Facharbeit Projekt");
        camera = new Camera();
        camera.setEye(new Vector3f(0.5f, 1f, 1f));
        //camera.setEye(new Vector3f(0f, 0f, 2f));
        //camera.setYaw(-0.25f * (float) Math.PI);
        camera.updateViewProjectionMatrix();

        voxelShader = new VoxelShader(camera);
        boxShader = new BoxShader(camera);
        screenShader = new ScreenShader(camera);

        /*int testArray = glGenVertexArrays();
        glBindVertexArray(testArray);

        int testBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, testBuffer);

        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(84);
        floatBuffer.put(-0.5f).put(0f).put(0.5f).put(1f).put(0f).put(0f).put(1f)
                        .put(0.5f).put(0f).put(0.5f).put(1f).put(0f).put(0f).put(1f)
                        .put(0f).put(0.5f).put(0f).put(1f).put(0f).put(0f).put(1f)

                        .put(0.5f).put(0f).put(0.5f).put(0f).put(1f).put(0f).put(1f)
                        .put(0f).put(0f).put(-0.5f).put(0f).put(1f).put(0f).put(1f)
                        .put(0f).put(0.5f).put(0f).put(0f).put(1f).put(0f).put(1f)

                        .put(0f).put(0f).put(-0.5f).put(0f).put(0f).put(1f).put(1f)
                        .put(-0.5f).put(0f).put(0.5f).put(0f).put(0f).put(1f).put(1f)
                        .put(0f).put(0.5f).put(0f).put(0f).put(0f).put(1f).put(1f)

                        .put(-0.5f).put(0f).put(0.5f).put(1f).put(0f).put(1f).put(1f)
                        .put(0.5f).put(0f).put(0.5f).put(1f).put(1f).put(0f).put(1f)
                        .put(0f).put(0f).put(-0.5f).put(0f).put(1f).put(1f).put(1f).flip();
        glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW);

        glBindVertexArray(testArray);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 28, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 28, 12);
        glEnableVertexAttribArray(1);*/

        /*MeshBuilder meshBuilder = new MeshBuilder(24);
        meshBuilder.addFace(new Vector3i(0, 0, 0), Direction.UP, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(0, 0, 0), Direction.DOWN, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(0, 0, 0), Direction.NORTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(0, 0, 0), Direction.EAST, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(0, 0, 0), Direction.SOUTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(0, 0, 0), Direction.WEST, new Vector2f(9, 17).div(32));

        meshBuilder.addFace(new Vector3i(1, 0, 0), Direction.UP, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(1, 0, 0), Direction.DOWN, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(1, 0, 0), Direction.NORTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(1, 0, 0), Direction.EAST, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(1, 0, 0), Direction.SOUTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(1, 0, 0), Direction.WEST, new Vector2f(9, 17).div(32));

        meshBuilder.addFace(new Vector3i(1, 1, 0), Direction.UP, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(1, 1, 0), Direction.DOWN, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(1, 1, 0), Direction.NORTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(1, 1, 0), Direction.EAST, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(1, 1, 0), Direction.SOUTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3i(1, 1, 0), Direction.WEST, new Vector2f(9, 17).div(32));

        meshBuilder.addFace(new Vector3i(1, -5, 3), Direction.UP, new Vector2f(6, 18).div(32));
        meshBuilder.addFace(new Vector3i(1, -5, 3), Direction.DOWN, new Vector2f(2, 1).div(32));
        meshBuilder.addFace(new Vector3i(1, -5, 3), Direction.NORTH, new Vector2f(2, 0).div(32));
        meshBuilder.addFace(new Vector3i(1, -5, 3), Direction.EAST, new Vector2f(2, 0).div(32));
        meshBuilder.addFace(new Vector3i(1, -5, 3), Direction.SOUTH, new Vector2f(2, 0).div(32));
        meshBuilder.addFace(new Vector3i(1, -5, 3), Direction.WEST, new Vector2f(2, 0).div(32));

        testMesh = meshBuilder.build();*/

        octree = new Octree(6, 4, this);
        //octree.addVoxel(new Vector3i(0, 0, 0), Material.CRATE.getId());
        //octree.addVoxel(new Vector3i(1, 0, 0), Material.CRATE.getId());
        //octree.addVoxel(new Vector3i(1, 1, 0), Material.CRATE.getId());

        //octree.addVoxel(new Vector3i(1, -5, 3), Material.GRASS.getId());

        /*for(int x = -64; x < 64; x++) {
            for(int y = 0; y < 2; y++) {
                for (int z = -64; z < 64; z++) {
                    //if(x == 0 && y == 0 && z == 0)
                    //    continue;
                    octree.addVoxel(new Vector3i(x, y, z), Material.TEST_STONE.getId());
                }
            }
        }

        for(int x = -64; x < 64; x++) {
            for(int y = 2; y < 3; y++) {
                for (int z = -64; z < 64; z++) {
                    //if(x == 0 && y == 0 && z == 0)
                    //    continue;
                    octree.addVoxel(new Vector3i(x, y, z), Material.GRASS.getId());
                }
            }
        }*/

        //octree.addVoxel(new Vector3i(25, 3, 17), Material.LOG.getId());
        //octree.addVoxel(new Vector3i(25, 4, 17), Material.LOG.getId());
        //octree.addVoxel(new Vector3i(25, 5, 17), Material.LOG.getId());
        //octree.addVoxel(new Vector3i(25, 6, 17), Material.LOG.getId());
        //octree.addVoxel(new Vector3i(24, 6, 17), Material.LEAVES.getId());
        //octree.addVoxel(new Vector3i(26, 6, 17), Material.LEAVES.getId());
        //octree.addVoxel(new Vector3i(25, 6, 16), Material.LEAVES.getId());
        //octree.addVoxel(new Vector3i(25, 6, 18), Material.LEAVES.getId());
        //octree.addVoxel(new Vector3i(25, 7, 17), Material.LEAVES.getId());

        /*for(int x = 0; x < 20; x++) {
            for(int y = 0; y < 20; y++) {
                for (int z = 0; z < 20; z++) {
                    //if(x == 0 && y == 0 && z == 0)
                    //    continue;
                    octree.addVoxel(new Vector3i(x, y, z), Material.GRASS.getId());
                }
            }
        }*/

        //octree.removeVoxel(new Vector3i(0, 0, 0));

        noiseGenerator = new PerlinNoiseGenerator(1, 512);
        noiseGenerator.noise(8f, 3f)
                .noise(32f, 1f)
                .noise(14f, 2f);

        noiseGenerator.normalizeTexture();
        float[] heightMap = noiseGenerator.getTexture();
        for(int i = 0; i < heightMap.length; i++) {
            int terrainHeight = (int) (heightMap[i] * 100);
            int x = noiseGenerator.getXPosByIndex(i, 512);
            int z = noiseGenerator.getYPosByIndex(i, 512);
            if(terrainHeight < 0)
                for (int y = 0; y > terrainHeight; y--) {
                    octree.addVoxel(new Vector3i(x, y, z), Material.WATER.getId());
                }
            octree.addVoxel(new Vector3i(x, terrainHeight, z), Material.GRASS.getId());
            for(int y = terrainHeight - 1; y >= -100 && y >= terrainHeight - 3; y--)
                octree.addVoxel(new Vector3i(x, y, z), Material.DIRT.getId());
            for(int y = terrainHeight - 3; y >= -100; y--)
                octree.addVoxel(new Vector3i(x, y, z), Material.TEST_STONE.getId());
        }

        voxelTextureAtlas = new VoxelTextureAtlas(game);
        screenTextureAtlas = new ScreenTextureAtlas(game);

        boxMesh = new BoxMesh();

        overlay = new Overlay(this);

        meshBuilder = new MeshBuilder(6 * (1 << octree.getEdgeLengthExponent()) * (1 << octree.getEdgeLengthExponent()) * (1 << octree.getEdgeLengthExponent()));

        glEnable(GL_POLYGON_SMOOTH);
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_CULL_FACE);

        window.show();
    }

    private void tick() {
        long currentTime = System.nanoTime();
        long actualDelta = currentTime - lastTime;
        long delta = Math.min(actualDelta, 1_000_000_000);
        lastTime = currentTime;

        float yaw = camera.getYaw();
        float pitch = camera.getPitch();
        yaw += window.getMouseXMovement() / 500f;
        pitch -= window.getMouseYMovement() / 500f;
        pitch = (float) Math.max(pitch, -0.5 * Math.PI);
        pitch = (float) Math.min(pitch, 0.5 * Math.PI);
        //System.out.println(pitch);
        camera.setYaw(yaw);
        camera.setPitch(pitch);

        camera.getEye().add(new Vector3f((window.isKeyWPressed() ? movementSpeed * (float) Math.sin(yaw) : 0)
                    - (window.isKeySPressed() ? movementSpeed * (float) Math.sin(yaw) : 0)
                    + (window.isKeyDPressed() ? movementSpeed * (float) Math.cos(yaw) : 0)
                    - (window.isKeyAPressed() ? movementSpeed * (float) Math.cos(yaw) : 0),
                (window.isSpacebarPressed() ? movementSpeed : 0) - (window.isShiftPressed() ? movementSpeed : 0),
                (window.isKeySPressed() ? movementSpeed * (float) Math.cos(-yaw) : 0)
                        - (window.isKeyWPressed() ? movementSpeed * (float) Math.cos(-yaw) : 0)
                        + (window.isKeyAPressed() ? movementSpeed * (float) Math.sin(-yaw) : 0)
                        - (window.isKeyDPressed() ? movementSpeed * (float) Math.sin(-yaw) : 0)
                ).mul(delta / 1_000_000_000f * 5));

        Vector3f lookingAt = new Vector3f(camera.getEye()).add(new Vector3f((float) (Math.sin(yaw) * Math.cos(pitch)), (float) Math.sin(pitch), -(float) (Math.cos(yaw) * Math.cos(pitch))).normalize().mul(selectionDistance));
        Vector3i selectedVoxel = new Vector3i((int) lookingAt.x, (int) lookingAt.y, (int) lookingAt.z);

        if(destroyCooldown > delta)
            destroyCooldown -= delta;
        else
            destroyCooldown = 0;
        if(placeCooldown > delta)
            placeCooldown -= delta;
        else
            placeCooldown = 0;

        if(destroyCooldown == 0 && window.isLeftMouseButtonPressed()) {
            octree.removeVoxel(selectedVoxel);
            destroyCooldown = 300_000_000;
        }
        if(placeCooldown == 0 && window.isRightMouseButtonPressed()) {
            octree.addVoxel(selectedVoxel, Material.CRATE.getId());
            placeCooldown = 300_000_000;
        }

        if(!window.isLeftMouseButtonPressed())
            destroyCooldown = 0;
        if(!window.isRightMouseButtonPressed())
            placeCooldown = 0;

        double scroll = window.getScroll();
        if(window.isCtrlPressed()) {
            if(window.isAltPressed()) {
                if(scroll != 0)
                    System.exit(-1); // ( ͡° ͜ʖ ͡°)
            } else {
                movementSpeed += (float) scroll * 0.25;
                movementSpeed = Math.min(movementSpeed, 20);
                movementSpeed = Math.max(movementSpeed, 0);
            }
        } else {
            if(window.isAltPressed()) {
                float fov = camera.getFov() - (float) scroll * 5;
                fov = Math.min(fov, 179);
                fov = Math.max(fov, 1);
                camera.setFov(fov);
            } else {
                selectionDistance += (float) scroll * 0.5;
                selectionDistance = Math.min(selectionDistance, 15);
                selectionDistance = Math.max(selectionDistance, 0);
            }
        }

        camera.setAspectRatio((float) window.getFramebufferWidth() / window.getFramebufferHeight());

        camera.updateViewProjectionMatrix();

        octree.updateMeshs();

        if(window.isKeyF1Clicked())
            hideOverlay = !hideOverlay;

        glClearColor(0.74609375f, 0.9140625f, 0.95703125f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        octree.render();

        if(!hideOverlay)
            boxMesh.draw(selectedVoxel, this);

        glDisable(GL_DEPTH_TEST);
        /*screenMesh = new ScreenMesh();
        List<ScreenObject> screenObjectList = new ArrayList<>();
        screenObjectList.add(new ScreenObject(new Vector2f(0f * ScreenShader.CHARACTER_SIZE, 0f * ScreenShader.CHARACTER_SIZE), new Vector2f(8f, 2f)));
        screenObjectList.add(new ScreenObject(new Vector2f(1f * ScreenShader.CHARACTER_SIZE, 0f * ScreenShader.CHARACTER_SIZE), new Vector2f(1f, 4f)));
        screenObjectList.add(new ScreenObject(new Vector2f(2f * ScreenShader.CHARACTER_SIZE, 0f * ScreenShader.CHARACTER_SIZE), new Vector2f(12f, 4f)));
        screenObjectList.add(new ScreenObject(new Vector2f(3f * ScreenShader.CHARACTER_SIZE, 0f * ScreenShader.CHARACTER_SIZE), new Vector2f(12f, 4f)));
        screenObjectList.add(new ScreenObject(new Vector2f(4f * ScreenShader.CHARACTER_SIZE, 0f * ScreenShader.CHARACTER_SIZE), new Vector2f(15f, 4f)));

        screenObjectList.add(new ScreenObject(new Vector2f(window.getFramebufferWidth() - 4f * ScreenShader.CHARACTER_SIZE, 0f), new Vector2f(7f, 3f)));
        screenObjectList.add(new ScreenObject(new Vector2f(window.getFramebufferWidth() - 3f * ScreenShader.CHARACTER_SIZE, 0f), new Vector2f(5f, 4f)));
        screenObjectList.add(new ScreenObject(new Vector2f(window.getFramebufferWidth() - 2f * ScreenShader.CHARACTER_SIZE, 0f), new Vector2f(12f, 4f)));
        screenObjectList.add(new ScreenObject(new Vector2f(window.getFramebufferWidth() - 1f * ScreenShader.CHARACTER_SIZE, 0f), new Vector2f(4f, 5f)));
        screenMesh.display(screenObjectList, window);
        screenMesh.draw(window.getFramebufferWidth(), window.getFramebufferHeight(), this);*/
        if(!hideOverlay)
            overlay.draw();

        window.update();

        if(window.shouldClose())
            shouldTerminate = true;
    }

    public void terminate() {
        window.hide();
        octree.deleteEverything();
        boxMesh.delete();
        overlay.deleteMesh();
        window.destroy();
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public MeshBuilder getMeshBuilder() {
        return meshBuilder;
    }

    public Window getWindow() {
        return window;
    }

}
