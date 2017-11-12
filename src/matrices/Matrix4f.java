package matrices;

import java.io.Serializable;
import java.util.HashSet;

import common.BoundingBox3D;

import raytracer.Ray;
import shapes.geometry.IndexedTriangleSet;
import shapes.geometry.Triangle;
import tuples.Point3f;
import tuples.Point4f;
import tuples.Vector3f;
import tuples.Vector4f;

/**
 * Implements a 4x4 matrix of floats. 
 */
@SuppressWarnings("serial")
public class Matrix4f implements Serializable, Cloneable
{
    /**
     * The first element of the first row.
     */

    public final float m00;


    /**
     * The second element of the first row.
     */

    public final float m01;


    /**
     * The third element of the first row.
     */

    public final float m02;


    /**
     * The forth element of the first row.
     */

    public final float m03;


    /**
     * The first element of the second row.
     */

    public final float m10;


    /**
     * The second element of the second row.
     */

    public final float m11;


    /**
     * The third element of the second row.
     */

    public final float m12;


    /**
     * The fourth element of the second row.
     */

    public final float m13;


    /**
     * The first element of the third row.
     */

    public final float m20;


    /**
     * The second element of the third row.
     */

    public final float m21;


    /**
     * The thrid element of the third row.
     */

    public final float m22;


    /**
     * The fourth element of the third row.
     */

    public final float m23;


    /**
     * The first element of the fourth row.
     */

    public final float m30;


    /**
     * The second element of the fourth row.
     */

    public final float m31;


    /**
     * The third element of the fourth row.
     */

    public final float m32;


    /**
     * The fourth element of the fourth row.
     */

    public final float m33;

    /**
     * Constructs and initializes a Matrix4f from the specified 16 values
     */	

    public Matrix4f
    (
        float m00, float m01, float m02, float m03,
        float m10, float m11, float m12, float m13,
        float m20, float m21, float m22, float m23,
        float m30, float m31, float m32, float m33
    )
    {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }




    /**
     * Constructs a new matrix with the same values as the Matrix4f parameter.
     * @param matrix4f
     */

    public Matrix4f(Matrix4f matrix4f)
    {
        m00 = matrix4f.m00;
        m01 = matrix4f.m01;
        m02 = matrix4f.m02;
        m03 = matrix4f.m03;
        m10 = matrix4f.m10;
        m11 = matrix4f.m11;
        m12 = matrix4f.m12;
        m13 = matrix4f.m13;
        m20 = matrix4f.m20;
        m21 = matrix4f.m21;
        m22 = matrix4f.m22;
        m23 = matrix4f.m23;
        m30 = matrix4f.m30;
        m31 = matrix4f.m31;
        m32 = matrix4f.m32;
        m33 = matrix4f.m33;
    }


    /**
     * Returns a string that contains the values of this Matrix4f.
     */

    public String toString()
    {
        return "[" + m00 + ", " + m01 + ", " + m02 + ", " + m03 + "]\n[" + m10 + ", " + m11 + ", " + m12 + ", " + m13 + "]\n[" + m20 + ", " + m21 + ", " + m22 + ", " + m23 + "]\n[" + m30 + ", " + m31 + ", " + m32 + ", " + m33 + "]\n";
    }

    @Override
    public Matrix4f clone()
    {
        return new Matrix4f(m00, m01, m02, m03, 
        					m10, m11, m12, m13, 
        					m20, m21, m22, m23, 
        					m30, m31, m32, m33);
    }
    
    public Matrix4f multiplyRightWith(Matrix4f o) //[this] * [o]
    {
    	float n00 = m00*o.m00 + m01*o.m10 + m02*o.m20 + m03*o.m30;
    	float n01 = m00*o.m01 + m01*o.m11 + m02*o.m21 + m03*o.m31;
    	float n02 = m00*o.m02 + m01*o.m12 + m02*o.m22 + m03*o.m32;
    	float n03 = m00*o.m03 + m01*o.m13 + m02*o.m23 + m03*o.m33;

    	float n10 = m10*o.m00 + m11*o.m10 + m12*o.m20 + m13*o.m30;
    	float n11 = m10*o.m01 + m11*o.m11 + m12*o.m21 + m13*o.m31;
    	float n12 = m10*o.m02 + m11*o.m12 + m12*o.m22 + m13*o.m32;
    	float n13 = m10*o.m03 + m11*o.m13 + m12*o.m23 + m13*o.m33;

    	float n20 = m20*o.m00 + m21*o.m10 + m22*o.m20 + m23*o.m30;
    	float n21 = m20*o.m01 + m21*o.m11 + m22*o.m21 + m23*o.m31;
    	float n22 = m20*o.m02 + m21*o.m12 + m22*o.m22 + m23*o.m32;
    	float n23 = m20*o.m03 + m21*o.m13 + m22*o.m23 + m23*o.m33;

    	float n30 = m30*o.m00 + m31*o.m10 + m32*o.m20 + m33*o.m30;
    	float n31 = m30*o.m01 + m31*o.m11 + m32*o.m21 + m33*o.m31;
    	float n32 = m30*o.m02 + m31*o.m12 + m32*o.m22 + m33*o.m32;
    	float n33 = m30*o.m03 + m31*o.m13 + m32*o.m23 + m33*o.m33;
    	
    	return new Matrix4f(n00, n01, n02, n03, 
    						n10, n11, n12, n13, 
    						n20, n21, n22, n23, 
    						n30, n31, n32, n33);
    }
	
