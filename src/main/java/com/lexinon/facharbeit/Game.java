package com.lexinon.facharbeit;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

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
    private BoxMesh boxMesh;
    private ScreenMesh screenMesh;

    private MeshBuilder meshBuilder;

    private float movementSpeed = 1f;
    private float selectionDistance = 5f;

    private long lastTime;
    private int destroyCooldown = 0;
    private int placeCooldown = 0;
    private boolean terminate = false;

    public static int testCounter = 0;

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
        camera = new Camera();
        camera.setEye(new Vector3f(0.5f, 1f, 1f));
        //camera.setEye(new Vector3f(0f, 0f, 2f));
        //camera.setYaw(-0.25f * (float) Math.PI);
        camera.updateViewProjectionMatrix();

        voxelShader = new VoxelShader();
        boxShader = new BoxShader();
        //screenShader = new ScreenShader();

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

        for(int x = -512; x < 512; x++) {
            for(int y = 0; y < 2; y++) {
                for (int z = -512; z < 512; z++) {
                    //if(x == 0 && y == 0 && z == 0)
                    //    continue;
                    octree.addVoxel(new Vector3i(x, y, z), Material.TEST_STONE.getId());
                }
            }
        }

        for(int x = -512; x < 512; x++) {
            for(int y = 2; y < 3; y++) {
                for (int z = -512; z < 512; z++) {
                    //if(x == 0 && y == 0 && z == 0)
                    //    continue;
                    octree.addVoxel(new Vector3i(x, y, z), Material.GRASS.getId());
                }
            }
        }

        octree.addVoxel(new Vector3i(25, 3, 17), Material.LOG.getId());
        octree.addVoxel(new Vector3i(25, 4, 17), Material.LOG.getId());
        octree.addVoxel(new Vector3i(25, 5, 17), Material.LOG.getId());
        octree.addVoxel(new Vector3i(25, 6, 17), Material.LOG.getId());
        octree.addVoxel(new Vector3i(24, 6, 17), Material.LEAVES.getId());
        octree.addVoxel(new Vector3i(26, 6, 17), Material.LEAVES.getId());
        octree.addVoxel(new Vector3i(25, 6, 16), Material.LEAVES.getId());
        octree.addVoxel(new Vector3i(25, 6, 18), Material.LEAVES.getId());
        octree.addVoxel(new Vector3i(25, 7, 17), Material.LEAVES.getId());

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

        voxelTextureAtlas = new VoxelTextureAtlas();
        voxelTextureAtlas.activate(this);

        //screenTextureAtlas = new ScreenTextureAtlas();
        //screenTextureAtlas.activate(this);

        boxMesh = new BoxMesh();

        meshBuilder = new MeshBuilder(6 * (1 << octree.getEdgeLengthExponent()) * (1 << octree.getEdgeLengthExponent()) * (1 << octree.getEdgeLengthExponent()));

        glEnable(GL_DEPTH_TEST);

        glEnable(GL_POLYGON_SMOOTH);

        glEnable(GL_MULTISAMPLE);

        glEnable(GL_CULL_FACE);

        //screenMesh = new ScreenMesh();
        //List<ScreenObject> screenObjectList = new ArrayList<>();
        //screenObjectList.add(new ScreenObject(new Vector2f(0f, 0f), new Vector2f(0f, 0f)));
        //screenMesh.display(screenObjectList);

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
                (window.isSpacebarPressed() ? 1 : 0) - (window.isShiftPressed() ? 1 : 0),
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
                movementSpeed = Math.min(movementSpeed, 5);
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

        glClearColor(0.74609375f, 0.9140625f, 0.95703125f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        octree.render();

        //boxMesh.draw(selectedVoxel, this);
        //screenMesh.draw(window.getFramebufferWidth(), window.getFramebufferHeight(), this);

        window.update();

        window.setTitle(String.format("Facharbeit Projekt, Position = (%d,%d,%d)", (int) camera.getEye().x, (int) camera.getEye().y, (int) camera.getEye().z));

        if(window.shouldClose())
            terminate = true;

        System.gc();
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
}
