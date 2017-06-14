#version 330

layout (location = 0) in vec2 position;

uniform mat4 uModelMatrix;

out vec2 vTexCoords;

void main()
{
	gl_Position = uModelMatrix * vec4(position, 0.0, 1.0);
	vTexCoords = position.xy;	//right now this allows for bad texCoords (if scale is > 1)
}