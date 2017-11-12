package shapes.geometry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import raytracer.IntersectRecord;
import raytracer.Ray;
import tuples.Point3f;
import tuples.TexCoord2f;
import tuples.Vector3f;

import common.BoundingBox3D;


/**
 * Implements a datastructure similar to the Indexed Triangle Set.
 */
public class IndexedTriangleSet extends Geometry implements Iterable<Triangle> {

	public HashSet<Triangle> triangles = new HashSet<Triangle>();
	//public ArrayList<Triangle> triangles = new ArrayList<Triangle>();
	
	private float 	minX = Float.POSITIVE_INFINITY, 
					minY = Float.POSITIVE_INFINITY, 
					minZ = Float.POSITIVE_INFINITY, 
					maxX = Float.NEGATIVE_INFINITY, 
					maxY = Float.NEGATIVE_INFINITY, 
					maxZ = Float.NEGATIVE_INFINITY;
	
	private void updateBounds(float x, float y, float z)
	{
		if(x < minX)
			minX = x;
		else if(x > maxX)
			maxX = x;
		
		if(y < minY)
			minY = y;
		else if(y > maxY)
			maxY = y;
		
		if(z < minZ)
			minZ = z;
		else if(z > maxZ)
			maxZ = z;
	}
	
	private Point3f getCenter()
	{
		return new Point3f((maxX + minX)/2, (maxY + minY)/2, (maxZ + minZ)/2);
	}
	
	private Sphere circumSphere = null;
	private void generateCircumSphere()
	{
		if(circumSphere != null)
			return;
		
		Point3f center = getCenter();
		Point3f max = new Point3f(maxX, maxY, maxZ);
		Point3f min = new Point3f(minX, minY, minZ);
		Vector3f c_max = max.minus(center);
		Vector3f c_min = min.minus(center);
		float lengthMax = c_max.getNorm();
		float lengthMin = c_min.getNorm();
		if(lengthMax < lengthMin)
			circumSphere = new Sphere(center, lengthMin);
		else
			circumSphere = new Sphere(center, lengthMax);
	}
	
	private String name = null;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IndexedTriangleSet(	Point3f[] coordinates, 
								Vector3f[] normals, 
								TexCoord2f[] textureCoordinates, 
								int[] coordinateIndices, 
								int[] normalIndices, 
								int[] textureCoordinateIndices,
								String name)
	{
		for(int i=0; i < coordinateIndices.length; i++)
		{
			Point3f v = coordinates[coordinateIndices[i]];
			updateBounds(v.x, v.y, v.z);
		}
		
		generateCircumSphere();
		
		for(int i=0; i < coordinateIndices.length; i += 3)
		{
			Point3f v1 = coordinates[coordinateIndices[i]];
			Point3f v2 = coordinates[coordinateIndices[i+1]];
			Point3f v3 = coordinates[coordinateIndices[i+2]];
			
			Vector3f n1 = normals[normalIndices[i]];
			Vector3f n2 = normals[normalIndices[i+1]];
			Vector3f n3 = normals[normalIndices[i+2]];
			
			Triangle triangle = new Triangle(v1, v2, v3, n1, n2, n3);
			
			try
			{
				TexCoord2f t1 = textureCoordinates[textureCoordinateIndices[i]];
				TexCoord2f t2 = textureCoordinates[textureCoordinateIndices[i+1]];
				TexCoord2f t3 = textureCoordinates[textureCoordinateIndices[i+2]];
				triangle.setTextureCoordinates(t1, t2, t3);
			}
			catch(Exception e)
			{
				generateTextureCoordinates(triangle);
			}
			
			triangles.add(triangle);
		}
		this.name = name;
	}
	
