
#version 330

#include shaders/ADSShading.glsl
// ADSShading is used only for light and material struct
// uniforms
uniform sampler2D uTextureMap; 		// uniform variables
uniform sampler2D uNormalMap;

uniform float mode;

// incoming varying data
									// incoming varying data to the fragment shader 
smooth in vec2 vTexCoord;			// sent from the vertex shader. Must have the same 
									// names as the "outs" of the vertex shader.
//in vec3 gl_FragCoord;				// gl_FragCoord is an input that is already 
									// implicitly defined by the system. 
								
smooth in vec4 vNormalEC;
in vec3 vPositionWC;
smooth in vec4 vPositionEC;
in vec3 vPositionTC;
in vec3 vLight1PositionEC;
in vec3 vLight2PositionEC;
in vec3 vLight1DirectionTC;
in vec3 vLight2DirectionTC;
in vec3 vEyeDirectionTC;
uniform mat4 uViewMatrix;

// outgoing data
out vec4 fFragmentColor; 	 		// A fragment shader must provide an "out vec4" 
									// which will be automatically used to write into 
									// the buffers COLOR_ATTACHMENT0. If more than one
									// output is written, you have to specify which
									// value goes to which attachment by using 
									// layout(location = i), where output i goes to 
									// COLOR_ATTACHMENTi. 
//out float gl_FragDepth;			// gl_FragDepth is implicitly defined by the system 
									// and will automatically be set to gl_FragCoord.z 
									// if depth buffering is enabled and it is not 
									// manually set different. 
									
//---------------------
Light myLight1WC = Light(
vec3(0.0, 0.0, 1.0), // position in WC
vec3(0.5, 0.5, 0.5), // ambient
vec3(1.0, 1.0, 1.0), // diffuse
vec3(0.01, 0.01, 0.01)//vec3(0.6, 0.5, 0.4) // specular
);

Light myLight2WC = Light(
vec3(6.0, 3.0, 1.0), // position in WC
vec3(0.5, 0.5, 0.5), // ambient
vec3(1.0, 1.0, 1.0), // diffuse
vec3(0.01, 0.01, 0.01)//vec3(0.6, 0.5, 0.4) // specular
);

Material myMaterial = Material(
	vec3 (0.0, 0.0, 0.0),
	vec3 (0.64, 0.64, 0.64),
	vec3 (0.5, 0.5, 0.5),
	1.0
	);

void main (void) 
{
	Light myLight1EC = Light (
	vLight1PositionEC,
	myLight1WC.ambient,
	myLight1WC.diffuse,
	myLight1WC.specular
	);
	
	Light myLight2EC = Light (
	vLight2PositionEC,
	myLight2WC.ambient,
	myLight2WC.diffuse,
	myLight2WC.specular
	);
	
	float LightPower = 40.0;
	
	vec2 fTexCoord = vec2(vTexCoord.x, vTexCoord.y);
	//tangent space coding starts here
	vec3 textureNormalTC = normalize(texture( uNormalMap, vec2(fTexCoord.x, fTexCoord.y)).rgb*2.0-1.0);//vec2(fTexCoord.x, -fTexCoord.y) ).rgb*2.0-1.0;//normalize(texture( uNormalMap, vec2(fTexCoord.x, -fTexCoord.y) ).rgb*2.0-1.0);

	float distance1 = length(vLight1PositionEC - vPositionEC.xyz);
	
	vec3 n = textureNormalTC;
	if(mode == 0 || mode == 2)
	{	//if bump mapping is disabled, TBN was the identity and the normal will need to be in EC as well
		n = vNormalEC.xyz;
	}
	vec3 l1 = normalize(vLight1DirectionTC);
	
	// Cosine of the angle between the normal and the light direction, 
	// clamped above 0
	//  - light is at the vertical of the triangle -> 1
	//  - light is perpendicular to the triangle -> 0
	//  - light is behind the triangle -> 0
	float cosTheta1 = clamp( dot( n,l1 ), 0,1 );
	
	vec3 e = normalize(vEyeDirectionTC);	//eyevector towards the camera
	vec3 r1 = reflect(-l1, n);			//reflectiondirection of the light by the triangle
	
	// Cosine of the angle between the Eye vector and the Reflect vector,
	// clamped to 0
	//  - Looking into the reflection -> 1
	//  - Looking elsewhere -> < 1
	float cosAlpha1 = clamp( dot( e,r1 ), 0,1 );
	
	vec3 colorTemp = myLight1WC.ambient*myMaterial.ambient + 
			myLight1WC.diffuse * myMaterial.diffuse * LightPower * cosTheta1 / (distance1*distance1) +
			myLight1WC.specular * myMaterial.specular * LightPower * pow(cosAlpha1, 5) / (distance1*distance1);
	//all calculations where done for one light. second light here is done manually, better would be for loop
	float distance2 = length(vLight2PositionEC - vPositionEC.xyz);
	

	vec3 l2 = normalize(vLight2DirectionTC);
	
	// Cosine of the angle between the normal and the light direction, 
	// clamped above 0
	//  - light is at the vertical of the triangle -> 1
	//  - light is perpendicular to the triangle -> 0
	//  - light is behind the triangle -> 0
	float cosTheta2 = clamp( dot( n,l2 ), 0,1 );
	
	vec3 r2 = reflect(-l2, n);			//reflectiondirection of the light by the triangle
	
	// Cosine of the angle between the Eye vector and the Reflect vector,
	// clamped to 0
	//  - Looking into the reflection -> 1
	//  - Looking elsewhere -> < 1
	float cosAlpha2 = clamp( dot( e,r2 ), 0,1 );
	
	vec4 color = vec4(colorTemp, 1.0);
	colorTemp = myLight2WC.ambient + 
			myLight2WC.diffuse * LightPower * cosTheta2 / (distance2*distance2) +
			myLight2WC.specular * LightPower * pow(cosAlpha2, 5) / (distance2*distance2);
	color.xyz += colorTemp;
	color *= texture(uTextureMap, fTexCoord);
	//tangent space coding ends here
	fFragmentColor = clamp(color, 0.0, 1.0); 
	if(mode == 2)
	{
		fFragmentColor = texture2D(uNormalMap, fTexCoord);
	}
	//gl_FragDepth = gl_FragCoord.z;// implicitly done, if depth buffering is enabled

}
