package com.lexinon.facharbeit;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20C.*;

/**
 * The {@code Game} class contains most of the game's global state.
 */
public class Game {

    private static Game game;

    private Window window;
    private Camera camera;
    private Config config;

    private VoxelShader voxelShader;
    private BoxShader boxShader;
    private ScreenShader screenShader;
    private Octree octree;
    private BoxMesh boxMesh;
    private Overlay overlay;
    private BenchmarkMode benchmarkMode = BenchmarkMode.NONE;

    private MeshBuilder meshBuilder;

    private float movementSpeed = 1f;
    private float selectionDistance = 5f;
    private Material selectedMaterial = Material.CRATE;
    private boolean hideSelectionBox = false;
    private boolean hideOverlay = true;

    private long lastTime;
    private int destroyCooldown = 0;
    private int placeCooldown = 0;
    private boolean shouldTerminate = false;

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

    /**
     * This method is executed once at the start of the application to initialize it.
     */
    private void init() {
        config = Config.read(new File("config.txt"));

        window = new Window(800, 600, "Facharbeit Projekt");
        camera = new Camera();
        camera.setEye(new Vector3f(0.5f, 1f, 1f)); // TODO
        camera.updateViewProjectionMatrix();

        voxelShader = new VoxelShader(camera);
        boxShader = new BoxShader(camera);
        screenShader = new ScreenShader(camera);

        IWorldGenerator worldGenerator = switch(config.getWorldType()) {
            case TERRAIN -> new TerrainGenerator()
                    .setWorldSize(1 << (config.getDepth() + config.getEdgeLengthExponent()))
                    .setSeed(config.getSeed()); //236
            case EMPTY -> new EmptyWorldGenerator();
            case FLAT -> new FlatWorldGenerator()
                    .setWorldSize(1 << (config.getDepth() + config.getEdgeLengthExponent()), config.getFlatWorldHeight(), 1 << (config.getDepth() + config.getEdgeLengthExponent()));
        };

        octree = worldGenerator.setCameraToTerrainHeight(0, 0, camera)
                .generate(config.getDepth(), config.getEdgeLengthExponent(), this);

        new VoxelTextureAtlas(game);
        new ScreenTextureAtlas(game);

        boxMesh = new BoxMesh();

        overlay = new Overlay(this);

        meshBuilder = new MeshBuilder(6 * (1 << octree.getLeafNodeArrayEdgeLengthExponent()) * (1 << octree.getLeafNodeArrayEdgeLengthExponent()) * (1 << octree.getLeafNodeArrayEdgeLengthExponent()));

        glEnable(GL_POLYGON_SMOOTH);
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_CULL_FACE);

