#version 330

in vec3 texCoord;

out vec4 fragColor;

uniform samplerCube gCubemapTexture;

void main()
{
	//no more calculations are done, no lighting goes into this
	//thus just reading out the correct texture pixel suffices
	fragColor = texture(gCubemapTexture, texCoord);
}