#version 330 core

layout (location = 0) in vec3 inPos;
layout (location = 1) in vec2 inColor;

out vec4 color;

uniform mat4 ViewProjectionMatrix;

void main() {
    gl_Position = ViewProjectionMatrix * vec4(inPos, 1f);
    color = vec4(inColor, inColor.g, 1f);
}