	/**
	 * Transforms normals etc. using this matrix.
	 * 
	 * Vectors stay Vectors under transformation as long as m30=m31=m32=0, 
	 * which is always the case in this course.
	 */
    private Vector4f transform(Vector4f t) {
    	assert t.w == 0;
    	
    	float n0 = m00 * t.x + m01 * t.y + m02 * t.z;
    	float n1 = m10 * t.x + m11 * t.y + m12 * t.z;
    	float n2 = m20 * t.x + m21 * t.y + m22 * t.z;
    	float n3 = m30 * t.x + m31 * t.y + m32 * t.z;
    	
    	return new Vector4f(n0, n1, n2, n3);
    }
    
    /**
     * Transform vertices etc using this matrix.
     * 
     * If the resulting w-coordinate is not 1, 
     * the vector will be normalized so that w=1 again.
     */
    private Point4f transform(Point4f t) {
    	assert t.w == 1;
    	
    	float n0 = m00 * t.x + m01 * t.y + m02 * t.z + m03;
    	float n1 = m10 * t.x + m11 * t.y + m12 * t.z + m13;
    	float n2 = m20 * t.x + m21 * t.y + m22 * t.z + m23;
    	float n3 = m30 * t.x + m31 * t.y + m32 * t.z + m33;
    	
    	return new Point4f(n0, n1, n2, n3).normalizeHomogeneous();
    }
    
    public Point3f transform(Point3f t)
    {
    	Point4f result = transform(new Point4f(t)); //Point3D -> Point4D -> transform4D
		return new Point3f(result.x, result.y, result.z); //-> Point3D
    }
    
    public Vector3f transform(Vector3f t)
    {
    	Vector4f result = transform(new Vector4f(t));
		return new Vector3f(result.x, result.y, result.z);
    }
	
	public Matrix4f transpose() {
		return new Matrix4f(m00, m10, m20, m30, 
							m01, m11, m21, m31, 
							m02, m12, m22, m32, 
							m03, m13, m23, m33);
	}
	
	private HashSet<Point3f> alreadyTransformed;
	
	public void transform(IndexedTriangleSet mesh)
	{
		alreadyTransformed = new HashSet<Point3f>(); //reset
		for(Triangle t : mesh)
		{			
			if(!alreadyTransformed.contains(t.vertex1))
			{
				transformInline(t.vertex1);
				alreadyTransformed.add(t.vertex1);
			}
			if(!alreadyTransformed.contains(t.vertex2))
			{
				transformInline(t.vertex2);
				alreadyTransformed.add(t.vertex2);
			}
			if(!alreadyTransformed.contains(t.vertex3))
			{
				transformInline(t.vertex3);
				alreadyTransformed.add(t.vertex3);
			}

			//normals are not shared in any way, so we can transform all we want
			transformNormal(t.normal1);
			transformNormal(t.normal2);
			transformNormal(t.normal3);
			
			t.updateBarycentricConstants();
		}
	}
	
	public void transformInline(Point3f v)
	{
		Point4f result = transform(new Point4f(v)); //Point3D -> Point4D -> transform4D
		v.set(result.x, result.y, result.z); //-> Point3D
	}
	
	private void transformNormal(Vector3f n)
	{
		Matrix4f MinvT = this.inv().transpose();
		Vector3f nTrans = new Vector3f(MinvT.transform(new Vector4f(n))).normalize();  // 4D -> transform4D -> 3D -> normalize
		n.set(nTrans.x, nTrans.y, nTrans.z);
	}
	
	public void transform(Ray ray, Ray newRay)
	{
		assert !ray.hasTransformed; //don't do double transforms
		newRay.eye = transform(ray.eye);
		newRay.setDirection(transform(ray.dir));
		newRay.hasTransformed = true;
	}
	
	public BoundingBox3D transform(BoundingBox3D box)
	{
		Vector3f lbn = box.bounds[0];
		Vector3f rtf = box.bounds[1];
		return new BoundingBox3D(transform(lbn), transform(rtf));
	}
	
	public float det()
	{
		return
			m00*m11*m22*m33 - 
			m00*m11*m23*m32 - 
			m00*m12*m21*m33 + 
			m00*m12*m23*m31 + 
			m00*m13*m21*m32 - 
			m00*m13*m22*m31 - 
			
			m01*m10*m22*m33 + 
			m01*m10*m23*m32 + 
			m01*m12*m20*m33 - 
			m01*m12*m23*m30 - 
			m01*m13*m20*m32 + 
			m01*m13*m22*m30 + 
			
			m02*m10*m21*m33 - 
			m02*m10*m23*m31 - 
			m02*m11*m20*m33 + 
			m02*m11*m23*m30 + 
			m02*m13*m20*m31 - 
			m02*m13*m21*m30 - 
			
			m03*m10*m21*m32 + 
			m03*m10*m22*m31 + 
			m03*m11*m20*m32 - 
			m03*m11*m22*m30 - 
			m03*m12*m20*m31 + 
			m03*m12*m21*m30;
	}
	