	public IndexedTriangleSet(File objFile)
	{
		ArrayList<Point3f> objVertices = new ArrayList<Point3f>();
		ArrayList<TexCoord2f> objTextures = new ArrayList<TexCoord2f>();
		ArrayList<Vector3f> objNormals = new ArrayList<Vector3f>();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(objFile));
			String line;
			while((line = reader.readLine()) != null)
			{
				if(line.equals(""))
					continue;
				
				StringTokenizer tokenizer = new StringTokenizer(line, " ");
				String identifier = tokenizer.nextToken();
				if(identifier.startsWith("#")) {} //ignore
				else if(identifier.equals("v"))
				{
					float x = Float.parseFloat(tokenizer.nextToken());
					float y = Float.parseFloat(tokenizer.nextToken());
					float z = Float.parseFloat(tokenizer.nextToken());
					Point3f v = new Point3f(x, y, z);
					objVertices.add(v);
					
					updateBounds(x, y, z);
				}
				else if(identifier.equals("vt"))
				{
					float u = Float.parseFloat(tokenizer.nextToken());
					float v = Float.parseFloat(tokenizer.nextToken());
					TexCoord2f tex = new TexCoord2f(u, v);
					objTextures.add(tex);
				}
				else if(identifier.equals("vn"))
				{
					float x = Float.parseFloat(tokenizer.nextToken());
					float y = Float.parseFloat(tokenizer.nextToken());
					float z = Float.parseFloat(tokenizer.nextToken());
					Vector3f normal = new Vector3f(x, y, z);
					objNormals.add(normal);
				}
				else if(identifier.equals("f"))
				{
					generateCircumSphere(); //once
					String c1 = tokenizer.nextToken(); //  1/1/1
					String c2 = tokenizer.nextToken(); //  2/2/2
					String c3 = tokenizer.nextToken(); //  3/3/3
					
					int[] c1Int = parseFaceCluster(c1); //[1 1 1]
					int[] c2Int = parseFaceCluster(c2); //[2 2 2]
					int[] c3Int = parseFaceCluster(c3); //[3 3 3]
					
					try
					{
						Point3f v1 = objVertices.get(c1Int[0]-1);
						Point3f v2 = objVertices.get(c2Int[0]-1);
						Point3f v3 = objVertices.get(c3Int[0]-1);
						
						Vector3f n1 = null; if(c1Int[2] != -1) n1 = objNormals.get(c1Int[2]-1);
						Vector3f n2 = null; if(c2Int[2] != -1) n2 = objNormals.get(c2Int[2]-1);
						Vector3f n3 = null; if(c3Int[2] != -1) n3 = objNormals.get(c3Int[2]-1);
						
						Triangle triangle = new Triangle(v1, v2, v3, n1, n2, n3);
						
						if(c1Int[1] != -1 && c2Int[1] != -1 && c3Int[1] != -1)
						{
							TexCoord2f t1 = objTextures.get(c1Int[1]-1);
							TexCoord2f t2 = objTextures.get(c2Int[1]-1);
							TexCoord2f t3 = objTextures.get(c3Int[1]-1);
							
							triangle.setTextureCoordinates(t1, t2, t3);
						} else //no textures given
							generateTextureCoordinates(triangle);
						
						triangles.add(triangle);
					}
					catch (IndexOutOfBoundsException e)
					{
						System.out.println(line);
						e.printStackTrace();
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("File " + objFile + " not found!");
			System.exit(0);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(reader != null)
			{
				try {
					reader.close();
				}
				catch (IOException e) {}
			}
		}
	}
	
	private void generateTextureCoordinates(Triangle triangle) {
		Ray ray = new Ray();
		IntersectRecord record = new IntersectRecord();
		ray.eye = getCenter();
		
		TexCoord2f t1 = generateTextureCoordinatesForVertex(triangle.vertex1, ray, record);
		TexCoord2f t2 = generateTextureCoordinatesForVertex(triangle.vertex2, ray, record);
		TexCoord2f t3 = generateTextureCoordinatesForVertex(triangle.vertex3, ray, record);
		triangle.setTextureCoordinates(t1, t2, t3);
	}
	
	private TexCoord2f generateTextureCoordinatesForVertex(Point3f vertex, Ray ray, IntersectRecord record)
	{
		ray.setDirection(vertex.minus(ray.eye).normalize());
		//ray.dir = vertex.minus(ray.eye).normalize();
		boolean intersect = circumSphere.intersectsWith(ray, 0, Double.POSITIVE_INFINITY, record);
		
		if(!intersect) assert false;
		
		Point3f loc = ray.getLocation(record.t);
		
//		System.out.println("sphere: " + circumSphere);
//		System.out.println("vertex: " + vertex);
//		System.out.println("ray:" + ray);
//		System.out.println("intersect: " + record.getInfo(ray));
//		System.out.println("texCoords: " + circumSphere.getTextureCoord(loc));
		
		return circumSphere.getTextureCoord(loc);
	}

	private int[] parseFaceCluster(String faceData)
	{
		int[] result = new int[3];
		//System.out.println(faceData);
		StringTokenizer tokenizer = new StringTokenizer(faceData, "/");
		int count = tokenizer.countTokens();
		if(count == 1) //1
		{
			result[0] = Integer.parseInt(faceData); //1
			result[1] = -1; //no texture
			result[2] = -1; //no normal
		}
		else if(count == 2) // 1//1
		{
			result[0] = Integer.parseInt(tokenizer.nextToken()); //vertex
			result[1] = -1; //texture
			result[2] = Integer.parseInt(tokenizer.nextToken()); //normal
		}
		else if(count == 3) // 1/1/1
		{
			result[0] = Integer.parseInt(tokenizer.nextToken()); //vertex
			result[1] = Integer.parseInt(tokenizer.nextToken()); //texture
			result[2] = Integer.parseInt(tokenizer.nextToken()); //normal
		}
		else assert false;
		
		return result;
	}
	
	public IndexedTriangleSet(String fileName)
	{
		this(new File(fileName));
	}
	
	@Override
	public String toString() {
		if(name == null)
			return "IndexedTriangleSet (" + triangles.size() + ")";
		return name;
	}
	
	public String toFullString() {
		return 	triangles.toString();
	}

	@Override
	public Iterator<Triangle> iterator() {
		return triangles.iterator();
	}

	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax, IntersectRecord record) {
		double oldMax = tMax;
		
		for(Triangle obj : triangles)
			if(obj.intersectsWith(ray, 0, tMax, record))
				tMax = record.t;
		
		
		return tMax < oldMax;
	}
	
	@Override
	public boolean intersectsWith(Ray ray, double tMin, double tMax) {
		for(Triangle obj : triangles)
			if(obj.intersectsWith(ray, tMin, tMax))
				return true;
		
		return false;
	}

	@Override
	public BoundingBox3D getBoundingBox3D() {
		return new BoundingBox3D(minX, maxX, minY, maxY, minZ, maxZ);
	}
}
