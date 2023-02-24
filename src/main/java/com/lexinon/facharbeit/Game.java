package com.lexinon.facharbeit;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Scanner;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;

public class Game {

    private static Game game;

    private Window window;
    private Camera camera;
    private TextureAtlas textureAtlas;

    private Mesh testMesh;
    public int testShader;

    private int matrixLoc;
    private FloatBuffer cameraMatrix;

    private long lastTime;
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
        camera = new Camera();
        camera.setEye(new Vector3f(0.5f, 1f, 1f));
        //camera.setEye(new Vector3f(0f, 0f, 2f));
        //camera.setYaw(-0.25f * (float) Math.PI);

        InputStream vertexShaderInputStream = Game.class.getResourceAsStream("/myshader.vert");
        Scanner vertexShaderScanner = new Scanner(vertexShaderInputStream);
        StringBuilder vertexShaderStringBuilder = new StringBuilder();
        while(vertexShaderScanner.hasNext()) {
            vertexShaderStringBuilder.append(vertexShaderScanner.nextLine());
            vertexShaderStringBuilder.append("\n");
        }
        String vertexShaderCode = vertexShaderStringBuilder.toString();
        int vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderId, vertexShaderCode);
        glCompileShader(vertexShaderId);

        InputStream fragmentShaderInputStream = Game.class.getResourceAsStream("/myshader.frag");
        Scanner fragmentShaderScanner = new Scanner(fragmentShaderInputStream);
        StringBuilder fragmentShaderStringBuilder = new StringBuilder();
        while(fragmentShaderScanner.hasNext()) {
            fragmentShaderStringBuilder.append(fragmentShaderScanner.nextLine());
            fragmentShaderStringBuilder.append("\n");
        }
        String fragmentShaderCode = fragmentShaderStringBuilder.toString();
        int fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderId, fragmentShaderCode);
        glCompileShader(fragmentShaderId);

        int programId = glCreateProgram();
        testShader = programId;
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        glLinkProgram(programId);

        glUseProgram(programId);

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

        MeshBuilder meshBuilder = new MeshBuilder(24);
        meshBuilder.addFace(new Vector3f(0f, 0f, 0f), Direction.UP, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(0f, 0f, 0f), Direction.DOWN, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(0f, 0f, 0f), Direction.NORTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(0f, 0f, 0f), Direction.EAST, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(0f, 0f, 0f), Direction.SOUTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(0f, 0f, 0f), Direction.WEST, new Vector2f(9, 17).div(32));

        meshBuilder.addFace(new Vector3f(1f, 0f, 0f), Direction.UP, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(1f, 0f, 0f), Direction.DOWN, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(1f, 0f, 0f), Direction.NORTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(1f, 0f, 0f), Direction.EAST, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(1f, 0f, 0f), Direction.SOUTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(1f, 0f, 0f), Direction.WEST, new Vector2f(9, 17).div(32));

        meshBuilder.addFace(new Vector3f(1f, 1f, 0f), Direction.UP, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(1f, 1f, 0f), Direction.DOWN, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(1f, 1f, 0f), Direction.NORTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(1f, 1f, 0f), Direction.EAST, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(1f, 1f, 0f), Direction.SOUTH, new Vector2f(9, 17).div(32));
        meshBuilder.addFace(new Vector3f(1f, 1f, 0f), Direction.WEST, new Vector2f(9, 17).div(32));

        meshBuilder.addFace(new Vector3f(1f, -5f, 3f), Direction.UP, new Vector2f(6, 18).div(32));
        meshBuilder.addFace(new Vector3f(1f, -5f, 3f), Direction.DOWN, new Vector2f(2, 1).div(32));
        meshBuilder.addFace(new Vector3f(1f, -5f, 3f), Direction.NORTH, new Vector2f(2, 0).div(32));
        meshBuilder.addFace(new Vector3f(1f, -5f, 3f), Direction.EAST, new Vector2f(2, 0).div(32));
        meshBuilder.addFace(new Vector3f(1f, -5f, 3f), Direction.SOUTH, new Vector2f(2, 0).div(32));
        meshBuilder.addFace(new Vector3f(1f, -5f, 3f), Direction.WEST, new Vector2f(2, 0).div(32));

        testMesh = meshBuilder.toMesh();

        textureAtlas = new TextureAtlas();
        textureAtlas.activate();

        glEnable(GL_DEPTH_TEST);

        glEnable(GL_CULL_FACE);

        cameraMatrix = BufferUtils.createFloatBuffer(16);
        matrixLoc = glGetUniformLocation(programId, "ViewProjectionMatrix");
        glUniformMatrix4fv(matrixLoc, false, camera.updateViewProjectionMatrix().get(cameraMatrix));

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

        Vector3f eye = camera.getEye();
        eye = eye.add(new Vector3f((window.isKeyWPressed() ? (float) Math.sin(yaw) : 0) - (window.isKeySPressed() ? (float) Math.sin(yaw) : 0) + (window.isKeyDPressed() ? (float) Math.cos(yaw) : 0) - (window.isKeyAPressed() ? (float) Math.cos(yaw) : 0),
                (window.isSpacebarPressed() ? 1 : 0) - (window.isShiftPressed() ? 1 : 0),
                (window.isKeySPressed() ? (float) Math.cos(-yaw) : 0) - (window.isKeyWPressed() ? (float) Math.cos(-yaw) : 0) + (window.isKeyAPressed() ? (float) Math.sin(-yaw) : 0) - (window.isKeyDPressed() ? (float) Math.sin(-yaw) : 0)
                ).mul(delta / 1_000_000_000f * 5));
        camera.setEye(eye);

        camera.setAspectRatio((float) window.getFramebufferWidth() / window.getFramebufferHeight());

        glUniformMatrix4fv(matrixLoc, false, camera.updateViewProjectionMatrix().get(cameraMatrix));

        glClearColor(0.74609375f, 0.9140625f, 0.95703125f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        testMesh.draw();

        window.update();

        if(window.shouldClose())
            terminate = true;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
