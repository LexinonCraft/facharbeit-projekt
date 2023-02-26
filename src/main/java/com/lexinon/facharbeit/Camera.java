package com.lexinon.facharbeit;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private Vector3f eye;
    private float yaw;      // looking left and right
    private float pitch;    // looking up and down
    private float fov;
    private float aspectRatio;
    private float zNear, zFar;
    private Matrix4f viewProjectionMatrix;

    public Camera() {
        eye = new Vector3f();
        yaw = 0f;
        pitch = 0f;
        fov = 90;
        aspectRatio = 1;
        zNear = 0.05f;
        zFar = 100f;
        updateViewProjectionMatrix();
    }

    public Matrix4f updateViewProjectionMatrix() {
        return viewProjectionMatrix = new Matrix4f()
            .perspective((float) Math.toRadians(fov), aspectRatio, zNear, zFar)
                .lookAt(eye, new Vector3f((float) (Math.sin(yaw) * Math.cos(pitch)), (float) Math.sin(pitch), -(float) (Math.cos(yaw) * Math.cos(pitch))).normalize().add(eye),
                        new Vector3f((float) (Math.sin(yaw) * Math.sin(-pitch)), (float) Math.cos(pitch), -(float) (Math.cos(yaw) * Math.sin(-pitch))).normalize());
    }

    public Matrix4f getViewProjectionMatrix() {
        return viewProjectionMatrix;
    }

    public Vector3f getEye() {
        return eye;
    }

    public void setEye(Vector3f eye) {
        this.eye = eye;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw % (2 * (float) Math.PI);
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

}
