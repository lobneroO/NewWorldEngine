#version 330

layout (location = 0) in vec3 position;

uniform mat4 uModelViewProjectionMatrix;

out vec4 vPosition_CS;
out vec2 vTexCoords;

const float tiling = 6.0;

void main()
{
	//transform to clip space coordinates
	vPosition_CS = uModelViewProjectionMatrix * vec4(position, 1.0);	
	gl_Position = vPosition_CS;
	
	vTexCoords = vec2(position.x/2.0+0.5, position.y/2.0+0.5) * tiling;
}