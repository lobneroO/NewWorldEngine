
#version 330

// includes can be used to structure your shader 
#include shaders/ADSShading.glsl
// ADSShading is used only for light and material struct

uniform mat4 uViewMatrix;						// Incoming data used by the vertex shader:
uniform mat4 uModelMatrix;
uniform mat4 uModelViewMatrix;					// uniforms and attributes.
uniform mat4 uModelViewProjectionMatrix;
uniform mat4 uProjectionMatrix;
uniform mat4 uNormalMatrix;
uniform vec3 lightPos1WC;
uniform vec3 lightPos2WC;
uniform float mode;
layout(location = 0) in vec3 aPositionMC;		// "layout location" for attributes must be
layout(location = 1) in vec2 aTexCoord;			// exactly as specified in 
layout(location = 2) in vec3 aNormalMC;			// glVertexAttribPointer(0, ...)
layout(location = 3) in vec3 vertexTangentMC;
layout(location = 4) in vec3 vertexBinormalMC;

// variableMC == position or vector in object space coordinate system
// variableWC == position or vector in world space coordinate system (ModelMatrix * variableMC)
// variableEC == position or vector in eye space coordinate system (ViewMatrix * variableWC)
// variableCC == position or vector in clip space coordinate system (ProjectionMatrix * variableEC)
// variableSC == position or vector in screen space coordinate system (clip space AFTER viewport transform)
// Be very careful with coordinate systems! 

// Exceptional case: Normals are vectors with the special requirement to be perpendicular to a 
// surface. This demands a special transform in case of scaling. Thus:
// normalEC = NormalMatrix * normalMC

out vec2 vTexCoord;	
out vec4 vNormalEC;
out vec3 vPositionWC;
out vec4 vPositionEC;
out vec3 vPositionTC;
out vec3 vLight1PositionEC;
out vec3 vLight2PositionEC;
out vec3 vLight1DirectionTC;
out vec3 vLight2DirectionTC;
out vec3 vEyeDirectionTC;
		
//out vec4 gl_Position;							// gl_Position is an output that is already
												// implicitly defined by the system. A 
												// vertex shader must at least provide this
												// (clip-space) position, for the clipper 
												// and rasterizer to work. 


void main(void) 
{ 
	vTexCoord = aTexCoord; 

	vec4 light1PosWC = vec4(lightPos1WC, 1.0);
	vec4 light2PosWC = vec4(lightPos2WC, 1.0);
	//light end
	vPositionWC = (uModelMatrix * vec4(aPositionMC, 1.0)).xyz;
	vNormalEC  = uNormalMatrix * vec4(aNormalMC, 1.0);
	vPositionEC = uModelViewMatrix * vec4(aPositionMC, 1.0);
	
	//do Bump Mapping in eyespace because it's easier to get a fragments position
	mat3 MV3 = mat3(uModelViewMatrix);
	vec3 vertexNormalEC = MV3 * normalize(aNormalMC);
	vec3 vertexTangentEC = MV3 * normalize(vertexTangentMC);
	vec3 vertexBinormalEC = MV3 * normalize(vertexBinormalMC);
	
	//for (lightdirection and) eye calculation
	mat3 TBN = transpose(mat3(vertexTangentEC, vertexBinormalEC, vertexNormalEC));
	if(mode == 0 || mode == 2)
	{	//if the TBN is the identity, TC coordinates == EC coordinates, therefore TC is effectivley not used
		TBN = mat3(vec3(1.0, 0.0, 0.0), vec3(0.0, 1.0, 0.0), vec3(0.0, 0.0, 1.0));
	}
	//vectors have to be in eye space before transforming them to tangent space!
	vLight1PositionEC = (uViewMatrix * light1PosWC).xyz;
	vLight2PositionEC = (uViewMatrix * light2PosWC).xyz;
	vec3 eyeDirectionEC = vec3(0, 0, 0) - (uModelViewMatrix * vec4(aPositionMC, 1.0)).xyz;
	vEyeDirectionTC = TBN * eyeDirectionEC;
	vec3 light1DirectionEC = vLight1PositionEC + eyeDirectionEC;
	vec3 light2DirectionEC = vLight2PositionEC + eyeDirectionEC;
	
	vLight1DirectionTC = TBN * light1DirectionEC;
	vLight2DirectionTC = TBN * light2DirectionEC;

	vec4 positionCC = uModelViewProjectionMatrix * vec4(aPositionMC, 1.0); //clipspace position
	gl_Position = positionCC; 
}
