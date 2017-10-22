#version 330

uniform sampler2D uReflectionTexture;
uniform sampler2D uRefractionTexture;
uniform sampler2D uDudvTexture;

in vec4 vPosition_CS;
in vec2 vTexCoords;

out vec4 fragColor;

const float dudvScale = 0.02;

void main()
{
	//transform to normalised device space coordinates
	vec3 pos_NDS = vPosition_CS.xyz/vPosition_CS.w;	
	pos_NDS /= 2;
	pos_NDS += vec3(0.5);
	
	vec2 distortion = texture(uDudvTexture, vTexCoords).rg; //in [0, 1]
	distortion = distortion * 2.0 - vec2(1.0); //*2 = in [0, 2]; -1 = [-1, 1]
	distortion *= dudvScale;
	
	vec2 refractionTexCoords = vec2(pos_NDS.x, pos_NDS.y);
	vec2 reflectionTexCoords = vec2(pos_NDS.x, -pos_NDS.y);
	
	refractionTexCoords += distortion;
	refractionTexCoords = clamp(refractionTexCoords, 0.001, 0.999);
	reflectionTexCoords += distortion;
	//reflectionTexCoords.x = clamp(reflectionTexCoords.x, 0.001, 0.999);
	//reflectionTexCoords.y = clamp(reflectionTexCoords.y, -0.999, -0.001);
	
	vec4 reflectionColor = texture(uReflectionTexture, reflectionTexCoords);
	vec4 refractionColor = texture(uRefractionTexture, refractionTexCoords);

	fragColor = 0.5 * reflectionColor + 0.5 * refractionColor;//vec4(0.0, 0.0, 1.0, 1.0);
}