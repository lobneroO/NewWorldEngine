#version 330

uniform sampler2D gui;

in vec2 vTexCoords;

out vec4 fragColor;

void main()
{
	fragColor = texture(gui, vTexCoords);
}