	public Matrix4f inv() //lang leve matlab...
	{
		if(isSingular()) throw new SingularMatrixException("det() == 0");
		
		float n00 =  (m11*m22*m33 - m11*m23*m32 - m12*m21*m33 + m12*m23*m31 + m13*m21*m32 - m13*m22*m31)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n01 = -(m01*m22*m33 - m01*m23*m32 - m02*m21*m33 + m02*m23*m31 + m03*m21*m32 - m03*m22*m31)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n02 =  (m01*m12*m33 - m01*m13*m32 - m02*m11*m33 + m02*m13*m31 + m03*m11*m32 - m03*m12*m31)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n03 = -(m01*m12*m23 - m01*m13*m22 - m02*m11*m23 + m02*m13*m21 + m03*m11*m22 - m03*m12*m21)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);

		float n10 = -(m10*m22*m33 - m10*m23*m32 - m12*m20*m33 + m12*m23*m30 + m13*m20*m32 - m13*m22*m30)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n11 =  (m00*m22*m33 - m00*m23*m32 - m02*m20*m33 + m02*m23*m30 + m03*m20*m32 - m03*m22*m30)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n12 = -(m00*m12*m33 - m00*m13*m32 - m02*m10*m33 + m02*m13*m30 + m03*m10*m32 - m03*m12*m30)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n13 =  (m00*m12*m23 - m00*m13*m22 - m02*m10*m23 + m02*m13*m20 + m03*m10*m22 - m03*m12*m20)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);

		float n20 =  (m10*m21*m33 - m10*m23*m31 - m11*m20*m33 + m11*m23*m30 + m13*m20*m31 - m13*m21*m30)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n21 = -(m00*m21*m33 - m00*m23*m31 - m01*m20*m33 + m01*m23*m30 + m03*m20*m31 - m03*m21*m30)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n22 =  (m00*m11*m33 - m00*m13*m31 - m01*m10*m33 + m01*m13*m30 + m03*m10*m31 - m03*m11*m30)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n23 = -(m00*m11*m23 - m00*m13*m21 - m01*m10*m23 + m01*m13*m20 + m03*m10*m21 - m03*m11*m20)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);

		float n30 = -(m10*m21*m32 - m10*m22*m31 - m11*m20*m32 + m11*m22*m30 + m12*m20*m31 - m12*m21*m30)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n31 =  (m00*m21*m32 - m00*m22*m31 - m01*m20*m32 + m01*m22*m30 + m02*m20*m31 - m02*m21*m30)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n32 = -(m00*m11*m32 - m00*m12*m31 - m01*m10*m32 + m01*m12*m30 + m02*m10*m31 - m02*m11*m30)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);
		float n33 =  (m00*m11*m22 - m00*m12*m21 - m01*m10*m22 + m01*m12*m20 + m02*m10*m21 - m02*m11*m20)/(m00*m11*m22*m33 - m00*m11*m23*m32 - m00*m12*m21*m33 + m00*m12*m23*m31 + m00*m13*m21*m32 - m00*m13*m22*m31 - m01*m10*m22*m33 + m01*m10*m23*m32 + m01*m12*m20*m33 - m01*m12*m23*m30 - m01*m13*m20*m32 + m01*m13*m22*m30 + m02*m10*m21*m33 - m02*m10*m23*m31 - m02*m11*m20*m33 + m02*m11*m23*m30 + m02*m13*m20*m31 - m02*m13*m21*m30 - m03*m10*m21*m32 + m03*m10*m22*m31 + m03*m11*m20*m32 - m03*m11*m22*m30 - m03*m12*m20*m31 + m03*m12*m21*m30);

		return new Matrix4f(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
	}
	
	public boolean isIdentity()
	{
		return this.equals(IdentityMatrix.getInstance());
	}
	
	@Override
	public boolean equals(Object obj) {
		Matrix4f m = (Matrix4f) obj;
		return 	m00 == m.m00 &&
				m01 == m.m01 &&
				m02 == m.m02 &&
				m03 == m.m03 &&
				
				m10 == m.m10 &&
				m11 == m.m11 &&
				m12 == m.m12 &&
				m13 == m.m13 &&
				
				m20 == m.m20 &&
				m21 == m.m21 &&
				m22 == m.m22 &&
				m23 == m.m23 &&
				
				m30 == m.m30 &&
				m31 == m.m31 &&
				m32 == m.m32 &&
				m33 == m.m33;
	}
	
	public boolean isSingular()
	{
		return det() == 0;
	}
}
