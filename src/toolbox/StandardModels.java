package toolbox;

public class StandardModels 
{
	/**
	 * Returns vertex positions for a cube centered at (0,0,0) that can be drawn 
	 * as GL_TRIANGLES without extra indices
	 * @param scale sets the size of the cube's edges
	 * @return
	 */
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

	/**
	 * Returns vertex positions for a quad centered at (0.0, 0.0, 0.0) that can be drawn
	 * as GL_TRIANGLES without extra indices
	 * @param scale sets the size of the quad's edges
	 * @return
	 */
	public static float[] getQuadVertices(float scale)
	{
		float[] positions =  {
				-scale, -scale, 0.0f,
				scale, -scale, 0.0f,
				scale, scale, 0.0f,
				
				scale, scale, 0.0f,
				-scale, scale, 0.0f,
				-scale, -scale, 0.0f
		};
		
		return positions;
	}
	
	/**
	 * Returns vertex positions for a quad centered at (0.0, 0.0, 0.0) that can be drawn
	 * as GL_TRIANGLE_STRIP without extra indices
	 * @param scale sets the size of the quad's edges
	 * @return
	 */
	public static float[] getQuadTriangleStripVertices(float scale)
	{
		float[] positions =  {
				scale, -scale, 0.0f,
				scale, scale, 0.0f,
				-scale, -scale, 0.0f,
				-scale, scale, 0.0f
		};
		
		return positions;
	}
	
	/**
	 * Returns vertex positions for a quad centered at (0.0, 0.0) that can be drawn
	 * on the screen (i.e. it lacks z coordinates)
	 * @return
	 */
	public static float[] get2DQuadTriangleStripVertices()
	{
		float[] positions =  {
				1.0f, -1.0f, 
				1.0f, 1.0f, 
				-1.0f, -1.0f, 
				-1.0f, 1.0f
		};
		
		return positions;
	}
}
