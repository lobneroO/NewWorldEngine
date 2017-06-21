#version 330

uniform sampler2D blackMap;	//black color of the blend map
uniform sampler2D redMap;	//red color of the blend map
uniform sampler2D greenMap;
uniform sampler2D blueMap;
uniform sampler2D blendMap;	//blend map that says which part of the terrain gets which texture

uniform vec3 uLightColor;
uniform vec3 uCamPos;		//position of the camera
uniform float uIntensity;	//for specular lighting
uniform float uPower;		//for specular lighting

in vec3 vPosWS;
in vec2 vTexCoords;
in vec3 vNormalWS;
in vec3 vLightDirection;

layout (location = 0) out vec4 fragColor;

void main()
{
	vec4 blendMapColor = texture(blendMap, vTexCoords);
	//the blackTextureAmount is higher the less color the blend map has at a texel it reads out
	float blackTextureAmount = 1 - blendMapColor.r - blendMapColor.g - blendMapColor.b;
	
	vec2 tiledCoords = vTexCoords * 10;	//leads to tiling the terrain
	//read out the individual textures and weight them by the color of the blendmap
	vec4 blackMapColor = texture(blackMap, tiledCoords) * blackTextureAmount;
	vec4 redMapColor = texture(redMap, tiledCoords) * blendMapColor.r;
	vec4 greenMapColor = texture(greenMap, tiledCoords) * blendMapColor.g;
	vec4 blueMapColor = texture(blueMap, tiledCoords) * blendMapColor.b;
	//the weighting is done through multiplying by the blendMapColor / backTextureAmount
	vec4 totalColor = blackMapColor + redMapColor + greenMapColor + blueMapColor;

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

	fragColor = (vec4(diffuse, 1.0) + ambientLight + specularLight) * totalColor;
}