package shapes.geometry;

import java.util.ArrayList;
import java.util.List;

import matrices.Matrix4f;
import raytracer.IntersectRecord;
import raytracer.Intersectable;
import raytracer.Ray;
import shapes.Shape;

import common.BoundingBox3D;

public class HierarchicalBoundingBoxNode implements Intersectable {
	private BoundingBox3D box;
	private Intersectable leftNode;
	private Intersectable rightNode;
	private Shape shape = null;
	
	/**
	 * @param axis X=0; Y=1; Z=2
	 */
	public HierarchicalBoundingBoxNode(List<Intersectable> intersectables, int axis, Shape shape)
	{
		this.shape = shape;
		int newAxis = (axis+1)%3;
		int n = intersectables.size();
		//System.out.println(n);
		assert n != 0;
		
		//as soon as we're down to triangle level
		//and there's only a couple left
		//group them together in 1 box
		//so we avoid stack overflow
		if(n >= 3 && n <= 2*HierarchicalBoundingBoxLeaf.MAX_TRIANGLES && intersectables.get(0) instanceof Triangle)
		{
			leftNode = new HierarchicalBoundingBoxLeaf(intersectables.subList(0, n/2));
			rightNode = new HierarchicalBoundingBoxLeaf(intersectables.subList(n/2, n));
		}
		else if(n == 1)
		{
			Intersectable geo = intersectables.get(0);
			if(geo instanceof Shape && ((Shape)geo).getGeometry() instanceof IndexedTriangleSet) //continue deeper
			{
				this.shape = (Shape)geo;
				IndexedTriangleSet its = (IndexedTriangleSet) this.shape.getGeometry();
				List<Intersectable> triangles = new ArrayList<Intersectable>(its.triangles);
				partitionList(triangles, axis, this.shape, newAxis);
			}
			else
			{
				leftNode = geo;
				rightNode = null;
			}
		}
		else if(n == 2)
		{
			Intersectable geoLeft = intersectables.get(0);
			if(geoLeft instanceof Shape && ((Shape)geoLeft).getGeometry() instanceof IndexedTriangleSet) //continue deeper
			{
				IndexedTriangleSet its = (IndexedTriangleSet) ((Shape)geoLeft).getGeometry();
				List<Intersectable> triangles = new ArrayList<Intersectable>(its.triangles);
				HierarchicalBoundingBoxNode hbbNode = new HierarchicalBoundingBoxNode(triangles, newAxis, (Shape)geoLeft);
				leftNode = hbbNode;
			}
			else
			{
				leftNode = geoLeft;
			}
			
			Intersectable geoRight = intersectables.get(1);
			if(geoRight instanceof Shape && ((Shape)geoRight).getGeometry() instanceof IndexedTriangleSet) //continue deeper
			{
				IndexedTriangleSet its = (IndexedTriangleSet) ((Shape)geoRight).getGeometry();
				List<Intersectable> triangles = new ArrayList<Intersectable>(its.triangles);
				HierarchicalBoundingBoxNode hbbNode = new HierarchicalBoundingBoxNode(triangles, newAxis, (Shape)geoRight);
				rightNode = hbbNode;
			}
			else
			{
				rightNode = geoRight;
			}
		}
		else
			partitionList(intersectables, axis, shape, newAxis);
		
		box = leftNode.getBoundingBox3D();
		if(rightNode != null)
			box = BoundingBox3D.combine(box, rightNode.getBoundingBox3D());
	}

