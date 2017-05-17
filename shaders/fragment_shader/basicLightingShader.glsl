#version 330

uniform sampler2D diffuseMap;
uniform vec3 uLightColor;
uniform vec3 uCamPos;		//position of the camera
uniform float uIntensity;	//for specular lighting
uniform float uPower;		//for specular lighting

in vec3 vPosWS;
in vec2 vTexCoords;
in vec3 vNormalWS;
in vec3 vLightDirection;

out vec4 fragColor;

void main()
{
	vec4 textureColor = texture(diffuseMap, vTexCoords);
	if(textureColor.a < 0.5)
	{	//quick and dirty support for transparency in model textures
		//this only works for pixels that should bei either opaque (a = 1) 
		//or fully transparent (a = 0)
		discard;
	}
	float nDotl = dot(vNormalWS, vLightDirection);	//calculate the "angle" between the normal and the light direction
				//this determines the brightness of the fragment
	float brightness = max(nDotl, 0.0);
	
	vec3 diffuse = brightness * uLightColor;	//take into account the light color
	
	vec4 ambientLight = vec4(uLightColor.xyz/10.0, 1.0);	//a minor ambient light for better visibility in the scene
	
	vec3 vertexToCam = normalize(uCamPos - vPosWS);	//calculates the normalized vector from the vertex(/fragment) 
													//to the camera
	vec3 lightReflection = normalize(reflect(vLightDirection, vNormalWS));
	float specIntensity = dot(vertexToCam, lightReflection);
	specIntensity = max(0.0, specIntensity);
	specIntensity = pow(specIntensity, uPower);
	vec4 specularLight = clamp(vec4(uLightColor * uIntensity * specIntensity, 1.0),
		vec4(0), vec4(1));	//for some reason specular lighting can take negative values otherwise

	fragColor = (vec4(diffuse, 1.0) + ambientLight + specularLight) * textureColor;
}