package util;
import com.jogamp.opengl.math.FloatUtil;

/**
 * <p>This class is a heavily inefficient math class for Matrix (4x4) and Vector (3) operations. All
 * other packages seem to be written for performance. Their methods typically write results to a 
 * parameter to not create unnecessary new objects (Unfortunately simple AND efficient solutions as
 * in C or GLSL are not possible in JAVA). In linear algebra creating few objects this is an 
 * important performance factor. However, it is inconvenient to use. These files are written for a
 * very simple understanding and not for performance. If you need to program efficiently replace
 * this math library! </p>
 * 
 * <p>Column matrices(!) as in GLSL. </p>
 * 
 * 
 * @author Stephan Arens
 *
 */
public class simpleMathWithReturnValues {
	
	// constants to access vector fields more convenient e.g. myVector[Z] instead of myVector[2] 
	public static final int X = 0;
	public static final int Y = 1;
	public static final int Z = 2;
	public static final int W = 3;
	
	/**
	 * E = 2.7182818284590452354f
	 */
	public static final float E = 2.7182818284590452354f;

	/**
	 * PI = 3.14159265358979323846f
	 */
	public static final float PI = 3.14159265358979323846f;

	/**
	 * Math.abs
	 */
	public static float abs(float a) {
		return (float) java.lang.Math.abs(a);
	}

	/**
	 * Math.pow
	 */
	public static float pow(float a, float b) {
		return (float) java.lang.Math.pow(a, b);
	}

	/**
	 * Math.sin
	 */
	public static float sin(float a) {
		return (float) java.lang.Math.sin(a);
	}

	/**
	 * Math.cos
	 */
	public static float cos(float a) {
		return (float) java.lang.Math.cos(a);
	}

	/**
	 * Math.acos
	 */
	public static float acos(float a) {
		return (float) java.lang.Math.acos(a);
	}

	/**
	 * Math.sqrt
	 */
	public static float sqrt(float a) {
		return (float) java.lang.Math.sqrt(a);
	}

	/**
	 * Normalize Vector3
	 * @param vector 3-component vector
	 * @return returns Normalized copy
	 */
	public static float[] normalize(float[] vector){
		float[] result = vector.clone();
		FloatUtil.normalize(result);;
		return result;
	}

	/**
	 * Calculate cross-product of 2 vectors
	 * 
	 * @param v1 3-component vector
	 * @param v2 3-component vector
	 * @return v1 X v2
	 */
	public static float[] cross(float[] v1, float[] v2){
		float[] result = new float[3];
		FloatUtil.cross(v1, v2, result);
		return result;
	}
	
	/**
	 * Calculate dot-product of two 3-d vectors
	 * @author Tim Lobner
	 * @param v1 3-component vector
	 * @param v2 3-component vector
	 * @return v1 * v2 = v1.x*v2.x + v1.y*v2.y + v1.z*v2.z
	 */
	public static float dot(float[] v1, float[] v2)
	{
		float result = 0;
		for(int i = 0; i < 3; i++)
		{
			result += v1[i]*v2[i];
		}
		
		return result;
	}
	
	/**
	 * Calculate multyplication of two 2-d vectors
	 * 
	 * @param v1 2-component vector
	 * @param v2 2-component vector
	 * @return v1 * v2 = v1.u*v2.u + v1.v*v2.v
	 */
	public static float multiply2D(float[] v1, float[] v2)
	{
		float result = 0;
		for(int i = 0; i < 2; i++)
		{
			result += v1[i]*v2[i];
		}
		
		return result;
	}
	
	/**
	 * Adds a translation transformation T(x, y, z) to a 4x4 matrix m by multiplying m*T. 
	 * <p>Column matrices(!) as in GLSL. </p>
	 * 
	 * @param m original transformation (also result: m*T(x, y, z))
	 * @param x x offset
	 * @param y y offset
	 * @param z z offset
	 */
	public static final void applyTranslation(float[] m, float x, float y, float z) {
		float[] t = { 1, 0, 0, 0, 
					  0, 1, 0, 0, 
					  0, 0, 1, 0, 
					  x, y, z, 1 };
		FloatUtil.multMatrixf(m, 0, t, 0);
	}

	/**
	 * Adds a rotation transformation R(alpha, x, y, z) to a 4x4 matrix m by multiplying m*R. 
	 * Rotation axis is (x, y, z). <br/>
	 * If the rotation axis is not normalized, a scaling by the length of the rotation axis 
	 * will be done. 
	 * <p>Column matrices(!) as in GLSL. </p>
	 * 
	 * @param m original transformation (also result: m*R(alpha, x, y, z))
	 * @param alpha rotation angle in degrees. 
	 * @param x rotation axis' x
	 * @param y rotation axis' y
	 * @param z rotation axis' z
	 */
	public static final void applyRotation(float[] m, float alpha, float x, float y, float z) {
		float s, c;
		s = (float) Math.sin(Math.toRadians(alpha));
		c = (float) Math.cos(Math.toRadians(alpha));
		float[] r = {
            x * x * (1.0f - c) + c,     y * x * (1.0f - c) + z * s, x * z * (1.0f - c) - y * s, 0.0f,
            x * y * (1.0f - c) - z * s, y * y * (1.0f - c) + c,     y * z * (1.0f - c) + x * s, 0.0f,
            x * z * (1.0f - c) + y * s, y * z * (1.0f - c) - x * s, z * z * (1.0f - c) + c,     0.0f,
            0, 0, 0, 1 };
		FloatUtil.multMatrixf(m, 0, r, 0);
    }
    
