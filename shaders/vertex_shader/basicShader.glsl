#version 330

layout (location = 0) in vec3 position;

uniform mat4 uModelMatrix;

out vec3 vTexCoords;

void main()
{
	gl_Position = vec4(position, 1.0);
	color = vec3(position.x+0.5, 1.0, position.y+0.5);
}