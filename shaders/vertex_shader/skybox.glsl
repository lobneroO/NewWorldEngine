#version 330

layout (location = 0) in vec3 position;

uniform mat4 uViewProjectionMatrix;
out vec3 vTexCoords;

void main()
{
	vec4 VP_Pos = uViewProjectionMatrix * vec4(position, 1.0);
	gl_Position = VP_Pos.xyww;	//perspective divide leads to z=1.0
								//this corresponds to the far plane
								//thus the skybox will always be rendered on empty spaces, 
								//but is guaranteed to be behind everything else	
								
	vTexCoords = position;		//this is done since the cube map is a 3d texture and the
								//correct pixel is found by a vector from the origin 
								//through a point in the skybox, 
								//thus making the position the texture coordinate
								//for the fragment shader, it will be interpolated
								//as it always is, just in 3d
	if(vTexCoords.x != 0)
	{
		vTexCoords.x /= vTexCoords.x;
	}
	if(vTexCoords.y != 0)
	{
		vTexCoords.y /= vTexCoords.y;
	}
	if(vTexCoords.z != 0)
	{
		vTexCoords.z /= vTexCoords.z;
	}
}