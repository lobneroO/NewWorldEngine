#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoords;
layout (location = 2) in vec3 normal;

uniform mat4 uModelMatrix;
uniform mat4 uModelViewProjectionMatrix;
uniform vec3 uLightPosition;

out vec2 vTexCoords;
out vec3 vNormalWS;	//normals in world space
out vec3 vLightDirection;	//the direction from the vertex towards the light

void main()
{
	gl_Position = uModelViewProjectionMatrix * vec4(position, 1.0);
	vNormalWS = (uModelMatrix * vec4(normal, 0.0)).xyz;	//assume no odd scaling
														//thus the modelmatrix can be used for normals
	vNormalWS = normalize(vNormalWS);	//normalize in the vertex shader for efficiency
	//the vertex position is in model space coordinates, thus the conversion to world space coords is needed
	vec4 positionWS = uModelMatrix * vec4(position, 1.0);
	vLightDirection = uLightPosition - positionWS.xyz;
	vLightDirection = normalize(vLightDirection);
		
	vTexCoords = texCoords;
}