#version 330

uniform sampler2D uReflectionTexture;
uniform sampler2D uRefractionTexture;

in vec4 vPosition_CS;

out vec4 fragColor;

void main()
{
	//transform to normalised device space coordinates
	vec3 pos_NDS = vPosition_CS.xyz/vPosition_CS.w;	
	pos_NDS /= 2;
	pos_NDS += vec3(0.5);
	vec2 refractionTexCoords = vec2(pos_NDS.x, pos_NDS.y);
	vec2 reflectionTexCoords = vec2(pos_NDS.x, -pos_NDS.y);
	vec4 reflectionColor = texture(uReflectionTexture, reflectionTexCoords);
	vec4 refractionColor = texture(uRefractionTexture, refractionTexCoords);

	fragColor = 0.5 * reflectionColor + 0.5 * refractionColor;//vec4(0.0, 0.0, 1.0, 1.0);
}