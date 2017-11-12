package matrices;

@SuppressWarnings("serial")
public class ScalingMatrix extends Matrix4f {
	private float scaleX, scaleY, scaleZ;
	
	public ScalingMatrix(float scaleX, float scaleY, float scaleZ)
	{
		super(	scaleX, 0f, 0f, 0f,
				0f, scaleY, 0f, 0f,
				0f, 0f, scaleZ, 0f,
				0f, 0f, 0f, 1f);
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
	}
	
	@Override
	public ScalingMatrix inv() {
		return new ScalingMatrix(1/scaleX, 1/scaleY, 1/scaleZ);
	}
	
	@Override
	public float det() {
		return scaleX*scaleY*scaleZ;
	}
	
	@Override
	public boolean isSingular() {
		return false;
	}
	
	@Override
	public boolean isIdentity() {
		return 	scaleX == 1 &&
				scaleY == 1 &&
				scaleZ == 1;
	}
}