        window.show();
    }

    /**
     * This method is run periodically during the application's runtime.
     */
    private void tick() {
        Metrics.tick();

        // Handle benchmark keys
        if(window.isKeyF5Clicked())
            switch(benchmarkMode) {
                case NONE, WAITING_UNLIMITED -> {
                    benchmarkMode = BenchmarkMode.WAITING_LIMITED;
                }
                case WAITING_LIMITED -> {
                    Metrics.startBenchmark(window, config, config.getBenchmarkDuration());
                    benchmarkMode = BenchmarkMode.ACTIVE_LIMITED;
                }
                case ACTIVE_LIMITED, ACTIVE_UNLIMITED -> {
                    Metrics.endBenchmark();
                    benchmarkMode = BenchmarkMode.NONE;
                }
            }
        if(window.isKeyF6Clicked())
            switch(benchmarkMode) {
                case NONE, WAITING_LIMITED -> {
                    benchmarkMode = BenchmarkMode.WAITING_UNLIMITED;
                }
                case WAITING_UNLIMITED -> {
                    Metrics.startBenchmark(window, config);
                    benchmarkMode = BenchmarkMode.ACTIVE_UNLIMITED;
                }
                case ACTIVE_LIMITED, ACTIVE_UNLIMITED -> {
                    Metrics.endBenchmark();
                    benchmarkMode = BenchmarkMode.NONE;
                }
            }

        if(!Metrics.isBenchmarkRunning() && (benchmarkMode == BenchmarkMode.ACTIVE_LIMITED || benchmarkMode == BenchmarkMode.ACTIVE_UNLIMITED))
            benchmarkMode = BenchmarkMode.NONE;

        // Calculate delta so that the application always runs at the same speed
        long currentTime = System.nanoTime();
        long actualDelta = currentTime - lastTime;
        long delta = Math.min(actualDelta, 1_000_000_000);
        lastTime = currentTime;

        Metrics.frameTime(delta);

        // Handle camera movement
        float yaw = camera.getYaw();
        float pitch = camera.getPitch();
        yaw += window.getMouseXMovement() / 500f;
        pitch -= window.getMouseYMovement() / 500f;
        pitch = (float) Math.max(pitch, -0.5 * Math.PI);
        pitch = (float) Math.min(pitch, 0.5 * Math.PI);
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

        camera.setAspectRatio((float) window.getFramebufferWidth() / window.getFramebufferHeight());

        camera.updateViewProjectionMatrix();

        // Determine voxel position selected by the player
        Vector3f lookingAt = new Vector3f(camera.getEye()).add(new Vector3f((float) (Math.sin(yaw) * Math.cos(pitch)), (float) Math.sin(pitch), -(float) (Math.cos(yaw) * Math.cos(pitch))).normalize().mul(selectionDistance));
        Vector3i selectedVoxel = new Vector3i((int) Math.floor(lookingAt.x), (int) Math.floor(lookingAt.y), (int) Math.floor(lookingAt.z));

        // Handle input of the mouse buttons to add (place) or remove (destroy) a voxel with a cooldown
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
            activateBenchmarkIfWaiting();
        }
        if(placeCooldown == 0 && window.isRightMouseButtonPressed()) {
            octree.addVoxel(selectedVoxel, selectedMaterial.getId());
            placeCooldown = 300_000_000;
            activateBenchmarkIfWaiting();
        }

        if(!window.isLeftMouseButtonPressed())
            destroyCooldown = 0;
        if(!window.isRightMouseButtonPressed())
            placeCooldown = 0;

        // Handle scroll input
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

        // Determine material currently selected by the player to be placed later
        selectedMaterial = switch(window.getSelectedNum()) {
            case 0 -> Material.CRATE;
            case 1 -> Material.GRASS;
            case 2 -> Material.DIRT;
            case 3 -> Material.STONE;
            case 4 -> Material.LOG;
            case 5 -> Material.LEAVES;
            case 6 -> Material.WATER;
            case 7 -> Material.SAND;
            case 8 -> Material.FARMLAND;
            case 9 -> Material.HAY;
            case 10 -> Material.CACTUS;
            case 11 -> Material.CAKE;
            case 12 -> Material.PUMPKIN;
            case 13 -> Material.VINES;
            case 14 -> Material.TANK;
            case 15 -> Material.PLANKS;
            case 16 -> Material.BRICKS;
            case 17 -> Material.RED_BRICKS;
            default -> selectedMaterial;
        };

        // Update all meshes modified in this frame
        octree.updateMeshs();

        // Show / hide selection box and overlay
        if(window.isKeyF1Clicked())
            hideSelectionBox = !hideSelectionBox;
        if(window.isKeyF3Clicked())
            hideOverlay = !hideOverlay;

        // Draw sky
        glClearColor(0.74609375f, 0.9140625f, 0.95703125f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Draw voxels
        glEnable(GL_DEPTH_TEST);
        octree.render();

        // Draw selection box
        if(!hideSelectionBox)
            boxMesh.draw(selectedVoxel, this);

        // Draw overlay
        glDisable(GL_DEPTH_TEST);
        if(!hideOverlay)
            overlay.draw();

        // Show updated content of framebuffer and poll events
        window.update();

        // Take a screenshot if requested
        if(window.isKeyF2Clicked())
            takeScreenshot();

        // Check whether the user requests to close the window
        if(window.shouldClose())
            shouldTerminate = true;
    }

    /**
     * This method is called before terminating the application to release resources
     */
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

    private void takeScreenshot() {
        ByteBuffer buffer = BufferUtils.createByteBuffer(3 * window.getFramebufferWidth() * window.getFramebufferHeight());
        glReadPixels(0, 0, window.getFramebufferWidth(), window.getFramebufferHeight(), GL_RGB, GL_UNSIGNED_BYTE, buffer);
        BufferedImage image = new BufferedImage(window.getFramebufferWidth(), window.getFramebufferHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < window.getFramebufferWidth() * window.getFramebufferHeight(); i++) {
            image.setRGB(i % window.getFramebufferWidth(), window.getFramebufferHeight() - 1 - i / window.getFramebufferWidth(),
                    ((((int) buffer.get(3 * i)) << 16) & 0x00FF0000) + (((((int) buffer.get(3 * i + 1)) << 8) & 0x0000FF00) + (((int) buffer.get(3 * i + 2)) & 0x000000FF)) + 0xFF000000);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss");
        Date now = new Date();
        try {
            new File("screenshots").mkdir();
            ImageIO.write(image, "png", new File(String.format("screenshots/Screenshot-%s.png", formatter.format(now))));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Config getConfig() {
        return config;
    }

    public Material getSelectedMaterial() {
        return selectedMaterial;
    }

    public void activateBenchmarkIfWaiting() {
        switch(benchmarkMode) {
            case WAITING_LIMITED -> {
                Metrics.startBenchmark(window, config, config.getBenchmarkDuration());
                benchmarkMode = BenchmarkMode.ACTIVE_LIMITED;
            }
            case WAITING_UNLIMITED -> {
                Metrics.startBenchmark(window, config);
                benchmarkMode = BenchmarkMode.ACTIVE_UNLIMITED;
            }
        }
    }

    public BenchmarkMode getBenchmarkMode() {
        return benchmarkMode;
    }

    public VoxelShader getVoxelShader() {
        return voxelShader;
    }

    public BoxShader getBoxShader() {
        return boxShader;
    }

    public ScreenShader getScreenShader() {
        return screenShader;
    }

}
