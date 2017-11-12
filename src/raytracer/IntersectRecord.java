package raytracer;

import shapes.Shape;
import tuples.Point3f;
import tuples.TexCoord2f;
import tuples.Vector3f;

public class IntersectRecord {
	public double t;
	public Point3f location;
	public Vector3f normal;
	public Intersectable object = null;
	public Shape shape = null;
	public TexCoord2f textureCoord;
	
	public IntersectRecord()
	{
		t = Double.POSITIVE_INFINITY;
	}
	
	@Override
	public String toString() {
		return "Intersection with " + object + " at " + location;
	}
	
	public void importFrom(IntersectRecord o)
	{
		t = o.t;
		location = o.location;
		normal = o.normal;
		object = o.object;
		shape = o.shape;
		textureCoord = o.textureCoord;
	}
}
