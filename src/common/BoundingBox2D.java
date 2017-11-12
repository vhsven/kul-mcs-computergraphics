package common;

public class BoundingBox2D {
	public final int left, right, top, bottom;
	
	public BoundingBox2D(int left, int right, int top, int bottom)
	{
		this.right = right;
		this.top = top;
		
		this.left = left;
		this.bottom = bottom;
	}
	
	public boolean isInsideBox(float x, float y)
	{
		return 	left < x && x < right &&
				bottom < y && y < top;
	}
}
