package shapes.geometry;


import raytracer.IntersectRecord;
import raytracer.Ray;
import tuples.Point3f;
import tuples.TexCoord2f;
import tuples.Vector3f;

import common.BoundingBox3D;

public class Sphere extends Geometry {
	private float radius;
	private Point3f center;
	
	public Sphere(Point3f center, float radius)
	{
		this.center = center;
		this.radius = radius;
	}
	
	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax) {
		Vector3f d = ray.dir;
		Vector3f e_c = ray.eye.minus(center);
		
		if(e_c.getNorm() < radius)
		{
			//System.out.println("Inside Sphere!");
			return false; //inside sphere: don't return a hit for shadow rays!
		}
		
		double A = d.dotProduct(d);
		double B = d.dotProduct(e_c); //B' = B/2
		double C = e_c.dotProduct(e_c) - (radius * radius); //C' = C
		double D = B*B - A*C; //D' = B'*B' - C' = B*B/4 - AC = D/4
		if(D >= 0)
		{
			double t = (-B - Math.sqrt(D)) / A;
			return (tMin < t && t < tMax);
		}
		return false;
	}

	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax, IntersectRecord record)
	{
		Vector3f d = ray.dir;
		Vector3f e_c = ray.eye.minus(center);
		boolean inside = e_c.getNorm() < radius;
		float A = d.dotProduct(d);
		float B = d.dotProduct(e_c); //B' = B/2
		float C = e_c.dotProduct(e_c) - (radius * radius); //C' = C
		float D = B*B - A*C; //D' = B'*B' - C' = B*B/4 - AC = D/4
		if(D >= 0)
		{
			double t = (-B - Math.sqrt(D)) / A;
			
			if(inside)
			{
				double t2 = -B + Math.sqrt(D); //when inside sphere, this could be a match!
				if(t < 0 && t2 > 0)
					t = t2;
				//else t > 0 && t2 < 0, t can stay as it is
			}
			
			if(tMin < t && t < tMax)
			{
				record.t = t;
				record.object = this;
				Point3f location = ray.getLocation(t);
				record.normal = location.minus(center).normalize();
				record.location = ray.getLocation(t);
				record.textureCoord = getTextureCoord(location);
				return true;
			}
		}
//		else
//		{
//			record.t = -1;
//			record.object = null;
//			record.normal = null;
//			record.textureCoord = null;
//			return false;
//		}
		return false;
	}

	public TexCoord2f getTextureCoord(Point3f location) {
		double theta = Math.acos((location.y - center.y)/radius);				//0 <= theta <= 180
		double phi = Math.atan2(location.x - center.x, location.z - center.z);	//-180 <= phi <= 180
		
		//theta = Math.PI - theta;												//180 >= theta >= 0
		
		double phiOffset = 0;
		double thetaOffset = 0; //moves poles to equator etc...
		
		float u = (float) ((phi + phiOffset) / (2*Math.PI)); //-0.5 <= u <= 0.5
		float v = (float) ((theta + thetaOffset) / Math.PI); //1 >= v >= 0
		
		return new TexCoord2f(u, v);
	}

	@Override
	public BoundingBox3D getBoundingBox3D() {
		return new BoundingBox3D(center.x - radius, center.x + radius, center.y - radius, center.y + radius, center.z - radius, center.z + radius);
	}
	
//	@Override
//	public String toString() {
//		return "<Sphere loc="+center+" r="+radius+" />";
//	}

}
