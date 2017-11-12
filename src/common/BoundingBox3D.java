package common;

import raytracer.Ray;
import tuples.Point3f;
import tuples.Vector3f;

public class BoundingBox3D {
	public final Vector3f[] bounds = new Vector3f[2];
	
	public BoundingBox3D(float left, float right, float bottom, float top, float near, float far)
	{
		assert left <= right;
		assert bottom <= top;
		assert near <= far;
		
		bounds[0] = new Vector3f(left, bottom, near);
		bounds[1] = new Vector3f(right, top, far);
	}
	
	public BoundingBox3D(Vector3f lbn, Vector3f rtf)
	{
		bounds[0] = lbn;
		bounds[1] = rtf;
	}
	
	public boolean isInside(Point3f p)
	{
		return 	bounds[0].x < p.x &&
				bounds[1].x > p.x &&
				
				bounds[0].y < p.y &&
				bounds[1].y > p.y &&
				
				bounds[0].z < p.z &&
				bounds[1].z > p.z;
	}
	
	public boolean intersect(Ray r, double t0, double t1) {
		float tmin, tmax, tymin, tymax, tzmin, tzmax;
		
		tmin = (bounds[r.sign[0]].x - r.eye.x) * r.invDir.x;
		tmax = (bounds[1-r.sign[0]].x - r.eye.x) * r.invDir.x;
		tymin = (bounds[r.sign[1]].y - r.eye.y) * r.invDir.y;
		tymax = (bounds[1-r.sign[1]].y - r.eye.y) * r.invDir.y;
		
		if ( (tmin > tymax) || (tymin > tmax) )
			return false;
		
		if (tymin > tmin)
			tmin = tymin;
		
		if (tymax < tmax)
			tmax = tymax;
		
		tzmin = (bounds[r.sign[2]].z - r.eye.z) * r.invDir.z;
		tzmax = (bounds[1-r.sign[2]].z - r.eye.z) * r.invDir.z;
		
		if ( (tmin > tzmax) || (tzmin > tmax) )
			return false;
		
		if (tzmin > tmin)
			tmin = tzmin;
		
		if (tzmax < tmax)
			tmax = tzmax;
		
		return ( (tmin < t1) && (tmax > t0) );
		
		//return true;
	}
	
	public static BoundingBox3D combine(BoundingBox3D box1, BoundingBox3D box2)
	{
		Vector3f lbn1 = box1.bounds[0];
		Vector3f rtf1 = box1.bounds[1];
		
		Vector3f lbn2 = box2.bounds[0];
		Vector3f rtf2 = box2.bounds[1];
		
		float left = Math.min(lbn1.x, lbn2.x);
		float bottom = Math.min(lbn1.y, lbn2.y);
		float near = Math.min(lbn1.z, lbn2.z);

		float right = Math.max(rtf1.x, rtf2.x);
		float top = Math.max(rtf1.y, rtf2.y);
		float far = Math.max(rtf1.z, rtf2.z);
		
//		System.out.println("Box 1: " + box1);
//		System.out.println("Box 2: " + box2);
//		System.out.println("Box  : " + new BoundingBox3D(left, right, top, bottom, far, near));
		
		return new BoundingBox3D(left, right, bottom, top, near, far);
	}
	
	public Point3f getCenter()
	{
		return new Point3f(	(bounds[0].x+bounds[1].x)/2, 
							(bounds[0].y+bounds[1].y)/2, 
							(bounds[0].z+bounds[1].z)/2);
	}
	
	@Override
	public String toString() {
		return bounds[0] + " - " + bounds[1];
	}
}