	/**
	 * Adds a scale transformation S(x, y, z) to a 4x4 matrix m by multiplying m*S. 
	 * <p>Column matrices(!) as in GLSL. </p>
	 * 
	 * @param m original transformation (also result: m*S(x, y, z))
	 * @param x scale factor in x direction
	 * @param y scale factor in y direction
	 * @param z scale factor in z direction
	 */
	public static final void applyScaling(float[] m, float x, float y, float z) {
		float[] s = { x, 0, 0, 0, 
					  0, y, 0, 0, 
					  0, 0, z, 0, 
					  0, 0, 0, 1 };
		FloatUtil.multMatrixf(m, 0, s, 0);
	}
	
    /** 
     * Returns the product of two 4x4 matrices a and b. Is not the most efficient matrix 
     * multiplication (forces a copy), but is more convenient to use. All other methods write the 
     * result into one parameter. 
     * <p>Column matrices(!) as in GLSL. </p>
     * @param a
     * @param b
     * @return a*b
     */
	public static final float[] multiplyMatrix(float[] a, float[] b){
        float[] aCopy = a.clone();
        FloatUtil.multMatrixf(aCopy, 0, b, 0);
        return aCopy;
    }
	
	/** 
     * Returns the product of 4x4 matrix m and 4-component vector v.
     * <p>Column matrices(!) as in GLSL. </p>
     * @param m 4x4 matrix
     * @param v 4-component vector
     * @return m*v
     */
	public static final float[] multiplyMatrixVector(float[] m, float[] v){
		float[] result = new float[4];
        FloatUtil.multMatrixVecf(m, 0, v, 0, result, 0);
        return result;
    }
    
    /**
     * Computes and returns the inverse of the matrix. ATTENTION: This method assumes, that the 
     * matrix is invertible! That is true for all matrices that WE TEACH in computer graphics except
     * of scaling with 0. That is why a check is not performed for programming convenience. However, 
     * do not use this method in a productive system, because there are matrices that are not 
     * invertible!
     * 
     * <p>Column matrices(!) as in GLSL. </p>
     * @param matrix
     * @return inverse (i.e. matrix * inverse = identity)
     */
    public static final float[] invertMatrix(float[] matrix) {
    	float t;
    	float[] tmp = matrix.clone();
    	float[] result = identity(); 
    	
    	for (int i = 0; i < 4; ++i) {
            // Look for largest element in column
            int swap = i;
            for (int j = i + 1; j < 4; ++j) {
                if (Math.abs(tmp[4*i + j]) > Math.abs(tmp[4*i + i]))
                    swap = j;
            }

            if (swap != i) {
                // Swap rows.
                for (int k = 0; k < 4; ++k) {
                    t = tmp[4*k + i];
                    tmp[4*k + i] = tmp[4*k + swap];
                    tmp[4*k + swap] = t;

                    t = result[k*4+i];
                    result[k*4+i] = result[k*4+swap];
                    result[k*4+swap] = t;
                }
            }

            if (tmp[4*i + i] == 0) {
                // The matrix is singular
                System.out.println("Matrix is not invertible! Please do only invert matrices that "
                		+ "are invertible. Every concatenation of affine transformations (e.g. "
                		+ "translation, rotation) is invertible. Scaling is only invertible if al "
                		+ "scalefactors are different to 0.");
                System.exit(1);
            }

            t = tmp[4*i + i];
            for (int k = 0; k < 4; k++) {
                tmp[4*k + i] /= t;
                result[k*4+i] /= t;
            }
            for (int j = 0; j < 4; j++) {
                if (j != i) {
                    t = tmp[4*i + j];

                    for (int k = 0; k < 4; k++) {
                        tmp[4*k + j] -= tmp[4*k + i]*t;
                        result[k*4+j] -= result[k*4+i]*t;
                    }
                }
            }
        }
    	return result;
    	
    }
    
    /**
     * Returns a transposed matrix. 
     * @param matrix
     * @return transposed matrix. 
     */
    public static final float[] transposeMatrix(float[] matrix){
    	float[] transpose = new float[16];
    	for (int i = 0; i < 4; i++) {
    		for (int j = 0; j < 4; j++) {
				transpose[4*i + j] = matrix[4*j + i];
			}
		}
    	return transpose;
    }
    
    /**
     * Identity. 
     * @return identity. 
     */
    public static final float[] identity(){
    	return new float[]{ 1, 0, 0, 0,
		           			0, 1, 0, 0,
		           			0, 0, 1, 0,
		           			0, 0, 0, 1}; 
    }

}
