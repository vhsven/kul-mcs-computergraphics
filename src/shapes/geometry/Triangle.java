package shapes.geometry;

import raytracer.IntersectRecord;
import raytracer.Ray;
import tuples.BarycentricTuple;
import tuples.Color3f;
import tuples.Point3f;
import tuples.TexCoord2f;
import tuples.Vector3f;

import common.BoundingBox2D;
import common.BoundingBox3D;

public final class Triangle extends Geometry {
	public final Point3f vertex1, vertex2, vertex3;
	public TexCoord2f texture1, texture2, texture3;
	private double f_alpha, f_beta, f_gamma;
	public Vector3f normal1, normal2, normal3;
	private Color3f shadingColor1, shadingColor2, shadingColor3;
	
	/** Raytracer barycentric constants. */
	private double a, b, c, d, e, f;
	
	public Triangle(Point3f v1, Point3f v2, Point3f v3, Vector3f n1, Vector3f n2, Vector3f n3)
	{
		this.vertex1 = v1;
		this.vertex2 = v2;
		this.vertex3 = v3;
		
		if(n1 == null || n2 == null || n3 == null)
		{
			Vector3f v12 = v2.minus(v1);
			Vector3f v21 = v1.minus(v2);
			
			Vector3f v23 = v3.minus(v2);
			Vector3f v32 = v2.minus(v3);
			
			Vector3f v13 = v3.minus(v1);
			Vector3f v31 = v1.minus(v3);
			
			n1 = v12.crossProductRight(v13).normalize();
			n2 = v23.crossProductRight(v21).normalize();
			n3 = v31.crossProductRight(v32).normalize();
		}
		this.normal1 = n1;
		this.normal2 = n2;
		this.normal3 = n3;
		
		updateBarycentricConstants();
	}
	
	public void setTextureCoordinates(TexCoord2f t1, TexCoord2f t2, TexCoord2f t3)
	{
		this.texture1 = t1;
		this.texture2 = t2;
		this.texture3 = t3;
	}

	/**
	 * Call this every time vertices change!
	 */
	public void updateBarycentricConstants() {
		f_alpha = f12(vertex1.x, vertex1.y);
		f_beta = f20(vertex2.x, vertex2.y);
		f_gamma = f01(vertex3.x, vertex3.y);
		
		a = vertex1.x - vertex2.x;
		b = vertex1.y - vertex2.y;
		c = vertex1.z - vertex2.z;
				
		d = vertex1.x - vertex3.x;
		e = vertex1.y - vertex3.y;
		f = vertex1.z - vertex3.z;
	}

	/**
	 * Used by rasterizer.
	 */
	private double f01(float x, float y)
	{
		double x0 = vertex1.x;
		double x1 = vertex2.x;
		
		double y0 = vertex1.y;
		double y1 = vertex2.y;
		
		return (y0-y1)*x + (x1-x0)*y + x0*y1 - x1*y0;
		//      ay-by *x +  bx-ax *y + ax*by - bx*ay
	}
	
	/**
	 * Used by rasterizer.
	 */
	private double f12(float x, float y)
	{
		double x1 = vertex2.x;
		double x2 = vertex3.x;
		
		double y1 = vertex2.y;
		double y2 = vertex3.y;
		
		return (y1-y2)*x + (x2-x1)*y + x1*y2 - x2*y1;
		//      by-cy *x +  cx-bx *y + bx*cy - cx*by
	}
	
	/**
	 * Used by rasterizer.
	 */
	private double f20(float x, float y)
	{
		double x0 = vertex1.x;
		double x2 = vertex3.x;
		
		double y0 = vertex1.y;
		double y2 = vertex3.y;
		
		return (y2-y0)*x + (x0-x2)*y + x2*y0 - x0*y2;
		//      cy-ay *x +  ax-cx *y + cx*ay - ax*cy
	}
	
