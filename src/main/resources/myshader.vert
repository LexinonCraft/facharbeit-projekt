#version 330 core

layout (location = 0) in vec3 inPos;
layout (location = 1) in vec2 inTexCoords;

out vec2 texCoords;

uniform mat4 ViewProjectionMatrix;

void main() {
    gl_Position = ViewProjectionMatrix * vec4(inPos, 1f);
    texCoords = inTexCoords;
}