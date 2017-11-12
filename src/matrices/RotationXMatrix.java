package matrices;

@SuppressWarnings("serial")
public class RotationXMatrix extends Matrix4f {
	private float theta;
	
	/**
	 * Constructs a new Matrix that rotates objects around the X-axis.
	 * 
	 * @param theta The rotation angle in degrees.
	 */
	public RotationXMatrix(float theta)
	{
		super(	1, 0, 0, 0, 
				0, (float)Math.cos(Math.toRadians(theta)), (float)-Math.sin(Math.toRadians(theta)), 0, 
				0, (float)Math.sin(Math.toRadians(theta)), (float) Math.cos(Math.toRadians(theta)), 0, 
				0, 0, 0, 1);
		
		this.theta = theta%360;
	}
	
	@Override
	public RotationXMatrix inv() {
		return new RotationXMatrix(-theta);
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
