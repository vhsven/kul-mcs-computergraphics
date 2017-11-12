package matrices;

public class IdentityMatrix extends Matrix4f {

	private static final long serialVersionUID = -4569163870972836837L;
	
	private static IdentityMatrix instance;
	
	public static IdentityMatrix getInstance() {
		if(instance == null)
			instance = new IdentityMatrix();
		
		return instance;
	}
	
	private IdentityMatrix()
	{
		super(	1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1);
	}
	
	@Override
	public float det() {
		return 1;
	}
	
	@Override
	public Matrix4f inv() {
		return this;
	}
	
	@Override
	public Matrix4f multiplyRightWith(Matrix4f o) {
		return o;
	}
	
	@Override
	public boolean isIdentity() {
		return true;
	}
	
	@Override
	public Matrix4f transpose() {
		return this;
	}
	
	@Override
	public boolean isSingular() {
		return false;
	}
}
