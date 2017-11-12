package shapes.geometry;

import raytracer.IntersectRecord;
import raytracer.Ray;
import tuples.Point3f;
import tuples.TexCoord2f;
import tuples.Tuple3f;
import tuples.Vector3f;

import common.BoundingBox3D;

public class Cylinder extends Geometry {
	private Point3f c;
	private float h, r;
	private boolean capped;
	
	public Cylinder(Point3f centerBottom, float radius, float height, boolean capped)
	{
		this.c = centerBottom;
		this.r = radius;
		this.h = height;
		this.capped = capped;
	}

	/*
	 * Infinite Cylinder: (px-x0)^2 + (pz-z0)^2 = r^2 (cf. circle)
	 * Ray: px = ex + t*dx
	 * 		py = ey + t*dy
	 * 		pz = ez + t*dz
	 */
	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax, IntersectRecord record) {
		IntersectRecord topRecord = new IntersectRecord();
		IntersectRecord bottomRecord = new IntersectRecord();
		
		
		Tuple3f d = ray.dir;
		Tuple3f e = ray.eye;
		
		float A = d.x*d.x + d.z*d.z;
			
		float cx_ex = c.x - e.x;
		float cz_ez = c.z - e.z;
		double tCylinder = (d.x*cx_ex + d.z*cz_ez - Math.sqrt(- c.x*c.x*d.z*d.z + 2*c.x*c.z*d.x*d.z - 2*c.x*d.x*d.z*e.z + 2*c.x*d.z*d.z*e.x - c.z*c.z*d.x*d.x + 2*c.z*d.x*d.x*e.z - 2*c.z*d.x*d.z*e.x - d.x*d.x*e.z*e.z + d.x*d.x*r*r + 2*d.x*d.z*e.x*e.z - d.z*d.z*e.x*e.x + d.z*d.z*r*r))/A;
		
		boolean cylinderIntersection = tMin < tCylinder && tCylinder < tMax && c.y < ray.getLocation(tCylinder).y && ray.getLocation(tCylinder).y < c.y + h;
		boolean topIntersection = capped && new Disk(new Point3f(c.x, c.y+h, c.z), r, new Vector3f(0, 1, 0)).intersectsWith(ray, tMin, tMax, topRecord);// && tMin < topRecord.t && topRecord.t < tMax;
		boolean bottomIntersection = capped && new Disk(c, r, new Vector3f(0, -1, 0)).intersectsWith(ray, tMin, tMax, bottomRecord);// && tMin < bottomRecord.t && bottomRecord.t < tMax;
		
		double tTop = topRecord.t;
		double tBottom = bottomRecord.t;
		
		if(cylinderIntersection && topIntersection && bottomIntersection)
		{
			//all 3
			double t = Math.min(tCylinder, Math.min(tTop, tBottom));
			if(t == tCylinder)
				setRecordFromCylinder(ray, record, tCylinder);
			else if(t == tBottom)
				setRecordFromBottom(record, bottomRecord);
			else if(t == tTop)
				setRecordFromTop(record, topRecord);
			return true;
		}
		else if(cylinderIntersection && !topIntersection && bottomIntersection)
		{
			double t = Math.min(tCylinder, tBottom);
			if(t == tCylinder)
				setRecordFromCylinder(ray, record, tCylinder);
			else if(t == tBottom)
				setRecordFromBottom(record, bottomRecord);
			return true;
		}
		else if(cylinderIntersection && topIntersection && !bottomIntersection)
		{
			double t = Math.min(tCylinder, tTop);
			if(t == tCylinder)
				setRecordFromCylinder(ray, record, tCylinder);
			else if(t == tTop)
				setRecordFromTop(record, topRecord);
			return true;
		}
		else if(cylinderIntersection && !topIntersection && !bottomIntersection)
		{
			//cylinder only
			setRecordFromCylinder(ray, record, tCylinder);
			return true;
		}
		else if(!cylinderIntersection && topIntersection && bottomIntersection)
		{
			double t = Math.min(tTop, tBottom);
			if(t == tBottom)
				setRecordFromBottom(record, bottomRecord);
			else if(t == tTop)
				setRecordFromTop(record, topRecord);
			return true;
		}
		else if(!cylinderIntersection && !topIntersection && bottomIntersection)
		{
			//bottom only
			setRecordFromBottom(record, bottomRecord);
			return true;
		}
		else if(!cylinderIntersection && topIntersection && !bottomIntersection)
		{
			//top only
			setRecordFromTop(record, topRecord);
			return true;
			
		}
		else// if(!cylinderIntersection && !topIntersection && !bottomIntersection)
		{
			//no intersection
			return false;
		}
	}
	
	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax) {
		Tuple3f d = ray.dir;
		Tuple3f e = ray.eye;
		
		float A = d.x*d.x + d.z*d.z;
			
		float cx_ex = c.x - e.x;
		float cz_ez = c.z - e.z;
		double tCylinder = (d.x*cx_ex + d.z*cz_ez - Math.sqrt(- c.x*c.x*d.z*d.z + 2*c.x*c.z*d.x*d.z - 2*c.x*d.x*d.z*e.z + 2*c.x*d.z*d.z*e.x - c.z*c.z*d.x*d.x + 2*c.z*d.x*d.x*e.z - 2*c.z*d.x*d.z*e.x - d.x*d.x*e.z*e.z + d.x*d.x*r*r + 2*d.x*d.z*e.x*e.z - d.z*d.z*e.x*e.x + d.z*d.z*r*r))/A;
		
		boolean cylinderIntersection = tMin < tCylinder && tCylinder < tMax && c.y < ray.getLocation(tCylinder).y && ray.getLocation(tCylinder).y < c.y + h;
		boolean topIntersection = capped && new Disk(new Point3f(c.x, c.y+h, c.z), r, new Vector3f(0, 1, 0)).intersectsWith(ray, tMin, tMax);// && tMin < topRecord.t && topRecord.t < tMax;
		boolean bottomIntersection = capped && new Disk(c, r, new Vector3f(0, -1, 0)).intersectsWith(ray, tMin, tMax);// && tMin < bottomRecord.t && bottomRecord.t < tMax;
		
		return cylinderIntersection || topIntersection || bottomIntersection;
	}

	private void setRecordFromTop(IntersectRecord record, IntersectRecord topRecord) {
		record.t = topRecord.t;
		record.normal = topRecord.normal;
		record.object = this;
		record.location = topRecord.location;
		record.textureCoord = topRecord.textureCoord;
	}

	private void setRecordFromBottom(IntersectRecord record, IntersectRecord bottomRecord) {
		record.t = bottomRecord.t;
		record.normal = bottomRecord.normal;
		record.object = this;
		record.location = bottomRecord.location;
		record.textureCoord = bottomRecord.textureCoord;
	}

	private void setRecordFromCylinder(Ray ray, IntersectRecord record, double tCylinder) {
		Point3f location = ray.getLocation(tCylinder);
		record.t = tCylinder;
		Vector3f normal = location.minus(c);
		normal.y = 0;
		record.normal = normal.normalize();
		record.object = this;
		record.location = ray.getLocation(tCylinder);
		
		double theta = Math.atan2(location.z - c.z, location.x - c.x);
		float u = (float) (((theta / Math.PI)+1)/2);
		float v = (location.y - c.y)/h;
		record.textureCoord = new TexCoord2f(u, v);
	}

	@Override
	public BoundingBox3D getBoundingBox3D() {
		return new BoundingBox3D(c.x-r, c.x+r, c.y-h/2f, c.y+h/2f, c.z-r, c.z+r);
	}	
}
