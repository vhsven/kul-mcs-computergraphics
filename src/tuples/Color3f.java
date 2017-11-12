package tuples;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Color3f extends Tuple3f implements Serializable
{
    /**
     * Constructs and initializes a Color3f to (0.0, 0.0, 0.0).
     */

    public Color3f()
    {
    }


    /**
     * Constructs and initializes a Color3f from the three xyz values.
     * @param r
     * @param g
     * @param b
     */

    public Color3f(float r, float g, float b)
    {
        super(r, g, b);
    }
    
    public Color3f(int r, int g, int b)
    {
    	this(r/255.0f, g/255.0f, b/255.0f);
    }


    /**
     * Constructs and initializes a Color3f from the specified Color3f. 
     * @param c
     */

    public Color3f(Color3f c)
    {
        super(c);
    }


    /**
     * Constructs and initializes a Color3f from the specified Tuple3f.
     * @param t
     */

    public Color3f(Tuple3f t)
    {
        super(t);
    }


    /**
     * Constructs and initializes a Color3f from the array of length 3.
     * @param c
     */

    public Color3f(float c[])
    {
        super(c);
    }


    /**
     * Constructs and initializes a Color3f from the specified AWT Color object.
     * @param c
     */
    public Color3f(Color c)
    {
        super((float)c.getRed() / 255F, (float)c.getGreen() / 255F, (float)c.getBlue() / 255F);
    }


//    /**
//     * Sets the r,g,b values of this Color3f object to those of the specified AWT Color object.
//     * @param c
//     */
//
//    public final void set(Color c)
//    {
//        x = (float)c.getRed() / 255F;
//        y = (float)c.getGreen() / 255F;
//        z = (float)c.getBlue() / 255F;
//    }


    /**
     * Returns a new AWT color object initialized with the r,g,b values of this Color3f object.
     * @return
     */

    public final Color get()
    {
        int i = Math.round(x * 255F);
        int j = Math.round(y * 255F);
        int k = Math.round(z * 255F);
        return new Color(i, j, k);
    }
    
    public float getRed()
    {
    	return x;
    }
    
    public float getGreen()
    {
    	return y;
    }
    
    public float getBlue()
    {
    	return z;
    }
    
    @Override
    public Color3f clone() {
    	return new Color3f(x, y, z); 
    }
    
    public void plus(Color3f c)
    {
    	x += c.x;
		y += c.y;
		z += c.z;
    }
    
    public void times(Color3f c)
    {
    	x *= c.x;
    	y *= c.y;
    	z *= c.z;
    }
    
    public void times(float intensity)
    {
    	x *= intensity;
    	y *= intensity;
    	z *= intensity;
    }
    
    public void plus(float r, float g, float b)
    {
    	x += r;
    	y += g;
    	z += b;
    }
    
    public void minus(Color3f c)
    {
    	x -= c.x;
    	y -= c.y;
    	z -= c.z;
    }
    
    /**
     * Eliminates light overload in a very naive way... -_-
     */
    public void normalize()
    {
    	x = Math.min(x, 1);
    	y = Math.min(y, 1);
    	z = Math.min(z, 1);
    }
    
    public static Color3f getAverage(List<Color3f> list)
    {
    	float r = 0;
    	float g = 0;
    	float b = 0;
    	for(Color3f color : list)
    	{
    		r += color.x;
    		g += color.y;
    		b += color.z;
    	}
    	int size = list.size();
    	r /= size;
    	g /= size;
    	b /= size;
    	
    	return new Color3f(r, g, b);
    }
}
