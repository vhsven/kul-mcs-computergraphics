package tuples;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Vector3f extends Tuple3f implements Serializable
{
    public Vector3f(Tuple4f t) {
		super(t);
	}


	/**
     * Constructs and initializes a Vector3f to (0,0,0).
     */

    public Vector3f()
    {
    }


    /**
     * Constructs and initializes a Vector3f from the specified xyz coordinates.
     * @param x
     * @param y
     * @param z
     */

    public Vector3f(float x, float y, float z)
    {
        super(x, y, z);
    }


    /**
     * Constructs and initializes a Vector3f from the specified Vector3f
     * @param v
     */

    public Vector3f(Vector3f v)
    {
        super(v);
    }


    /**
     * Constructs and initializes a Vector3f from the specified Tuple3f.
     * @param t
     */

    public Vector3f(Tuple3f t)
    {
        super(t);
    }


    /**
     * Constructs and initializes a Vector3f from the array of length 3.
     * @param v
     */

    public Vector3f(float v[])
    {
        super(v);
    }
    
    public float dotProduct(Vector3f o)
    {
    	float newX = x*o.x;
		float newY = y*o.y;
		float newZ = z*o.z;
		return newX + newY + newZ;
    }
    
    public Vector3f crossProductRight(Vector3f o)
    {
    	assert !this.equals(o);
		assert !this.equals(o.invert());
		
    	return new Vector3f(y*o.z - z*o.y, 
    						z*o.x - x*o.z, 
    						x*o.y - y*o.x);
    }
    
    public Vector3f crossProductLeft(Vector3f o)
    {
    	return o.crossProductRight(this);
    }
    
    /**
     * Returns the (second) norm of this 3D vector.
     * 
     * @return ||v||_2
     */
    public float getNorm()
    {
    	return (float) Math.sqrt(x*x+y*y+z*z);
    }
    
    /**
     * Normalizes this 3D vector.
     * 
     * @return v / ||v||_2 
     */
    public Vector3f normalize()
    {
    	float n = getNorm();
    	return new Vector3f(x/n, y/n, z/n);
    }


	@Override
	public Vector3f clone() {
		return new Vector3f(x, y, z);
	}


	public Vector3f plus(Vector3f v)
	{
		return new Vector3f(this.x + v.x, this.y + v.y, this.z + v.z);
	}
	
    public Vector3f minus(Vector3f v)
	{
		return new Vector3f(this.x - v.x, this.y - v.y, this.z - v.z);
	}
	
	public Vector3f invert()
	{
		return new Vector3f(-x, -y, -z);
	}
	
	public Vector3f scalarMultiplication(float s)
	{
		return new Vector3f(s*x, s*y, s*z);
	}


	/**
	 * Generates a not-colinear vector out of w.
	 * This is done by changing the smallest coordinate to 1.
	 */
	public Vector3f generateT() {
		float min = Math.min(x, Math.min(y, z));
		assert min != 1;
		if(min == x)
			return new Vector3f(1, y, z);
		else if(min == y)
			return new Vector3f(x, 1, z);
		else // min == z
			return new Vector3f(x, y, 1);
	}
}
