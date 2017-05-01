#version 330

uniform sampler2D diffuseMap;
uniform vec3 uLightColor;

in vec2 vTexCoords;
in vec3 vNormalWS;
in vec3 vLightDirection;

out vec4 fragColor;

void main()
{
	float nDotl = dot(vNormalWS, vLightDirection);	//calculate the "angle" between the normal and the light direction
				//this determines the brightness of the fragment
	float brightness = max(nDotl, 0.0);
	
	vec3 diffuse = brightness * uLightColor;	//take into account the light color

	fragColor = vec4(diffuse, 1.0) * texture(diffuseMap, vTexCoords);
}