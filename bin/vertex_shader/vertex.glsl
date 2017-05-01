#version 330

layout (location = 0) in vec3 Position;
layout (location = 1) in vec4 Color;

uniform mat4 gModelMatrix;

out vec4 vColor;

void main()
{
	vColor = Color;//vec4(clamp(Position, 0.0, 1.0), 1.0);
	gl_Position = gModelMatrix * vec4(Position, 1.0);
}