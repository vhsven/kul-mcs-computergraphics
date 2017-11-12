package xml;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import lights.*;
import matrices.*;
import org.xml.sax.InputSource;
import common.Camera;
import common.Scene;
import raytracer.Intersectable;
import shapes.Shape;
import shapes.geometry.*;
import shapes.materials.*;
import shapes.textures.BumpTexture;
import shapes.textures.Texture;
import tuples.Color3f;
import tuples.Point3f;
import tuples.TexCoord2f;
import tuples.Vector3f;

/**
  * Class used to build a scene from a given sdl file.
  * Implements the ParserHandler interface (these methods
  * need to be filled in by you).
  * 
  * Note that this class keeps the absolute path to the
  * directory where the sdl file was found.  If you put your
  * textures in the same directory, you can use this path
  * to construct an absolute file name for each texture.
  * You will probably need absolute file names when loading
  * the texture.
  */
public class SceneBuilder implements ParserHandler
{

	private final boolean ECHO = false;
    public Scene scene;
    
    private HierarchicalBoundingBoxNode rootNode;
    public HierarchicalBoundingBoxNode getRootNode() { return rootNode; }
    
    private ArrayList<Shape> shapes = new ArrayList<Shape>();
    public ArrayList<Shape> getShapes() { return shapes; }
    
    public SceneBuilder(String filename) throws FileNotFoundException
    {
    	loadScene(filename);
    	terminate();
    }
    
    /**
     * Keep scene and shapes (with their finalized matrix copy).
     * Prepare the rest for GC.
     */
    private void terminate()
    {
    	cameras.clear();
    	cameras = null;
    	geometries.clear();
    	geometries = null;
    	lights.clear();
    	lights = null;
    	materials.clear();
    	materials = null;
    	textures.clear();
    	textures = null;
    	stack.clear();
    	stack = null;
    	System.gc();
    }

    /**
     * Load a scene.
     * @param filename The name of the file that contains the scene.
     * @return The scene, or null if something went wrong.
     * @throws FileNotFoundException The file could not be found.
     */
    private void loadScene(String filename) throws FileNotFoundException
    {
        File file = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(file);

        // set the system id so that the dtd can be a relative path
        // the first 2 lines of your sdl file should always be
        //    <?xml version='1.0' encoding='utf-8'?>
        //    <!DOCTYPE Sdl SYSTEM "sdl.dtd">
        // and sdl.dtd should be in the same directory as the dtd
        // if you experience dtd problems, commend the doctype declaration
        //    <!-- <!DOCTYPE Sdl SYSTEM "sdl.dtd"> -->
        // and disable validation (see further)
        // although this is in general not a good idea

        InputSource inputSource = new InputSource(fileInputStream);
        inputSource.setSystemId("file:///" + (file.getParentFile().getAbsolutePath() + "/") + "/");

        Parser parser = new Parser();
        parser.setHandler(this);

        boolean parseSuccessful = parser.parse(inputSource, /* validate */ true, /* echo */ ECHO);
        if(!parseSuccessful)
        {
        	System.err.println("Parsing Failed!");
        	System.exit(1);
        } else
        {
        	List<Intersectable> intersectables = new ArrayList<Intersectable>(shapes);
        	rootNode = new HierarchicalBoundingBoxNode(intersectables, 0, null);
        }
    }

    public void startSdl() throws Exception {}

    public void endSdl() throws Exception {}

    public void startCameras() throws Exception {}

    public void endCameras() throws Exception {}

    private HashMap<String, Camera> cameras = new HashMap<String, Camera>();
    public void startCamera(Point3f position, Vector3f direction, Vector3f up, float fovy, String name) throws Exception
    {
    	cameras.put(name, new Camera(position, direction, up, fovy));
    }

    public void endCamera() throws Exception {}

    public void startLights() throws Exception {}

    public void endLights() throws Exception {}

    private HashMap<String, Light> lights = new HashMap<String, Light>();
    
    public void startDirectionalLight(Vector3f direction, float intensity, Color3f color, String name) throws Exception
    {
    	lights.put(name, new DirectionalLight(intensity, color, direction));
    }

    public void endDirectionalLight() throws Exception {}

    public void startPointLight(Point3f position, float intensity, Color3f color, String name) throws Exception
    {
    	lights.put(name, new PointLight(position, intensity, color));
    }

