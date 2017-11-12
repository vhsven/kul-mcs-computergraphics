package shapes.textures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import tuples.Color3f;
import tuples.TexCoord2f;

public class Texture {
	protected BufferedImage image;
	private String src;
	
	public Texture(String src) {
		this.src = src;
		File file = new File(src);
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getHeight() {
		return image.getHeight();
	}

	public int getWidth() {
		return image.getWidth();
	}

	protected int getPx(float u) {
		if(u < 0) u++;
		if(u >= 1) u--; //1 -> 0
		
		int px = (int) (getWidth()*u);
		
		return px;
	}

	protected int getPy(float v) {
		if(v < 0) v++;
		if(v >= 1) v--;
		
		int py = (int) (getHeight()*v);
		
		return py;
	}
	
	public Color3f getColor(TexCoord2f texCoord)
	{		
		return getColorByPixel(getPx(texCoord.x), getPy(texCoord.y));
	}

	/**
	 * Returns the color of the given pixel in the source image.
	 */
	protected Color3f getColorByPixel(int px, int py) {
		try {
			//py = getHeight()-py-1;
			int pixel = image.getRGB(px, py);
			//int alpha = (pixel >> 24) & 0xff;
		    int red = (pixel >> 16) & 0xff;
		    int green = (pixel >> 8) & 0xff;
		    int blue = (pixel) & 0xff;
		    return new Color3f(red, green, blue);
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Height: " + getHeight());
			System.out.println("Width: " + getWidth());
			System.out.println("Out of bounds: " + new TexCoord2f(px, py));
		}
		return new Color3f(1, 0, 0); //mark errors red
	}
	
	@Override
	public String toString() {
		return src;
	}
}
