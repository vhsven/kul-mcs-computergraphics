package shapes.geometry;

import raytracer.IntersectRecord;
import raytracer.Ray;
import tuples.Point3f;
import tuples.TexCoord2f;
import tuples.Vector3f;

import common.BoundingBox3D;

public class Disk extends Geometry {
	private Point3f c;
	private float r;
	private Vector3f up;
	
	public Disk(Point3f c, float r, Vector3f up)
	{
		this.r = r;
		this.c = c;
		this.up = up;
	}

	@Override
	public BoundingBox3D getBoundingBox3D() {
		return new BoundingBox3D(c.x-r, c.x+r, c.y-0.0001f, c.y+0.0001f, c.z-r, c.z+r);
	}

	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax, IntersectRecord record) {
		double t = (c.y - ray.eye.y) / ray.dir.y; //only possible intersection height
		if(tMin >= t || t >= tMax)
			return false;
		
		Point3f location = ray.getLocation(t);

		float x = location.x - c.x;
		float z = location.z - c.z;
		if(x * x + z * z < r*r) //in circle
		{
			record.t = t;
			record.normal = up;
			record.object = this;
			record.location = ray.getLocation(t);
			
//			double theta = Math.atan2(z, x);
//			float u = (float) (((theta / Math.PI)+1)/2);
//			float v = (float) Math.sqrt(x * x + z * z) / r;
//			record.textureCoord = new TexCoord2f(u, v); //polar texture
			record.textureCoord = new TexCoord2f((location.x/r - c.x/r + 1)/2, (location.z/r - c.z/r + 1)/2); //rect. texture
			
			return true;
		}
		return false;
	}
	
	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax) {
		double t = (c.y - ray.eye.y) / ray.dir.y; //only possible intersection height
		if(tMin >= t || t >= tMax)
			return false;
		
		Point3f location = ray.getLocation(t);

		float x = location.x - c.x;
		float z = location.z - c.z;
		if(x * x + z * z < r*r) //in circle
			return true;
		
		return false;
	}
}
