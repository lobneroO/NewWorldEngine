#version 330

const int MAX_POINT_LIGHTS = 2;
const int MAX_SPOT_LIGHTS = 2;

in vec4 LightSpacePos;
in vec2 TexCoord0;
in vec3 Normal0;
in vec3 WorldPos0;

out vec4 FragColor;

struct BaseLight
{
	vec3 Color;
	float AmbientIntensity;
	float DiffuseIntensity;
};

struct DirectionalLight
{
	BaseLight Base;
	vec3 Direction;
};

struct Attenuation
{
	float Constant;
	float Linear;
	float Exponential;
};

struct PointLight
{
	BaseLight Base;
	vec3 Position;
	Attenuation Atten;
};

struct SpotLight
{
	PointLight Base;
	vec3 Direction;
	float Cutoff;
};

uniform int gNumSpotLights;
uniform SpotLight gSpotLights[MAX_SPOT_LIGHTS];
uniform int gNumPointLights;
uniform PointLight gPointLights[MAX_POINT_LIGHTS];
uniform DirectionalLight gDirectionalLight;
uniform sampler2D gSampler;	//the texture
uniform sampler2D gShadowMap;
uniform vec3 gEyeWorldPos;
uniform float gMatSpecularIntensity;
uniform int gMatSpecularPower;

float calcShadowFactor(vec4 lightSpacePos)
{
	vec3 projCoords = lightSpacePos.xyz / lightSpacePos.w;
	//lightspace is from the light POV, thus similar to the clipspace. 
	//it needs the perspective divide to be able to compare
	//the z-values correctly. the z-buffer also is for positions on the screen
	//thus the view port calculations need to be made as well
	vec2 uvCoords;
	uvCoords.x = 0.5 * projCoords.x + 0.5; //scale from [-1, 1] to [0, 1]
	uvCoords.y = 0.5 * projCoords.y + 0.5;
	
	float z = 0.5 * projCoords + 0.5;
	float depth = texture(gShadowMap, uvCoords).x; //shadowmap is not a RGB(A) color map!
	
	if(depth < z + 0.00001)
	{
		return 0.5;
	}
	else
	{
		return 1.0;
	}
}

vec4 calcLightInternal(BaseLight Light, vec3 LightDirection, vec3 Normal,
						float shadowFactor)
{
	vec4 AmbientColor = vec4(Light.Color * Light.AmbientIntensity, 1.0);
	
	float DiffuseFactor = dot(Normal, -LightDirection);
	
	vec4 DiffuseColor = vec4(0);
	vec4 SpecularColor = vec4(0);
	
	if(DiffuseFactor > 0)
	{
		DiffuseColor = vec4(Light.Color * Light.DiffuseIntensity * DiffuseFactor, 1.0);
		
		vec3 VertexToEye = normalize(gEyeWorldPos - WorldPos0);
		vec3 LightReflect = normalize(reflect(LightDirection, Normal));
		float SpecularFactor = dot(VertexToEye, LightReflect);
		if(SpecularFactor > 0)
		{
			SpecularFactor = pow(SpecularFactor, gMatSpecularPower);
			SpecularColor = vec4(Light.Color * gMatSpecularIntensity * SpecularFactor, 1.0);
		}
	}
	
	return (AmbientColor + shadowFactor * (DiffuseColor + SpecularColor));
}

vec4 calcDirectionalLight(vec3 Normal)
{
	return calcLightInternal(gDirectionalLight.Base, gDirectionalLight.Direction, Normal, 1.0);
}

vec4 calcPointLight(struct PointLight l, vec3 normal, vec4 lightSpacePos)
{
	vec3 lightDirection = WorldPos0 - l.Position;
	float Distance = length(lightDirection);
	lightDirection = normalize(lightDirection);
	float shadowFactor = calcShadowFactor(lightSpacePos);
	
	vec4 Color = calcLightInternal(l.Base, lightDirection, normal, shadowFactor);
	float Attenuation = l.Atten.Constant +
						l.Atten.Linear * Distance +
						l.Atten.Exponential * Distance * Distance;
						
	return Color / Attenuation;
}

vec4 calcSpotLight(struct SpotLight l, vec3 Normal, vec4 lightSpacePos)
{
	vec3 LightToPixel = normalize(WorldPos0 - l.Base.Position);
	float spotFactor = dot(LightToPixel, l.Direction);
	
	if(spotFactor > l.Cutoff)
	{
		vec4 Color = calcPointLight(l.Base, Normal, lightSpacePos);
		return Color * (1.0 - (1.0 - spotFactor) * 1.0 / (1.0 - l.Cutoff));
	}
	
}

void main()
{
	vec3 normal = normalize(Normal0);
	vec4 totalLight = calcDirectionalLight(normal);
	
	for(int i = 0; i < gNumPointLights; i++)
	{
		totalLight += calcPointLight(gPointLights[i], normal, LightSpacePos);
	}
	
	for(int i = 0; i < gNumSpotLights; i++)
	{
		totalLight += calcSpotLight(gSpotLights[i], normal, LightSpacePos);
	}
	
	FragColor = texture2D(gSampler, TexCoord0) * totalLight;
}