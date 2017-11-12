package shapes;

import java.util.Collection;

import lights.PointLight;
import matrices.IdentityMatrix;
import matrices.Matrix4f;
import matrices.MatrixStack;
import raytracer.IntersectRecord;
import raytracer.Intersectable;
import raytracer.Ray;
import shapes.geometry.Geometry;
import shapes.materials.Material;
import shapes.textures.BumpTexture;
import shapes.textures.Texture;
import tuples.Color3f;
import tuples.Point3f;
import tuples.TexCoord2f;
import tuples.Vector3f;

import common.BoundingBox3D;

public class Shape /*extends Geometry*/ implements Intersectable {

	private Geometry geometry;
	private Material material;
	private Texture texture;
	private BumpTexture bump;
	private Matrix4f transform = IdentityMatrix.getInstance();
	private Matrix4f inverseTransform = IdentityMatrix.getInstance();
	
	public Shape(Geometry geometry, Material material, Texture texture, BumpTexture bump)
	{
		this.geometry = geometry;
		this.material = material;
		this.texture = texture;
		this.bump = bump;
	}
	
	public void setTransformations(MatrixStack transformations) {
		this.transform = transformations.getMatrix();
		this.inverseTransform = transformations.getInverseMatrix();
	}

	public Matrix4f getTransform() {
		return transform;
	}
	
	public Matrix4f getInverseTransform() {
		return inverseTransform;
	}

	public BumpTexture getBumpTexture() {
		return bump;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public Material getMaterial() {
		return material;
	}

	public Texture getTexture() {
		return texture;
	}

	/*
	 * This function only gets called for non-IndexedTriangleSet Shapes.
	 * IndextedTriangleSet intersection is handled by the bounding box hierarchy.
	 */
	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax, IntersectRecord record) {
		Matrix4f inverse = getInverseTransform();
		Ray transformedRay = new Ray();
		//transform ray from world- to object coordinates
		inverse.transform(ray, transformedRay);
		
		boolean intersects = geometry.intersectsWith(transformedRay, tMin, tMax, record);
		if(intersects)
			record.shape = this;
		return intersects;
	}
	
	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax) {
		Matrix4f inverse = getInverseTransform();
		Ray transformedRay = new Ray();
		//transform ray from world- to object coordinates
		inverse.transform(ray, transformedRay);
		
		return geometry.intersectsWith(transformedRay, tMin, tMax);
	}

	@Override
	public BoundingBox3D getBoundingBox3D() {
		return getGeometry().getBoundingBox3D();
	}
	
	@Override
	public String toString() {
		return 	"Geometry: " + geometry + "\r\n" +
				"Material: " + material + "\r\n" + 
				"Texture:  " + texture +  "\r\n" + 
				"Bump:     " + bump;
	}
	
	public Color3f getShade(Collection<PointLight> lights, Point3f location, Vector3f normal, Vector3f view, TexCoord2f texCoords)
	{
		return material.getShade(lights, location, normal, view, texCoords, texture, bump);
	}
}
