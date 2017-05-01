struct Light
{
	vec3 position; 
	vec3 ambient; 
	vec3 diffuse; 
	vec3 specular;
};

struct Material
{
	vec3 ambient; 
	vec3 diffuse; 
	vec3 specular; 
	float shininess;
};

// Important: ALL coordinates must be in EC
// The Ambient, Diffuse and Specular part of the lighting model are written to the corresponding 
// out variables. 
void ADSLightModel(in vec3 normalEC, in vec3 positionEC, in Light lightEC, in Material material, 
				   out vec3 ambient, out vec3 diffuse, out vec3 specular) 
{
//	normal, light, view, and light reflection vectors
	vec3 cameraEC = vec3(0.0, 0.0, 0.0);
	vec3 normal0EC = normalize(normalEC);
	vec3 lightVectorEC = normalize(lightEC.position - positionEC);
	vec3 viewVectorEC = normalize(cameraEC - positionEC);
	vec3 reflectionVectorEC = reflect(cameraEC - lightVectorEC, normal0EC);

//	ambient light computation
	ambient = material.ambient * lightEC.ambient;
	
//	diffuse light computation
	diffuse = max(0.0, dot(lightVectorEC, normal0EC)) * material.diffuse * lightEC.diffuse;
	
//	Optionally you can add a diffuse attenuation term at this point

//	specular light computation: missing!
	float cosin = max(0.0, dot(reflectionVectorEC, viewVectorEC));
	specular = pow(cosin, material.shininess) * material.specular * lightEC.specular;
	//specular = vec3(0.0);
}
