#version 330 core

in vec2 texCoords;

uniform sampler2D TextureAtlas;

out vec4 fragColor;

void main() {
    fragColor = texture(TextureAtlas, texCoords);
}