	/**
	 * Get the barycentric coordinates tuple: alpha, beta and gamma.
	 */
	private BarycentricTuple getRasterizerBarycentricTuple(float px, float py)
	{		
		double alpha = f12(px,py)/f_alpha;
		double beta = f20(px,py)/f_beta;
		double gamma = f01(px,py)/f_gamma;
		return new BarycentricTuple(alpha, beta, gamma);
	}
	
	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax) 
	{
		double g = ray.dir.x;
		double h = ray.dir.y;
		double i = ray.dir.z;
				
		double j = vertex1.x - ray.eye.x;
		double k = vertex1.y - ray.eye.y;
		double l = vertex1.z - ray.eye.z;
		
		double ei_hf = e*i - h*f;
		double gf_di = g*f - d*i;
		double dh_eg = d*h - e*g;
		
		double ak_jb = a*k - j*b;
		double jc_al = j*c - a*l;
		double bl_kc = b*l - k*c;
		
		double M = a*ei_hf + b*gf_di + c*dh_eg;
		
		double t = -(f*ak_jb + e*jc_al + d*bl_kc)/M;
		if(t < tMin || t > tMax)
			return false;
		
		double gamma = (i*ak_jb + h*jc_al + g*bl_kc)/M;
		if(gamma < 0 || gamma > 1)
			return false;
		
		double beta = (j*ei_hf + k*gf_di + l*dh_eg)/M;
		if(beta < 0 || /* alpha < 0 */ beta > 1-gamma)
			return false;
		
		return true;
	}
	
	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax, IntersectRecord record) 
	{
		
		double g = ray.dir.x;
		double h = ray.dir.y;
		double i = ray.dir.z;
				
		double j = vertex1.x - ray.eye.x;
		double k = vertex1.y - ray.eye.y;
		double l = vertex1.z - ray.eye.z;
		
		double ei_hf = e*i - h*f;
		double gf_di = g*f - d*i;
		double dh_eg = d*h - e*g;
		
		double ak_jb = a*k - j*b;
		double jc_al = j*c - a*l;
		double bl_kc = b*l - k*c;
		
		double M = a*ei_hf + b*gf_di + c*dh_eg;
		
		double t = -(f*ak_jb + e*jc_al + d*bl_kc)/M;
		if(t < tMin || t > tMax)
			return false;
		
		double gamma = (i*ak_jb + h*jc_al + g*bl_kc)/M;
		if(gamma < 0 || gamma > 1)
			return false;
		
		double beta = (j*ei_hf + k*gf_di + l*dh_eg)/M;
		if(beta < 0 || beta > 1-gamma)
			return false;
		
		
		BarycentricTuple bary = new BarycentricTuple(1-beta-gamma, beta, gamma);
		float nx = (float) bary.getWeightedAverage(normal1.x, normal2.x, normal3.x);
		float ny = (float) bary.getWeightedAverage(normal1.y, normal2.y, normal3.y);
		float nz = (float) bary.getWeightedAverage(normal1.z, normal2.z, normal3.z);
		
		record.t = t;
		record.normal = new Vector3f(nx, ny, nz);;
		record.object = this;
		record.location = ray.getLocation(t);
		
		float texU = (float) bary.getWeightedAverage(texture1.x, texture2.x, texture3.x);
		float texV = (float) bary.getWeightedAverage(texture1.y, texture2.y, texture3.y);
		record.textureCoord = new TexCoord2f(texU, texV);

		return true;
		
	}
	
