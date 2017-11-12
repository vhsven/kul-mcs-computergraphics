package matrices;

@SuppressWarnings("serial")
public class RotationZMatrix extends Matrix4f {
	private float theta;
	
	/**
	 * Constructs a new Matrix that rotates objects around the Z-axis.
	 * 
	 * @param theta The rotation angle in degrees.
	 */
	public RotationZMatrix(float theta)
	{
		super(	(float)Math.cos(Math.toRadians(theta)), (float)-Math.sin(Math.toRadians(theta)), 0, 0, 
				(float)Math.sin(Math.toRadians(theta)), (float) Math.cos(Math.toRadians(theta)), 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1);
		
		this.theta = theta%360;
	}
	
	@Override
	public RotationZMatrix inv() {
		return new RotationZMatrix(-theta);
	}
	
	@Override
	public float det() {
		return 1; //or -1...
	}
	
	@Override
	public boolean isIdentity() {
		return theta == 0;
	}
	
	@Override
	public boolean isSingular() {
		return false;
	}
}
