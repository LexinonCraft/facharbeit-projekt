#version 330 core

layout (location = 0) in vec3 inPos;
layout (location = 1) in vec2 inTexCoords;

out vec2 texCoords;

uniform mat4 ModelViewProjectionMatrix;

void main() {
    gl_Position = ModelViewProjectionMatrix * vec4(inPos, 1f);
    gl_Position.z = 2.0 * log(gl_Position.w * _replace_near_ + 1) / log(_replace_far_ * _replace_near_ + 1) - 1;
    gl_Position.z *= gl_Position.w;
    texCoords = inTexCoords;
}