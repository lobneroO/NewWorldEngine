#version 330

uniform sampler2D diffuseMap;

in vec2 vTexCoords;

out vec4 fragColor;

void main()
{
	fragColor = texture(diffuseMap, vTexCoords);
}