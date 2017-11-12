package common;

import main.Demo;
import main.RayTracerDemo;
import matrices.Matrix4f;
import matrices.ScalingMatrix;
import matrices.TranslationMatrix;
import raytracer.Ray;
import tuples.Point3f;
import tuples.Vector3f;

public class Camera {	
	public Point3f eye;
	
	/**
	 * u = right
	 * v = up
	 * w = inverted view direction
	 */
	public final Vector3f u,v,w;
	
	private final float r, l, t, b, //fixed values 
		n = -0.1f, //get min,max-z from scene + clipping!
		f = -1f; //when scene graph is finished, make sure so transform these values along with M!

	/**
	 * Creates a new camera with the given parameters.
	 * 
	 * @param eye
	 * @param lookAt
	 * @param up
	 * @param fieldOfView in degrees!
	 */
	public Camera(Point3f eye, Point3f lookAt, Vector3f up, double fieldOfView)
	{
		this(eye, lookAt.minus(eye), up, fieldOfView);
	}
	
	/**
	 * Creates a new camera with the given parameters.
	 * 
	 * @param eye
	 * @param gaze
	 * @param up
	 * @param fieldOfView in degrees!
	 */
	public Camera(Point3f eye, Vector3f gaze, Vector3f up, double fieldOfView)
	{
		fieldOfView = (float) ((fieldOfView / 180.0) * Math.PI); //to radians
		this.eye = eye;
		
		r = (float) (Math.abs(n) * Math.tan(fieldOfView/2));
		l = -r;
		t = r * ((float) Demo.Ny / (float) Demo.Nx);
		b = -t;
		
		w = gaze.normalize().invert();
		u = up.crossProductRight(w).normalize(); //up x w
		v = w.crossProductRight(u).normalize(); //w x u
	}
	
	/**
	 * Projects CVV to screen.
	 */
	private static Matrix4f getCvvMatrix() //Mcvv = viewport
	{
		float n_x = Demo.Nx;
		float n_y = Demo.Ny;
		return new Matrix4f(n_x/2f, 0, 0, (n_x-1)/2f, 
							0, n_y/2f, 0, (n_y-1)/2f, 
							0, 0, 1, 0, 
							0, 0, 0, 1);
	}
	
	/**
	 * Scales to CVV.
	 */
	private Matrix4f getOrthoMatrix() //Mo
	{
		ScalingMatrix scale = new ScalingMatrix(2/(r-l), 2/(t-b), 2/(n-f));
		TranslationMatrix trans = new TranslationMatrix(-(l+r)/2, -(b+t)/2, -(n+f)/2);
		//Matrix4f scale = Matrix4f.getScaleMatrix(new Vector3f(2/(r-l), 2/(t-b), 2/(n-f)));
		//Matrix4f trans = Matrix4f.getTranslateMatrix(new Vector3f(-(l+r)/2, -(b+t)/2, -(n+f)/2));
		return scale.multiplyRightWith(trans); //[scale]*[trans]
	}
	
	/**
	 * Transforms camera to O
	 */
	private Matrix4f getCameraMatrix() //Mcam
	{
		Matrix4f viewMatrix = new Matrix4f(	u.x, u.y, u.z, 0, 
											v.x, v.y, v.z, 0, 
											w.x, w.y, w.z, 0, 
											0, 0, 0, 1);
		TranslationMatrix trans = new TranslationMatrix(-eye.x, -eye.y, -eye.z);
		//Matrix4f trans = Matrix4f.getTranslateMatrix(new Vector3f(-eye.x, -eye.y, -eye.z));
		return viewMatrix.multiplyRightWith(trans);
	}
	
	private Matrix4f getPerspectiveMatrix() //Mpersp
	{
//		if(n == 0)
//			throw new IllegalStateException("near-place @ 0");
//		
//		if(n <= f)
//			throw new IllegalStateException("near-plane is further away than far-plane");
		
			return new Matrix4f(n, 0, 0, 0,
								0, n, 0, 0,
								0, 0, (n+f), -f*n,
								0, 0, 1, 0);
	}
	
	public Matrix4f getCompleteCameraTransformationMatrix()
	{
		Matrix4f Mcvv = getCvvMatrix(); //viewport
		Matrix4f Mcam = getCameraMatrix();
		Matrix4f Mpersp = getPerspectiveMatrix();
		Matrix4f Mo = getOrthoMatrix();
		
		//System.out.println("Mcvv = " + Mcvv);
		//System.out.println("Mcam = " + Mcam);
		//System.out.println("Mpersp = " + Mpersp);
		//System.out.println("Mo = " + Mo);
		
		//[Mcvv] * [Mo] * [Mpersp] * [Mcam]
		return Mcvv.multiplyRightWith(Mo).multiplyRightWith(Mpersp).multiplyRightWith(Mcam);
	}
	
	@Override
	public String toString() {
		return 	"e = " + eye + ";\n" + 
				"u = " + u + ";\n" +
				"v = " + v + ";\n" + 
				"w = " + w + ";";
	}
	
	public void updateViewRay(Ray ray, int px, int py)
	{
		double n_x = Demo.Nx * RayTracerDemo.ANTI_ALIASING_FACTOR;
		double n_y = Demo.Ny * RayTracerDemo.ANTI_ALIASING_FACTOR;
		float u = (float) (l + (r-l)*((double)px + 0.5)/n_x);
		float v = (float) (b + (t-b)*((double)py + 0.5)/n_y);
		float d = Math.abs(n); //focal length
		ray.setDirection(w.scalarMultiplication(-d).plus(this.u.scalarMultiplication(u)).plus(this.v.scalarMultiplication(v)));
		//ray.dir = w.scalarMultiplication(-d).plus(this.u.scalarMultiplication(u)).plus(this.v.scalarMultiplication(v));
		ray.eye = eye;
	}
	
	public Vector3f getViewVector(Point3f location)
	{
		return eye.minus(location).normalize();
	}
}
