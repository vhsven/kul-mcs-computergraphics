package tuples;

@SuppressWarnings("serial")
public class Point4f extends Tuple4f {

	public Point4f() {} //(0,0,0,0)

	public Point4f(float x, float y, float z, float w) {
		super(x, y, z, w);
	}

	public Point4f(float[] t) {
		super(t);
	}

	public Point4f(Tuple4f t) {
		super(t);
	}
	
	public Point4f(Tuple3f t) {
		super(t.x, t.y, t.z, 1f);
	}

	@Override
	public Object clone() {
		return new Point4f(x, y, z, w);
	}

	/**
	 * Normalizes this vector so that w=1.
	 * 
	 * Not to be confused with ||v||.
	 * @return
	 */
	public Point4f normalizeHomogeneous() {
		if(w == 1)
			return this;
		
		if(w == 0)
			throw new IllegalStateException("This point is actually a vector!");
		
		return new Point4f(x/w, y/w, z/w, 1);
	}
}
