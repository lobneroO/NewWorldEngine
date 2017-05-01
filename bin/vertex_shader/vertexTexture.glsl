#version 330

layout (location = 0) in vec3 Position;
layout (location = 1) in vec2 TexCoord;
layout (location = 2) in vec3 Normal;

uniform mat4 gMVPMatrix;	//the Model View Projection Matrix
uniform mat4 gMMatrix;		//the Model Matrix

out vec2 TexCoord0;
out vec3 Normal0;
out vec3 WorldPos0;	//position of the vertex in world space coordinates (for lighting)

void main()
{
	TexCoord0 = TexCoord;
	gl_Position = gMVPMatrix * vec4(Position, 1.0);
	Normal0 = (gMMatrix * (vec4(Normal, 0.0))).xyz;	//this works only because scaling isn't done differently
													//in regards to the axes (i.e. x, y, z scaling is the same)
													//because the MMatrix will be a diagonal and the 
													//the normal matrix is the transposed inverse of the world matrix
													//which for a diagonal matrix is the the same as the 
													//transposed transposed matrix, therefore the original matrix
	WorldPos0 = (gMMatrix * (vec4(Position, 1.0))).xyz;
}