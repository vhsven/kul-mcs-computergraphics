package matrices;

@SuppressWarnings("serial")
public class RotationYMatrix extends Matrix4f {
	private float theta;
	
	/**
	 * Constructs a new Matrix that rotates objects around the Y-axis.
	 * 
	 * @param theta The rotation angle in degrees.
	 */
	public RotationYMatrix(float theta)
	{
		super(	(float) Math.cos(Math.toRadians(theta)), 0, (float)Math.sin(Math.toRadians(theta)), 0, 
				0, 1, 0, 0,
				(float)-Math.sin(Math.toRadians(theta)), 0, (float)Math.cos(Math.toRadians(theta)), 0, 
				0, 0, 0, 1);
		
		this.theta = theta%360;
	}
	
	@Override
	public RotationYMatrix inv() {
		return new RotationYMatrix(-theta);
	}
	
	@Override
	public float det() {
		return 1; //or -1...
	}
	
	@Override
	public boolean isSingular() {
		return false;
	}
	
	@Override
	public boolean isIdentity() {
		return theta == 0;
	}
}