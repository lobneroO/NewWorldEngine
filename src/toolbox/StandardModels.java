package toolbox;

public class StandardModels 
{
	public static float[] getCubeVertices(float scale)
	{
		scale /= 2;
		float[] positions = {
				-scale, -scale, -scale,	//front face lower right triangle
				scale, -scale, -scale,
				scale, scale, -scale,
				
				scale, scale, -scale,	//front face upper left triangle
				-scale, scale, -scale,
				-scale, -scale, -scale,
				
				scale, -scale, -scale,	//right face lower right triangle
				scale, -scale, scale,
				scale, scale, scale,
				
				scale, scale, scale, //right face upper left triangle
				scale, scale, -scale,
				scale, -scale, -scale,
				
				-scale, -scale, scale,	//left face lower right triangle
				-scale, -scale, -scale,
				-scale, scale, -scale,
				
				-scale, scale, -scale,	//left face upper left triangle
				-scale, scale, scale,
				-scale, -scale, scale,
				
				-scale, -scale, scale,	//bottom face lower right triangle
				scale, -scale, scale,
				scale, -scale, -scale,
				
				scale, -scale, -scale,	//bottom face upper right triangle
				-scale, -scale, -scale,
				-scale, -scale, scale,
				
				-scale, scale, -scale,	//top face lower right triangle
				scale, scale, -scale,
				scale, scale, scale,
				
				scale, scale, scale,//top face upper left triangle
				-scale, scale, scale,
				-scale, scale, -scale,
				
				-scale, -scale, scale,	//back face lower right triangle
				-scale, scale, scale,
				scale, -scale, scale,
				
				scale, -scale, scale,//back face upper left triangle
				-scale, scale, scale,
				scale, scale, scale
		};
		
		return positions;
	}
}
