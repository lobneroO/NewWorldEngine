#version 330

in vec2 TexCoord0;

out vec4 FragColor;

uniform sampler2D gSampler;	//the texture


void main()
{	
	FragColor = texture2D(gSampler, TexCoord0);
}