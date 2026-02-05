#version 430

in vec3 outColor;

out vec4 finalColor;

uniform double time;

void main() {
    finalColor = vec4(outColor * sin(time), 1.0);
}