	private void partitionList(List<Intersectable> intersectables, int currentAxis, Shape shape, int newAxis) {
		List<Intersectable> leftList = new ArrayList<Intersectable>();
		List<Intersectable> rightList = new ArrayList<Intersectable>();
		partition(intersectables, currentAxis, leftList, rightList);
		
		assert !(leftList.size() == 0 && rightList.size() == 0);
		
		if(leftList.size() == 0) //use left node only
		{
			System.out.println("Left 0");
			leftNode = new HierarchicalBoundingBoxNode(rightList, newAxis, shape);
			rightNode = null;
		}
		else if(rightList.size() == 0)
		{
			System.out.println("Right 0");
			leftNode = new HierarchicalBoundingBoxNode(leftList, newAxis, shape);
			rightNode = null;
		}
		else
		{
			leftNode = new HierarchicalBoundingBoxNode(leftList, newAxis, shape);
			rightNode = new HierarchicalBoundingBoxNode(rightList, newAxis, shape);
		}
	}

	private void partition(List<Intersectable> intersectables, int axis, List<Intersectable> leftList, List<Intersectable> rightList) {
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		for(Intersectable inter : intersectables)
		{
			float x = inter.getBoundingBox3D().getCenter().getComponent(axis);
			if(x < min)
				min = x;
			if(x > max)
				max = x;
		}
		
		if(min == max)
		{
			//System.out.println("Can't partition objects by center! Splitting in half instead.");
			int size = intersectables.size();
			for(int i=0; i < size; i++)
			{
				if(i < size/2)
					leftList.add(intersectables.get(i));
				else
					rightList.add(intersectables.get(i));
			}
			return;
		}
		
		float middle = (min + max) / 2f;
		
		for(Intersectable inter : intersectables)
		{
			float x = inter.getBoundingBox3D().getCenter().getComponent(axis);
			if(x < middle)
				leftList.add(inter);
			else
				rightList.add(inter);
		}
	}

	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax, IntersectRecord record) {
		Ray transformedRay = ray.clone();
		if(shape != null && shape.getGeometry() instanceof IndexedTriangleSet && !ray.hasTransformed)
		{
			Matrix4f inverse = shape.getInverseTransform();
			inverse.transform(ray, transformedRay);
		}
		
		//if we don't have a shape yet, just let the ray pass
		//because we can't get the correct transform yet
		if(shape == null || box.intersect(transformedRay, tMin, tMax))
		{
			IntersectRecord leftRec = new IntersectRecord();
			IntersectRecord rightRec = new IntersectRecord();
			
			boolean hitLeft = leftNode.intersectsWith(transformedRay, tMin, tMax, leftRec);
			boolean hitRight = (rightNode != null) && rightNode.intersectsWith(transformedRay, tMin, tMax, rightRec);
			
			boolean result = false;
			if(hitLeft && hitRight)
			{
				if(leftRec.t < rightRec.t)
					record.importFrom(leftRec);
				else
					record.importFrom(rightRec);
				result = true;
			}
			else if(hitLeft)
			{
				record.importFrom(leftRec);
				result = true;
			}
			else if(hitRight)
			{
				record.importFrom(rightRec);				
				result = true;
			}
			
			if(result)
			{
				//no intersection with shape class encountered (else record.shape would be set already)
				//	-> we're in an IndexedTriangleSet
				//		-> the shape of this BB will be set already
				if(record.shape == null)
					record.shape = shape;
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax) {
		Ray transformedRay = ray.clone();
		if(shape != null && shape.getGeometry() instanceof IndexedTriangleSet && !ray.hasTransformed)
		{
			Matrix4f inverse = shape.getInverseTransform();
			inverse.transform(ray, transformedRay);
		}
		
		if(shape == null || box.intersect(ray, tMin, tMax))
		{
			boolean hitLeft = leftNode.intersectsWith(transformedRay, tMin, tMax);
			if(hitLeft) return true;
			boolean hitRight = (rightNode != null) && rightNode.intersectsWith(transformedRay, tMin, tMax);
			return hitRight;
		}
		return false;
	}

	@Override
	public BoundingBox3D getBoundingBox3D() {
		return box;
	}
	
	@Override
	public String toString() {
		return 	"\t|_" + leftNode.toString() + "\n" + 
				(rightNode != null ? "\t|_" + rightNode.toString() : "\t|_ ___") + "\n";
	}
}
