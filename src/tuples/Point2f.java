package tuples;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Point2f extends Tuple2f implements Serializable
{
    public Point2f()
    {
    }

    public Point2f(float x, float y)
    {
        super(x, y);
    }

    public Point2f(float p[])
    {
        super(p);
    }

    public Point2f(Point2f p)
    {
        super(p);
    }

    public Point2f(Tuple2f t)
    {
        super(t);
    }

	@Override
	public Object clone() {
		return new Point2f(x, y);
	}

}
