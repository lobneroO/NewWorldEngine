#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoords;

uniform mat4 uModelMatrix;
uniform mat4 uModelViewProjectionMatrix;
out vec2 vTexCoords;

void main()
{
	gl_Position = uModelViewProjectionMatrix * vec4(position, 1.0);
	vTexCoords = texCoords;
}