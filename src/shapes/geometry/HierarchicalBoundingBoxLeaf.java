package shapes.geometry;

import java.util.List;

import raytracer.IntersectRecord;
import raytracer.Intersectable;
import raytracer.Ray;

import common.BoundingBox3D;

public class HierarchicalBoundingBoxLeaf implements Intersectable {
	public final static int MAX_TRIANGLES = 3;
	private BoundingBox3D box;
	
	private Intersectable[] triangles;
	
	public HierarchicalBoundingBoxLeaf(List<Intersectable> intersectables) {
		int n = intersectables.size();
		assert n <= MAX_TRIANGLES;
		assert n > 0;
		
		triangles = new Intersectable[n];
		
		for(int i=0; i < n; i++)
			triangles[i] = intersectables.get(i);
		
		box = triangles[0].getBoundingBox3D();
		for(int i=1; i < n; i++)
			box = BoundingBox3D.combine(box, triangles[i].getBoundingBox3D());
		
		for(int i=0; i < n; i++)
		{
			Triangle t = (Triangle) triangles[i];
			if(!box.isInside(t.vertex1) || !box.isInside(t.vertex2) || !box.isInside(t.vertex3))
				assert false;
		}
	}

	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax, IntersectRecord record) {
		double oldMax = tMax;
		
		for(int i=0; i < triangles.length; i++)
			if(triangles[i].intersectsWith(ray, 0, tMax, record))
				tMax = record.t;
		
		if(tMax < oldMax) //we have a hit
		{
			//record.shape = shape;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax) {
		for(int i=0; i < triangles.length; i++)
			if(triangles[i].intersectsWith(ray, tMin, tMax))
				return true;
		
		return false;
	}


	@Override
	public BoundingBox3D getBoundingBox3D() {
		return box;
	}

	@Override
	public String toString() {
		return triangles.length + " triangles leaf";
	}
}