    public void endPointLight() throws Exception {}

    public void startSpotLight(Point3f position, Vector3f direction, float angle, float intensity, Color3f color, String name) throws Exception
    {
    	lights.put(name, new SpotLight(position, direction, angle, intensity, color));
    }

    public void endSpotLight() throws Exception {}

    private HashMap<String, Geometry> geometries = new HashMap<String, Geometry>();
    public void startGeometry() throws Exception {}

    public void endGeometry() throws Exception {}

    public void startSphere(Point3f position, float radius, String name) throws Exception
    {
    	Sphere sphere = new Sphere(position, radius);
    	geometries.put(name, sphere);
    }

    public void endSphere() throws Exception {}

    public void startCylinder(float radius, float height, boolean capped, String name) throws Exception
    {
    	Cylinder cylinder = new Cylinder(new Point3f(0,-height/2,0), radius, height, capped);
    	geometries.put(name, cylinder);
    }

    public void endCylinder() throws Exception {}

    public void startCone(float radius, float height, boolean capped, String name) throws Exception
    {
    	Cone cone = new Cone(new Point3f(0, height, 0), radius, height, capped);
    	geometries.put(name, cone);
    }

    public void endCone() throws Exception {}

    public void startTorus(float innerRadius, float outerRadius, String name) throws Exception
    {
    	IndexedTriangleSet torus = new IndexedTriangleSet("assets/objects/torus.obj");
    	torus.setName(name);
    	geometries.put(name, torus);
    }

    public void endTorus() throws Exception {}

    public void startTeapot(float size, String name) throws Exception
    {
    	IndexedTriangleSet teapot = new IndexedTriangleSet("assets/objects/teapot.obj");
    	teapot.setName(name);
//    	ScalingMatrix scaling = new ScalingMatrix(size, size, size);
//    	scaling.transform(teapot);
    	geometries.put(name, teapot);
    }

    public void endTeapot() throws Exception {}
    
    public void startBunny(float size, String name) throws Exception
    {
    	IndexedTriangleSet bunny = new IndexedTriangleSet("assets/objects/bunny.obj");
    	bunny.setName(name);
//    	ScalingMatrix scaling = new ScalingMatrix(size, size, size);
//    	scaling.transform(bunny);
    	geometries.put(name, bunny);
    }

    public void endBunny() throws Exception {}
    
    public void startObject(String src, float size, String name) throws Exception
    {
    	IndexedTriangleSet obj = new IndexedTriangleSet(src);
    	obj.setName(name);
//    	ScalingMatrix scaling = new ScalingMatrix(size, size, size);
//    	scaling.transform(obj);
    	geometries.put(name, obj);
    }
    
    public void startDisk(float radius, Vector3f normal, String name) throws Exception
    {
    	Disk disk = new Disk(new Point3f(0, 0, 0), radius, normal);
    	geometries.put(name, disk);
    }

    public void endDisk() throws Exception {}

    public void endObject() throws Exception {}
    
    public void startCornell(String name) throws Exception
    {
    	IndexedTriangleSet cornell = new IndexedTriangleSet("assets/objects/cornellbox.obj");
    	cornell.setName(name);
    	geometries.put(name, cornell);
    }

    public void endCornell() throws Exception {}

    public void startIndexedTriangleSet(Point3f [] coordinates, Vector3f [] normals, TexCoord2f [] textureCoordinates, int [] coordinateIndices, int [] normalIndices, int [] textureCoordinateIndices, String name) throws Exception
    {
    	IndexedTriangleSet its = new IndexedTriangleSet(coordinates, normals, textureCoordinates, coordinateIndices, normalIndices, textureCoordinateIndices, name);
    	geometries.put(name, its);
    }

    public void endIndexedTriangleSet() throws Exception {}

    public void startTextures() throws Exception {}

    public void endTextures() throws Exception {}
    
    public void startBumpTextures() throws Exception {}

    public void endBumpTextures() throws Exception {}

    private HashMap<String, Texture> textures = new HashMap<String, Texture>();
    public void startTexture(String src, String name) throws Exception
    {
    	textures.put(name, new Texture(src));
    }

    public void endTexture() throws Exception {}
    
