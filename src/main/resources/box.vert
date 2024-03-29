#version 330 core

layout (location = 0) in vec3 inPos;

uniform mat4 ModelViewProjectionMatrix;

void main() {
    gl_Position = ModelViewProjectionMatrix * vec4(inPos, 1f);
}