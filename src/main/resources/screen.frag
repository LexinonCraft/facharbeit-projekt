#version 330 core

in vec2 texCoords;

layout (binding = 1) uniform sampler2D TextureAtlas;

out vec4 fragColor;

void main() {
    fragColor = texture(TextureAtlas, texCoords);
}