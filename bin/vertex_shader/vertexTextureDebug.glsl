#version 330

layout (location = 0) in vec3 Position;
layout (location = 1) in vec2 TexCoord;
layout (location = 2) in vec3 Normal;

uniform mat4 gMVPMatrix;	//the Model View Projection Matrix

out vec2 TexCoord0;
out vec3 Normal0;

void main()
{
	TexCoord0 = TexCoord;
	gl_Position = gMVPMatrix * vec4(Position, 1.0);
}