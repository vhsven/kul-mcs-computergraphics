package matrices;

import tuples.Vector3f;

@SuppressWarnings("serial")
public class BaseConversionMatrix extends Matrix4f {
	
	private Vector3f u;
	private Vector3f v;
	private Vector3f w;

	/**
	 * This Base Conversion Matrix transforms the coordinates of a vector in UVW-space to XYZ-space. 
	 */
	public BaseConversionMatrix(Vector3f u, Vector3f v, Vector3f w)
	{
		super(	u.x, u.y, u.z, 0, 
				v.x, v.y, v.z, 0, 
				w.x, w.y, w.z, 0, 
				0, 0, 0, 1);
		
		this.u = u;
		this.v = v;
		this.w = w;
	}
	
	@Override
	public BaseConversionMatrix inv() {
		Vector3f x = new Vector3f(u.x, v.x, w.x);
		Vector3f y = new Vector3f(u.y, v.y, w.y);
		Vector3f z = new Vector3f(u.z, v.z, w.z);
		return new BaseConversionMatrix(x, y, z);
	}
	
	public Vector3f getU()
	{
		return u;
	}
	
	public Vector3f getV()
	{
		return v;
	}
	
	public Vector3f getW()
	{
		return w;
	}	
}
