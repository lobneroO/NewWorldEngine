#version 330

layout (location = 0) in vec3 position;

uniform mat4 uModelViewProjectionMatrix;

out vec4 vPosition_CS;
out vec2 vTexCoords;

void main()
{
	//transform to clip space coordinates
	vPosition_CS = uModelViewProjectionMatrix * vec4(position, 1.0);	
	gl_Position = vPosition_CS;
}