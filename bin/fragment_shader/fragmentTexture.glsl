#version 330

const int MAX_SPOT_LIGHTS = 2;
const int MAX_POINT_LIGHTS = 2;

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
uniform sampler2D gSampler;	//the texture
uniform DirectionalLight gDirectionalLight;
uniform vec3 gEyeWorldPos;
uniform float gMatSpecularIntensity;
uniform int gMatSpecularPower;



vec4 calcLightInternal(BaseLight Light, vec3 LightDirection, vec3 Normal)
{
	vec4 AmbientColor = vec4(Light.Color * Light.AmbientIntensity, 1.0);
	
	float DiffuseFactor = clamp(dot(normalize(Normal), LightDirection), 0.0, 1.0);
	
	vec4 DiffuseColor = vec4(Light.Color * Light.DiffuseIntensity * DiffuseFactor, 1.0);
	
	vec3 VertexToEye = normalize(gEyeWorldPos - WorldPos0);
	vec3 LightReflect = normalize(reflect(LightDirection, Normal));
	float SpecularFactor = clamp(dot(VertexToEye, LightReflect), 0.0, 1.0);
	SpecularFactor = pow(SpecularFactor, gMatSpecularPower);
	vec4 SpecularColor = vec4(Light.Color * gMatSpecularIntensity * SpecularFactor, 1.0);
	
	return (AmbientColor + DiffuseColor + SpecularColor);
}

vec4 calcDirectionalLight(vec3 Normal)
{
	return calcLightInternal(gDirectionalLight.Base, gDirectionalLight.Direction, Normal);
}

vec4 calcPointLight(struct PointLight l, vec3 Normal)
{
	vec3 LightDirection = WorldPos0 - l.Position;
	float Distance = length(LightDirection);
	LightDirection = normalize(LightDirection);
	
	vec4 Color = calcLightInternal(l.Base, LightDirection, Normal);
	float Attenuation = l.Atten.Constant +
						l.Atten.Linear * Distance +
						l.Atten.Exponential * Distance * Distance;
						
	return Color / Attenuation;
}

vec4 calcSpotLight(struct SpotLight l, vec3 Normal)
{
	vec3 LightToPixel = normalize(WorldPos0 - l.Base.Position);
	float spotFactor = dot(LightToPixel, l.Direction);
	
	if(spotFactor > l.Cutoff)
	{
		vec4 Color = calcPointLight(l.Base, Normal);
		return Color * (1.0 - (1.0 - spotFactor) * 1.0 / (1.0 - l.Cutoff));
	}
	
}

void main()
{
	vec3 Normal = normalize(Normal0);
	vec4 TotalLight = calcDirectionalLight(Normal);
	
	for(int i = 0; i < gNumPointLights; i++)
	{
		TotalLight += calcPointLight(gPointLights[i], Normal);
	}
	
	for(int i = 0; i < gNumSpotLights; i++)
	{
		TotalLight += calcSpotLight(gSpotLights[i], Normal);
	}
	
	FragColor = texture2D(gSampler, TexCoord0) * TotalLight;
}