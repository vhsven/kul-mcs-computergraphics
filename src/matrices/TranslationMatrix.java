package matrices;

@SuppressWarnings("serial")
public class TranslationMatrix extends Matrix4f {
	
	private float offsetX, offsetY, offsetZ;
	
	public TranslationMatrix(float offsetX, float offsetY, float offsetZ)
	{
		super(	1, 0, 0, offsetX,
				0, 1, 0, offsetY,
				0, 0, 1, offsetZ,
				0, 0, 0, 1);
		
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
	}
	
	@Override
	public TranslationMatrix inv() {
		return new TranslationMatrix(-offsetX, -offsetY, -offsetZ);
	}
	
	@Override
	public float det() {
		return 1;
	}
	
	@Override
	public boolean isSingular() {
		return false;
	}
	
	@Override
	public boolean isIdentity() {
		return 	offsetX == 0 &&
				offsetY == 0 &&
				offsetZ == 0;
	}
}
