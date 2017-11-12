package matrices;

import tuples.Vector3f;

@SuppressWarnings("serial")
public class RotationMatrix extends Matrix4f {
	private Vector3f axis;
	private float theta;
	
	/**
	 * Generates a Matrix to rotate the given axis parallel to the world axis Z. 
	 */
	private static BaseConversionMatrix getBaseConversionMatrix(Vector3f axis)
	{
		Vector3f w = axis.normalize();
		Vector3f t = w.generateT();
		Vector3f u = t.crossProductRight(w).normalize();
		Vector3f v = w.crossProductRight(u);
		
		return new BaseConversionMatrix(u, v, w);
	}
	
	/*
	 * [u.x v.x w.x 0]   [cos -sin 0 0]   [u.x u.y u.z 0]
	 * [u.y v.y w.y 0] * [sin  cos 0 0] * [v.x v.y v.z 0]
	 * [u.z v.z w.z 0]   [ 0    0  1 0]   [w.x w.y w.z 0]
	 * [ 0   0   0  1]   [ 0    0  0 1]   [ 0   0   0  1]
	 */
	private static Matrix4f getCompleteTransformation(Vector3f axis, float theta)
	{
		BaseConversionMatrix toXYZ = getBaseConversionMatrix(axis);
		RotationZMatrix rotate = new RotationZMatrix(theta);
		BaseConversionMatrix toUVW = toXYZ.inv();
		return toUVW.multiplyRightWith(rotate).multiplyRightWith(toXYZ);
	}
	
	public RotationMatrix(Vector3f axis, float theta)
	{
		super(getCompleteTransformation(axis, theta));
		
		this.theta = theta;
		this.axis = axis;
	}
	
	@Override
	public float det() {
		return 1; //or -1...
	}
	
	@Override
	public RotationMatrix inv() {
		return new RotationMatrix(axis, -theta);
	}
	
	@Override
	public boolean isSingular() {
		return false;
	}
}