    private HashMap<String, BumpTexture> bumpTextures = new HashMap<String, BumpTexture>();
    public void startBumpTexture(String src, String name) throws Exception
    {
    	bumpTextures.put(name, new BumpTexture(src));
    }

    public void endBumpTexture() throws Exception {}

    public void startMaterials() throws Exception {}

    public void endMaterials() throws Exception {}

    private HashMap<String, Material> materials = new HashMap<String, Material>();
    public void startDiffuseMaterial(Color3f color, String name) throws Exception
    {
    	materials.put(name, new DiffuseMaterial(color));
    }

    public void endDiffuseMaterial() throws Exception {}

    public void startPhongMaterial(Color3f color, float shininess, String name) throws Exception
    {
    	materials.put(name, new PhongMaterial(color, shininess));
    }

    public void endPhongMaterial() throws Exception {}

    public void startLinearCombinedMaterial(String material1Name, float weight1, String material2Name, float weight2, String name) throws Exception
    {
    	assert weight1 + weight2 == 1;
    	
    	Material newMaterial;
    	Material mat1 = materials.get(material1Name);
    	Material mat2 = materials.get(material2Name);
    	Color3f color1 = mat1.getColor();
    	Color3f color2 = mat2.getColor();
    	float r = weight1 * color1.x + weight2 * color2.x;
    	float g = weight1 * color1.y + weight2 * color2.y;
    	float b = weight1 * color1.z + weight2 * color2.z;
    	if(mat1 instanceof PhongMaterial && mat2 instanceof PhongMaterial)
    	{
    		PhongMaterial phong1 = (PhongMaterial)mat1;
    		PhongMaterial phong2 = (PhongMaterial)mat2;
    		newMaterial = new PhongMaterial(r, g, b, weight1*phong1.getShininess() + weight2*phong2.getShininess());
    	}
    	else if(mat1 instanceof DiffuseMaterial && mat2 instanceof DiffuseMaterial)
    	{
    		newMaterial = new DiffuseMaterial(r, g, b);
    	}
    	else if(mat1 instanceof PhongMaterial && mat2 instanceof DiffuseMaterial)
    	{
    		PhongMaterial phong = (PhongMaterial)mat1;
    		newMaterial = new PhongMaterial(r, g, b, phong.getShininess());
    	}
    	else if(mat1 instanceof DiffuseMaterial && mat2 instanceof PhongMaterial)
    	{
    		PhongMaterial phong = (PhongMaterial)mat2;
    		newMaterial = new PhongMaterial(r, g, b, phong.getShininess());
    	} else
    	{
    		assert false;
    		newMaterial = null;
    	}
    	materials.put(name, newMaterial);
    }

    public void endLinearCombinedMaterial() throws Exception {}

    public void startScene(String cameraName, String [] lightNames, Color3f background) throws Exception
    {
    	Camera cam = cameras.get(cameraName);
    	HashSet<Light> myLights = new HashSet<Light>();
    	for(int i=0; i < lightNames.length; i++)
    		myLights.add(lights.get(lightNames[i]));
    	scene = new Scene(cam, background, myLights);
    }

    public void endScene() throws Exception {}

    public void startShape(String geometryName, String materialName, String textureName, String bumpTextureName) throws Exception
    {
    	Geometry geo = geometries.get(geometryName);
    	geo.setName(geometryName);
    	Material mat = materials.get(materialName);
    	Texture tex = textures.get(textureName);
    	BumpTexture bump = bumpTextures.get(bumpTextureName);
    	Shape shape = new Shape(geo, mat, tex, bump);
    	shape.setTransformations(stack);
    	shapes.add(shape);
    }

    public void endShape() throws Exception {}

    MatrixStack stack = new MatrixStack();
    public void startRotate(Vector3f axis, float angle) throws Exception
    {
    	stack.push(new RotationMatrix(axis, angle));
    }

    public void endRotate() throws Exception
    {
    	stack.pop();
    }

    public void startTranslate(Vector3f vector) throws Exception
    {
    	stack.push(new TranslationMatrix(vector.x, vector.y, vector.z));
    }

    public void endTranslate() throws Exception
    {
    	stack.pop();
    }

    public void startScale(Vector3f scale) throws Exception
    {
    	stack.push(new ScalingMatrix(scale.x, scale.y, scale.z));
    }

    public void endScale() throws Exception
    {
    	stack.pop();
    }

}
