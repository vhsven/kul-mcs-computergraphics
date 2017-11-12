package raytracer;

import tuples.Point3f;
import tuples.Vector3f;

public class Ray implements Cloneable {
	public Point3f eye;
	
	/**
	 * The directrion of this ray. MUST NOT BE NORMALIZED!
	 */
	public Vector3f dir;
	public Vector3f invDir;
	public int[] sign = new int[3];
	public boolean hasTransformed = false;
	
	public void setDirection(Vector3f dir)
	{
		this.dir = dir;
		this.invDir = new Vector3f(1/dir.x, 1/dir.y, 1/dir.z);
		
		sign[0] = 0; if(invDir.x < 0) sign[0] = 1;
		sign[1] = 0; if(invDir.y < 0) sign[1] = 1;
		sign[2] = 0; if(invDir.z < 0) sign[2] = 1;
	}
	
	@Override
	public String toString() {
		return 	eye + " + t * " + dir;
	}
	
	public Point3f getLocation(double t)
	{
		float x = (float) (eye.x + t*dir.x);
		float y = (float) (eye.y + t*dir.y);
		float z = (float) (eye.z + t*dir.z);
		
		return new Point3f(x, y, z);
	}
	
	@Override
	public Ray clone() {
		Ray clone = new Ray();
		clone.eye = this.eye.clone();
		clone.dir = this.dir.clone();
		clone.invDir = this.invDir.clone();
		clone.sign = this.sign.clone();
		clone.hasTransformed = this.hasTransformed;
		return clone;
	}
}
