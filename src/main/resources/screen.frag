#version 330 core

in vec2 texCoords;

uniform sampler2D ScreenTextureAtlas;

out vec4 fragColor;

void main() {
    vec4 texColor = texture(ScreenTextureAtlas, texCoords);
    if(texColor.a < 0.1)
    discard;
    fragColor = texColor;
}