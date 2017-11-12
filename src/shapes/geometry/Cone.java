package shapes.geometry;

import raytracer.IntersectRecord;
import raytracer.Ray;
import tuples.Point3f;
import tuples.TexCoord2f;
import tuples.Tuple3f;
import tuples.Vector3f;

import common.BoundingBox3D;

public class Cone extends Geometry {
	private Point3f c;
	private float h;
	private float r;
	private boolean capped;
	
	/**
	 * Constructs a Cone with the position of the top equal to the given apex.
	 * 
	 * @param r The factor by which the default radius(=current height) is multiplied.
	 */
	public Cone(Point3f apex, float r, float h, boolean capped)
	{
		this.c = apex;
		this.h = h;
		this.r = r;
		this.capped = capped;
	}

	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax, IntersectRecord record) {
		IntersectRecord circleRecord = new IntersectRecord();
		boolean circleIntersection = false;
		if(capped)
		{
			Disk circle = new Disk(new Point3f(c.x, c.y-h, c.z), h*r, new Vector3f(0, -1, 0));
			circleIntersection = circle.intersectsWith(ray, tMin, tMax, circleRecord);
		}
		
		double ex_cx = ray.eye.x - c.x;
		double ey_cy = ray.eye.y - c.y;
		double ez_cz = ray.eye.z - c.z;
		Tuple3f d = ray.dir;
		
		double A = d.x * d.x - r * r * d.y * d.y + d.z * d.z;
		double B = 2 * (ex_cx*d.x - r * r * ey_cy * d.y + ez_cz * d.z);
		double C = ex_cx * ex_cx - r * r * ey_cy * ey_cy + ez_cz * ez_cz;
		
		double D = B*B - 4*A*C;
		
		if(D <= 0)
			return false;
		
		double t = (-B - Math.sqrt(D)) / (2*A);
		
		if(tMin < t && t < tMax)
		{
			Point3f loc = ray.getLocation(t);
			boolean coneIntersection = c.y-h < loc.y && loc.y < c.y; //"right" part of cone
			if(coneIntersection && circleIntersection)
			{
				if(circleRecord.t < t) //circle was closer: return circle params
				{
					setRecordFromCircle(record, circleRecord);
					return true;
				}
				else { //circle hidden behind cone: return cone params
					setRecordFromCone(record, t, loc);
					return true;
				}
			}
			else if(circleIntersection && !coneIntersection) {
				setRecordFromCircle(record, circleRecord);
				return true;
			}
			else if(!circleIntersection && coneIntersection) {
				setRecordFromCone(record, t, loc);
				return true;
			}
			//else no intersection
		}
		return false;
	}
	
	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax) {
		if(capped)
		{
			Disk circle = new Disk(new Point3f(c.x, c.y-h, c.z), h*r, new Vector3f(0, -1, 0));
			if(circle.intersectsWith(ray, tMin, tMax))
				return true;
		}
		
		double ex_cx = ray.eye.x - c.x;
		double ey_cy = ray.eye.y - c.y;
		double ez_cz = ray.eye.z - c.z;
		Tuple3f d = ray.dir;
		
		double A = d.x * d.x - r * r * d.y * d.y + d.z * d.z;
		double B = 2 * (ex_cx*d.x - r * r * ey_cy * d.y + ez_cz * d.z);
		double C = ex_cx * ex_cx - r * r * ey_cy * ey_cy + ez_cz * ez_cz;
		
		double D = B*B - 4*A*C;
		
		if(D <= 0)
			return false;
		
		double t = (-B - Math.sqrt(D)) / (2*A);
		
		if(tMin < t && t < tMax)
		{
			Point3f loc = ray.getLocation(t);
			return c.y-h < loc.y && loc.y < c.y;
		}
		return false;
	}

	private void setRecordFromCone(IntersectRecord record, double t, Point3f location) {
		record.normal = new Vector3f(2*(location.x - c.x), -2*(location.y - c.y), 2*(location.z - c.z)).normalize();
		record.t = t;
		record.object = this;
		record.location = location;
		
		double theta = Math.atan2(location.z - c.z, location.x - c.x);
		float u = (float) (((theta / Math.PI)+1)/2);
		float v = (location.y - c.y)/h;
		record.textureCoord = new TexCoord2f(u, v);
	}

	private void setRecordFromCircle(IntersectRecord record, IntersectRecord circleRecord) {
		record.normal = circleRecord.normal;
		record.t = circleRecord.t;
		record.object = this;
		record.location = circleRecord.location;
		record.textureCoord = circleRecord.textureCoord;
	}

	@Override
	public BoundingBox3D getBoundingBox3D() {
		return new BoundingBox3D(c.x-r, c.x+r, c.y-h, c.y, c.z-r, c.z+r);
	}
}
