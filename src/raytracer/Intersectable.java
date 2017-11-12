package raytracer;

import common.BoundingBox3D;

public interface Intersectable {
	/**
	 * Finds the intersection point of the given Ray with this Intersectable Object.
	 * 
	 * @param ray
	 * @param tMin
	 * @param tMax
	 * @param record
	 * @return
	 */
	public boolean intersectsWith(Ray ray, double tMin, double tMax, IntersectRecord record);
	
	/**
	 * Tests whether the given Ray intersects with this Intersectable Object.
	 * 
	 * @param ray
	 * @param tMin
	 * @param tMax
	 * @return
	 */
	public boolean intersectsWith(Ray ray, double tMin, double tMax);
	public BoundingBox3D getBoundingBox3D();
	//public BoundingBox2D getBoundingBox2D();
}
