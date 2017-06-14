#version 330

uniform sampler2D gui;

in vec2 vTexCoords;

out vec4 fragColor;

void main()
{
	vec4 color = texture(gui, vTexCoords);
	if(color.a == 0.0)
	{
		//discard;
		color = vec4(1.0f, 0.0f, 0.0f, 1.0f);
	}
	fragColor = color;
	
}