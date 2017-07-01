#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoords;
layout (location = 2) in vec3 normal;

uniform mat4 uModelMatrix;
uniform mat4 uModelViewProjectionMatrix;
uniform vec3 uLightPosition;
uniform vec4 uClippingPlane;

out vec3 vPosWS;	//vertex position in world space
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
	vPosWS = (uModelMatrix * vec4(position, 1.0)).xyz;
	vLightDirection = uLightPosition - vPosWS;
	vLightDirection = normalize(vLightDirection);
		
	vTexCoords = texCoords;
	
	//get the distance of the entity from the clipping plane, if below: cull
	//if the clipping plane is not uploaded to the shader, this call is ignored
	gl_ClipDistance[0] = dot(vec4(vPosWS, 1.0), uClippingPlane);
}