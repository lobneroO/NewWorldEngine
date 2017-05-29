package toolbox;

public class StandardModels 
{
	public static float[] getCubeVertices(float scale)
	{
		float[] positions = {
				0.0f, 0.0f, 0.0f,	//front face lower right triangle
				scale, 0.0f, 0.0f,
				scale, scale, 0.0f,
				
				scale, scale, 0.0f,	//front face upper left triangle
				0.0f, scale, 0.0f,
				0.0f, 0.0f, 0.0f,
				
				scale, 0.0f, 0.0f,	//right face lower right triangle
				scale, 0.0f, scale,
				scale, scale, scale,
				
				scale, scale, scale, //right face upper left triangle
				scale, scale, 0.0f,
				scale, 0.0f, 0.0f,
				
				0.0f, 0.0f, scale,	//left face lower right triangle
				0.0f, 0.0f, 0.0f,
				0.0f, scale, 0.0f,
				
				0.0f, scale, 0.0f,	//left face upper left triangle
				0.0f, scale, scale,
				0.0f, 0.0f, scale,
				
				0.0f, 0.0f, scale,	//bottom face lower right triangle
				scale, 0.0f, scale,
				scale, 0.0f, 0.0f,
				
				scale, 0.0f, 0.0f,	//bottom face upper right triangle
				0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, scale,
				
				0.0f, scale, 0.0f,	//top face lower right triangle
				scale, scale, 0.0f,
				scale, scale, scale,
				
				scale, scale, scale,//top face upper left triangle
				0.0f, scale, scale,
				0.0f, scale, 0.0f,
				
				0.0f, 0.0f, scale,	//back face lower right triangle
				0.0f, scale, scale,
				scale, 0.0f, scale,
				
				scale, 0.0f, scale,//back face upper left triangle
				0.0f, scale, scale,
				scale, scale, scale
		};
		
		return positions;
	}
}
