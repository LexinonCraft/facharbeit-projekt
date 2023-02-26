#version 330 core

layout (location = 0) in vec2 inPos;
layout (location = 1) in vec2 inTexCoords;

uniform vec2 inverseScreenSize;

out vec2 texCoords;

void main() {
    gl_Position = vec4(inPos, 1f, 1f); // inPos.x * inverseScreenSize.x - floor(inPos.x * inverseScreenSize.x), inPos.y * inverseScreenSize.y - floor(inPos.y * inverseScreenSize.y)
    texCoords = inTexCoords;
}