#version 330 core

in vec2 texCoords;
in float distance;

uniform sampler2D TextureAtlas;
uniform vec4 fogColor;

float getFogFactor(float d) {
    const float fogMax = _replace_far_;
    const float fogMin = _replace_near_;

    if (d>=fogMax) return 1;
    if (d<=fogMin) return 0;

    return 1 - (fogMax - d) / (fogMax - fogMin);
}

void main() {
    /*vec4 fragColor = distance * texture(TextureAtlas, texCoords) / 2 + 0.5;
    gl_FragColor = fragColor;*/
    float alpha = getFogFactor(distance);

    vec4 fragColor = mix(texture(TextureAtlas, texCoords), fogColor, alpha);
    if(fragColor.a < 0.1)
        discard;
    gl_FragColor = fragColor;
}