//	private static final double EPSILON = 0.000001;
//	
//	@Override
//	public boolean intersectsWith(Ray ray, double tMin, double tMax, IntersectRecord record)
//	{
//		Vector3f edge1 = vertex2.minus(vertex1);
//		Vector3f edge2 = vertex3.minus(vertex1);
//		
//		Vector3f pvec = ray.dir.crossProductRight(edge2);
//		
//		double det = edge1.dotProduct(pvec);
//		if(det > -EPSILON && det < EPSILON)
//			return false;
//		double invDet = 1.0 / det;
//		
//		Vector3f tvec = ray.eye.minus(vertex1);
//		double beta = tvec.dotProduct(pvec) * invDet;
//		
//		if(beta < 0.0 || beta > 1.0)
//			return false;
//		
//		Vector3f qvec = tvec.crossProductRight(edge1);
//		double gamma = ray.dir.dotProduct(qvec) * invDet;
//		
//		if(gamma < 0.0 || gamma > 1.0)
//			return false;
//		
//		double t = edge2.dotProduct(qvec);
//		
//		if( tMin > t || t > tMax)
//			return false;
//		
//		BarycentricTuple bary = new BarycentricTuple(1-beta-gamma, beta, gamma);
//		
//		float nx = (float) bary.getWeightedAverage(normal1.x, normal2.x, normal3.x);
//		float ny = (float) bary.getWeightedAverage(normal1.y, normal2.y, normal3.y);
//		float nz = (float) bary.getWeightedAverage(normal1.z, normal2.z, normal3.z);
//		
//		record.t = t;
//		record.normal = new Vector3f(nx, ny, nz);;
//		record.object = this;
//		record.location = ray.getLocation(t);
//		
//		float texU = (float) bary.getWeightedAverage(texture1.x, texture2.x, texture3.x);
//		float texV = (float) bary.getWeightedAverage(texture1.y, texture2.y, texture3.y);
//		record.textureCoord = new TexCoord2f(texU, texV);
//			
//		return true;
//	}
	
	/**
	 * Used by rasterizer.
	 */
	public boolean isInTriangle(float x, float y) {
		BarycentricTuple bary = getRasterizerBarycentricTuple(x, y);
		
		if(bary.alpha >= 0 && bary.beta >= 0 && bary.gamma >= 0)
			if( (bary.alpha > 0 || f12(-1,-1)*f_alpha > 0) && //check vs offscreen coordinate
				(bary.beta > 0 || f12(-1,-1)*f_beta > 0) &&
				(bary.gamma > 0 || f01(-1,-1)*f_gamma > 0))
					return true;
		
		return false;
	}
	
	/**
	 * @pre isInTriangle(x,y)
	 */
	public Color3f getColor(float x, float y)
	{
		assert shadingColor1 != null;
		assert shadingColor2 != null;
		assert shadingColor3 != null;
		
		BarycentricTuple bary = getRasterizerBarycentricTuple(x, y);
		float r = (float) bary.getWeightedAverage(shadingColor1.x, shadingColor2.x, shadingColor3.x);
		float g = (float) bary.getWeightedAverage(shadingColor1.y, shadingColor2.y, shadingColor3.y);
		float b = (float) bary.getWeightedAverage(shadingColor1.z, shadingColor2.z, shadingColor3.z);
		return new Color3f(r,g,b);
	}
	
	/**
	 * Used by rasterizer.
	 * 
	 * @pre isInTriangle(x,y)
	 */
	public float getDepth(float x, float y)
	{
		BarycentricTuple bary = getRasterizerBarycentricTuple(x, y);
		return (float) bary.getWeightedAverage(vertex1.z, vertex2.z, vertex3.z);
	}
	
	@Override
	public String toString() {
		return 	"<Triangle>" + 
				vertex1 + 
				vertex2 + 
				vertex3 + 
				"</Triangle>";
	}
	
	public void setShadingColors(Color3f shadingColor1, Color3f shadingColor2, Color3f shadingColor3)
	{
		this.shadingColor1 = shadingColor1;
		this.shadingColor2 = shadingColor2;
		this.shadingColor3 = shadingColor3;
	}

	@Override
	public BoundingBox3D getBoundingBox3D() {
		float eps = 0.0001f;
		float rightBound = Math.max(Math.max(vertex1.x, vertex2.x), vertex3.x) + eps;
		float upperBound = Math.max(Math.max(vertex1.y, vertex2.y), vertex3.y) + eps;
		float   farBound = Math.max(Math.max(vertex1.z, vertex2.z), vertex3.z) + eps;
		
		float  leftBound = Math.min(Math.min(vertex1.x, vertex2.x), vertex3.x) - eps;
		float lowerBound = Math.min(Math.min(vertex1.y, vertex2.y), vertex3.y) - eps;
		float  nearBound = Math.min(Math.min(vertex1.z, vertex2.z), vertex3.z) - eps;
		
		return new BoundingBox3D(leftBound, rightBound, lowerBound, upperBound, nearBound, farBound);
	}

	public BoundingBox2D getBoundingBox2D() {
		int rightBound = (int)Math.max(Math.max(vertex1.x, vertex2.x), vertex3.x) + 1;
		int upperBound = (int)Math.max(Math.max(vertex1.y, vertex2.y), vertex3.y) + 1;
		int leftBound = (int)Math.min(Math.min(vertex1.x, vertex2.x), vertex3.x);
		int lowerBound = (int)Math.min(Math.min(vertex1.y, vertex2.y), vertex3.y);
		return new BoundingBox2D(leftBound, rightBound, upperBound, lowerBound);
	}
}