package xml;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import tuples.Color3f;
import tuples.Point3f;
import tuples.TexCoord2f;
import tuples.Vector3f;


import javax.xml.parsers.*;

public final class Parser extends DefaultHandler
{
    /**
     * The locator of the document being parsed.  
     */

    private Locator locator = null;


    /**
     * The user's parser handler. 
     */

    private ParserHandler handler = null;


    /**
     * The echo handler.
     */

    private ParserHandler echoHandler = null;


    /**
     * Set the user's parser handler.
     * @param handler The user's parser handler.
     */

    public void setHandler(ParserHandler handler)
    {
        this.handler = handler;
    }


    /**
     * Parse a document and call the user's parser handler. 
     * @param input The input.
     * @param validate Indicates if the document needs to be validated. Set this to true.
     * @param echo Indicates wether the document needs to be echod to stdout.
     * @return a boolean indicating if the parse was successful.
     */

    public boolean parse(InputSource input, boolean validate, boolean echo)
    {
        echoHandler = echo == true ? new EchoParserHandler() : null;

        try
        {
            // use the default parser
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(validate);

            // we are the SAX event handler
            DefaultHandler handler = this;

            // parse the input
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(input, handler);
        }
        catch (SAXParseException e)
        {
            // report the exception and return false
            e.printStackTrace();
            System.err.print("ERROR : " + e.getMessage());
            System.err.println((e.getLineNumber() > 0 ? (" (line " + e.getLineNumber() + ")") : ""));
            return false;
        }
        catch (Exception e)
        {
            // report the exception and return false
            e.printStackTrace();
            System.err.println("ERROR : " + e.getMessage());
            return false;
        }

        // the parse has succeeded
        return true;
    }


    /**
     * SAX2 event handler.
     * Receive notification of the start of an element.
     * You should not call this method, it is called by the SAX2 parser.
     */

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        try
        {
            // Sdl element
            if (qName.equals("Sdl"))
            {
                // call handler
                if (    echoHandler != null) echoHandler.startSdl();
                if (    handler != null) handler.startSdl();
            }
            // Cameras element
            else if (qName.equals("Cameras"))
            {
                // call handler
                if (echoHandler != null) echoHandler.startCameras();
                if (    handler != null) handler.startCameras();
            }
            // Camera element
            else if (qName.equals("Camera"))
            {
                // parse position attribute
                String positionString = attributes.getValue("position");
                if (positionString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"position\".");
                Point3f position = ParserUtils.parsePoint3f(positionString);

                // parse direction attribute
                String directionString = attributes.getValue("direction");
                if (directionString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"direction\".");
                Vector3f direction = ParserUtils.parseVector3f(directionString);

                // parse up attribute
                String upString = attributes.getValue("up");
                if (upString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"up\".");
                Vector3f up = ParserUtils.parseVector3f(upString);

                // parse fovy attribute
                String fovyString = attributes.getValue("fovy");
                if (fovyString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"fovy\".");
                float fovy = ParserUtils.parseFloat(fovyString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startCamera(position, direction, up, fovy, name);
                if (    handler != null) handler.startCamera(position, direction, up, fovy, name);
            }
            // Lights element
            else if (qName.equals("Lights"))
            {
                // call handler
                if (echoHandler != null) echoHandler.startLights();
                if (    handler != null) handler.startLights();
            }
            // DirectionalLight element
            else if (qName.equals("DirectionalLight"))
            {
                // parse direction attribute
                String directionString = attributes.getValue("direction");
                if (directionString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"direction\".");
                Vector3f direction = ParserUtils.parseVector3f(directionString);

                // parse intensity attribute
                String intensityString = attributes.getValue("intensity");
                if (intensityString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"intensity\".");
                float intensity = ParserUtils.parseFloat(intensityString);

                // parse color attribute
                String colorString = attributes.getValue("color");
                if (colorString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"color\".");
                Color3f color = ParserUtils.parseColor3f(colorString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startDirectionalLight(direction, intensity, color, name);
                if (    handler != null) handler.startDirectionalLight(direction, intensity, color, name);
            }
            // PointLight element
            else if (qName.equals("PointLight"))
            {
                // parse position attribute
                String positionString = attributes.getValue("position");
                if (positionString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"position\".");
                Point3f position = ParserUtils.parsePoint3f(positionString);

                // parse intensity attribute
                String intensityString = attributes.getValue("intensity");
                if (intensityString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"intensity\".");
                float intensity = ParserUtils.parseFloat(intensityString);

                // parse color attribute
                String colorString = attributes.getValue("color");
                if (colorString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"color\".");
                Color3f color = ParserUtils.parseColor3f(colorString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startPointLight(position, intensity, color, name);
                if (    handler != null) handler.startPointLight(position, intensity, color, name);
            }
            // SpotLight element
            else if (qName.equals("SpotLight"))
            {
                // parse position attribute
                String positionString = attributes.getValue("position");
                if (positionString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"position\".");
                Point3f position = ParserUtils.parsePoint3f(positionString);

                // parse direction attribute
                String directionString = attributes.getValue("direction");
                if (directionString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"direction\".");
                Vector3f direction = ParserUtils.parseVector3f(directionString);

                // parse intensity attribute
                String angleString = attributes.getValue("angle");
                if (angleString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"angle\".");
                float angle = ParserUtils.parseFloat(angleString);

                // parse intensity attribute
                String intensityString = attributes.getValue("intensity");
                if (intensityString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"intensity\".");
                float intensity = ParserUtils.parseFloat(intensityString);

                // parse color attribute
                String colorString = attributes.getValue("color");
                if (colorString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"color\".");
                Color3f color = ParserUtils.parseColor3f(colorString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startSpotLight(position, direction, angle, intensity, color, name);
                if (    handler != null) handler.startSpotLight(position, direction, angle, intensity, color, name);
            }
            // Geometry element
            else if (qName.equals("Geometry"))
            {
                // call handler
                if (echoHandler != null) echoHandler.startGeometry();
                if (    handler != null) handler.startGeometry();
            }
            // Sphere element
            else if (qName.equals("Sphere"))
            {
            	// parse position attribute
                String positionString = attributes.getValue("position");
                if (positionString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"position\".");
                Point3f position = ParserUtils.parsePoint3f(positionString);
                
                // parse radius attribute
                String radiusString = attributes.getValue("radius");
                if (radiusString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"radius\".");
                float radius = ParserUtils.parseFloat(radiusString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startSphere(position, radius, name);
                if (    handler != null) handler.startSphere(position, radius, name);
            }
            // Cylinder element
            else if (qName.equals("Cylinder"))
            {
                // parse radius attribute
                String radiusString = attributes.getValue("radius");
                if (radiusString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"radius\".");
                float radius = ParserUtils.parseFloat(radiusString);

                // parse height attribute
                String heightString = attributes.getValue("height");
                if (heightString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"height\".");
                float height = ParserUtils.parseFloat(heightString);

                // parse capped attribute
                String cappedString = attributes.getValue("capped");
                if (cappedString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"capped\".");
                boolean capped = ParserUtils.parseBoolean(cappedString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startCylinder(radius, height, capped, name);
                if (    handler != null) handler.startCylinder(radius, height, capped, name);
            }
            // Cone element
            else if (qName.equals("Cone"))
            {
                // parse radius attribute
                String radiusString = attributes.getValue("radius");
                if (radiusString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"radius\".");
                float radius = ParserUtils.parseFloat(radiusString);

                // parse height attribute
                String heightString = attributes.getValue("height");
                if (heightString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"height\".");
                float height = ParserUtils.parseFloat(heightString);

                // parse capped attribute
                String cappedString = attributes.getValue("capped");
                if (cappedString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"capped\".");
                boolean capped = ParserUtils.parseBoolean(cappedString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startCone(radius, height, capped, name);
                if (    handler != null) handler.startCone(radius, height, capped, name);
            }
            // Torus element
            else if (qName.equals("Torus"))
            {
                // parse radius attribute
                String innerRadiusString = attributes.getValue("innerRadius");
                if (innerRadiusString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"innerRadius\".");
                float innerRadius = ParserUtils.parseFloat(innerRadiusString);

                // parse radius attribute
                String outerRadiusString = attributes.getValue("outerRadius");
                if (outerRadiusString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"outerRadius\".");
                float outerRadius = ParserUtils.parseFloat(outerRadiusString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startTorus(innerRadius, outerRadius, name);
                if (    handler != null) handler.startTorus(innerRadius, outerRadius, name);
            }
            // Teapot element
            else if (qName.equals("Teapot"))
            {
                // parse size attribute
                String sizeString = attributes.getValue("size");
                if (sizeString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"size\".");
                float size = ParserUtils.parseFloat(sizeString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startTeapot(size, name);
                if (    handler != null) handler.startTeapot(size, name);
            }
            // Bunny element
            else if (qName.equals("Bunny"))
            {
                // parse size attribute
                String sizeString = attributes.getValue("size");
                if (sizeString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"size\".");
                float size = ParserUtils.parseFloat(sizeString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startBunny(size, name);
                if (    handler != null) handler.startBunny(size, name);
            }
            // Object element
            else if (qName.equals("Object"))
            {
            	// parse src attribute
                String src = attributes.getValue("src");
                if (src == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"src\".");

                // parse size attribute
                String sizeString = attributes.getValue("size");
                if (sizeString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"size\".");
                float size = ParserUtils.parseFloat(sizeString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startObject(src, size, name);
                if (    handler != null) handler.startObject(src, size, name);
            }
            // Disk element
            else if (qName.equals("Disk"))
            {
                // parse radius attribute
                String radiusString = attributes.getValue("radius");
                if (radiusString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"radius\".");
                float radius = ParserUtils.parseFloat(radiusString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");
                
                // parse normal attribute
                String directionString = attributes.getValue("normal");
                if (directionString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"normal\".");
                Vector3f normal = ParserUtils.parseVector3f(directionString);

                // call handler
                if (echoHandler != null) echoHandler.startDisk(radius, normal, name);
                if (    handler != null) handler.startDisk(radius, normal, name);
            }
            // Cornell Box
            else if (qName.equals("Cornell"))
            {
                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startCornell(name);
                if (    handler != null) handler.startCornell(name);
            }
            // IndexedTriangleSet element
            else if (qName.equals("IndexedTriangleSet"))
            {
                // parse coordinates
                String coordinatesString = attributes.getValue("coordinates");
                if (coordinatesString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"coordinates\".");
                Point3f [] coordinates = ParserUtils.parsePoint3fArray(coordinatesString);

                // parse normals (optional)
                Vector3f [] normals = ParserUtils.parseVector3fArray(attributes.getValue("normals"));

                // parse texture coordinats (optional)
                TexCoord2f [] textureCoordinates = ParserUtils.parseTexCoord2fArray(attributes.getValue("textureCoordinates"));

                // parse coordinate indices
                String coordinateIndicesString = attributes.getValue("coordinateIndices");
                if (coordinateIndicesString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"coordinateIndices\".");
                int [] coordinateIndices = ParserUtils.parseIntArray(coordinateIndicesString);

                // parse normal indices (optional)
                int [] normalIndices = 	ParserUtils.parseIntArray(attributes.getValue("normalIndices"));

                // parse texture coordinate indices (optional)
                int [] textureCoordinateIndices = ParserUtils.parseIntArray(attributes.getValue("textureCoordinateIndices"));

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startIndexedTriangleSet(coordinates, normals, textureCoordinates, coordinateIndices, normalIndices, textureCoordinateIndices, name);
                if (    handler != null) handler.startIndexedTriangleSet(coordinates, normals, textureCoordinates, coordinateIndices, normalIndices, textureCoordinateIndices, name);
            }
            // Textures element
            else if (qName.equals("Textures"))
            {
                // call handler
                if (echoHandler != null) echoHandler.startTextures();
                if (    handler != null) handler.startTextures();
            }
            // Texture element
            else if (qName.equals("Texture"))
            {
                // parse name attribute
                String src = attributes.getValue("src");
                if (src == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"src\".");

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startTexture(src, name);
                if (    handler != null) handler.startTexture(src, name);
            }
            // BumpTextures element
            else if (qName.equals("BumpTextures"))
            {
                // call handler
                if (echoHandler != null) echoHandler.startBumpTextures();
                if (    handler != null) handler.startBumpTextures();
            }
            // BumpTexture element
            else if (qName.equals("BumpTexture"))
            {
                // parse name attribute
                String src = attributes.getValue("src");
                if (src == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"src\".");

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startBumpTexture(src, name);
                if (    handler != null) handler.startBumpTexture(src, name);
            }
            // Materials element
            else if (qName.equals("Materials"))
            {
                // call handler
                if (echoHandler != null) echoHandler.startMaterials();
                if (    handler != null) handler.startMaterials();
            }
            // DiffuseMaterial element
            else if (qName.equals("DiffuseMaterial"))
            {
                // parse color attribute
                String colorString = attributes.getValue("color");
                if (colorString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"color\".");
                Color3f color = ParserUtils.parseColor3f(colorString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startDiffuseMaterial(color, name);
                if (    handler != null) handler.startDiffuseMaterial(color, name);
            }
            // PhongMaterial element
            else if (qName.equals("PhongMaterial"))
            {
                // parse color attribute
                String colorString = attributes.getValue("color");
                if (colorString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"color\".");
                Color3f color = ParserUtils.parseColor3f(colorString);

                // parse shininess attribute
                String shininessString = attributes.getValue("shininess");
                if (shininessString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"shininess\".");
                float shininess = ParserUtils.parseFloat(shininessString);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startPhongMaterial(color, shininess, name);
                if (    handler != null) handler.startPhongMaterial(color, shininess, name);
            }
            // LinearCombinedMaterial element
            else if (qName.equals("LinearCombinedMaterial"))
            {
                // parse material1 attribute
                String material1 = attributes.getValue("material1");
                if (material1 == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"material1\".");

                // parse weight1 attribute
                String weight1String = attributes.getValue("weight1");
                if (weight1String == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"weight1\".");
                float weight1 = ParserUtils.parseFloat(weight1String);

                // parse material2 attribute
                String material2 = attributes.getValue("material2");
                if (material2 == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"material2\".");

                // parse weight2 attribute
                String weight2String = attributes.getValue("weight2");
                if (weight2String == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"weight2\".");
                float weight2 = ParserUtils.parseFloat(weight2String);

                // parse name attribute
                String name = attributes.getValue("name");
                if (name == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"name\".");

                // call handler
                if (echoHandler != null) echoHandler.startLinearCombinedMaterial(material1, weight1, material2, weight2, name);
                if (    handler != null) handler.startLinearCombinedMaterial(material1, weight1, material2, weight2, name);
            }
            // Scene element
            else if (qName.equals("Scene"))
            {
                // parse camera attribute
                String camera = attributes.getValue("camera");
                if (camera == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"camera\".");

                // parse lights attribute
                String lightsString = attributes.getValue("lights");
                if (lightsString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"lights\".");
                String [] lights = ParserUtils.parseStringArray(lightsString);

                // parse color attribute
                String backgroundColorString = attributes.getValue("background");
                if (backgroundColorString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"background\".");
                Color3f background = ParserUtils.parseColor3f(backgroundColorString);

                // call handler
                if (echoHandler != null) echoHandler.startScene(camera, lights, background);
                if (    handler != null) handler.startScene(camera, lights, background);
            }
            // Shape element
            else if (qName.equals("Shape"))
            {
                // parse geometry attribute
                String geometry = attributes.getValue("geometry");
                if (geometry == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"geometry\".");

                // parse material attribute (optional)
                String material = attributes.getValue("material");

                // parse texture attribute (optional)
                String texture = attributes.getValue("texture");
                
                // parse bumptexture attribute (optional)
                String bumpTexture = attributes.getValue("bumptexture");

                // call handler
                if (echoHandler != null) echoHandler.startShape(geometry, material, texture, bumpTexture);
                if (    handler != null) handler.startShape(geometry, material, texture, bumpTexture);
            }
            // Rotate element
            else if (qName.equals("Rotate"))
            {
                // parse axis attribute
                String axisString = attributes.getValue("axis");
                if (axisString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"axis\".");
                Vector3f axis = ParserUtils.parseVector3f(axisString);

                // parse angle attribute
                String angleString = attributes.getValue("angle");
                if (angleString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"angle\".");
                float angle = ParserUtils.parseFloat(angleString);

                // call handler
                if (echoHandler != null) echoHandler.startRotate(axis, angle);
                if (    handler != null) handler.startRotate(axis, angle);
            }
            // Translate element
            else if (qName.equals("Translate"))
            {
                // parse vector attribute
                String vectorString = attributes.getValue("vector");
                if (vectorString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"vector\".");
                Vector3f vector = ParserUtils.parseVector3f(vectorString);

                // call handler
                if (echoHandler != null) echoHandler.startTranslate(vector);
                if (    handler != null) handler.startTranslate(vector);
            }
            // Scale element
            else if (qName.equals("Scale"))
            {
                // parse axis attribute
                String scaleString = attributes.getValue("scale");
                if (scaleString == null) throw new ParseException("Element \"" + qName + "\" requires attribute \"scale\".");
                Vector3f scale = ParserUtils.parseVector3f(scaleString);

                // call handler
                if (echoHandler != null) echoHandler.startScale(scale);
                if (    handler != null) handler.startScale(scale);
            }
            // Unknown element
            else
            {
                throw new ParseException("Unknown element \"" + qName + "\".");
            }
        }
        // catch any exception and turn it into a SAXParseException
        catch (Exception exception)
        {
            exception.printStackTrace();
            throw new SAXParseException(null, locator, exception);
        }
    }


    /**
     * SAX2 event handler.
     * Receive notification of the end of an element.
     * You should not call this method, it is called by the SAX2 parser.
     */

    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        try
        {
            // Sdl element
            if (qName.equals("Sdl"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endSdl();
                if (    handler != null) handler.endSdl();
            }
            // Cameras element
            else if (qName.equals("Cameras"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endCameras();
                if (    handler != null) handler.endCameras();
            }
            // Camera element
            else if (qName.equals("Camera"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endCamera();
                if (    handler != null) handler.endCamera();
            }
            // Lights element
            else if (qName.equals("Lights"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endLights();
                if (    handler != null) handler.endLights();
            }
            // DirectionalLight element
            else if (qName.equals("DirectionalLight"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endDirectionalLight();
                if (    handler != null) handler.endDirectionalLight();
            }
            // PointLight element
            else if (qName.equals("PointLight"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endPointLight();
                if (    handler != null) handler.endPointLight();
            }
            // SpotLight element
            else if (qName.equals("SpotLight"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endSpotLight();
                if (    handler != null) handler.endSpotLight();
            }
            // Geometry element
            else if (qName.equals("Geometry"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endGeometry();
                if (    handler != null) handler.endGeometry();
            }
            // Sphere element
            else if (qName.equals("Sphere"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endSphere();
                if (    handler != null) handler.endSphere();
            }
            // Cylinder element
            else if (qName.equals("Cylinder"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endCylinder();
                if (    handler != null) handler.endCylinder();
            }
            // Cone element
            else if (qName.equals("Cone"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endCone();
                if (    handler != null) handler.endCone();
            }
            // Torus element
            else if (qName.equals("Torus"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endTorus();
                if (    handler != null) handler.endTorus();
            }
            // Teapot element
            else if (qName.equals("Teapot"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endTeapot();
                if (    handler != null) handler.endTeapot();
            }
            // Bunny element
            else if (qName.equals("Bunny"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endBunny();
                if (    handler != null) handler.endBunny();
            }
            // Object element
            else if (qName.equals("Object"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endObject();
                if (    handler != null) handler.endObject();
            }
            // Disk element
            else if (qName.equals("Disk"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endDisk();
                if (    handler != null) handler.endDisk();
            }
            //Cornell Box
            else if (qName.equals("Cornell"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endCornell();
                if (    handler != null) handler.endCornell();
            }
            // IndexedTriangleSet element
            else if (qName.equals("IndexedTriangleSet"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endIndexedTriangleSet();
                if (    handler != null) handler.endIndexedTriangleSet();
            }
            // Textures element
            else if (qName.equals("Textures"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endTextures();
                if (    handler != null) handler.endTextures();
            }
            // Texture element
            else if (qName.equals("Texture"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endTexture();
                if (    handler != null) handler.endTexture();
            }
            // BumpTextures element
            else if (qName.equals("BumpTextures"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endBumpTextures();
                if (    handler != null) handler.endBumpTextures();
            }
            // BumpTexture element
            else if (qName.equals("BumpTexture"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endBumpTexture();
                if (    handler != null) handler.endBumpTexture();
            }
            // Materials element
            else if (qName.equals("Materials"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endMaterials();
                if (    handler != null) handler.endMaterials();
            }
            // DiffuseMaterial element
            else if (qName.equals("DiffuseMaterial"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endDiffuseMaterial();
                if (    handler != null) handler.endDiffuseMaterial();
            }
            // PhongMaterial element
            else if (qName.equals("PhongMaterial"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endPhongMaterial();
                if (    handler != null) handler.endPhongMaterial();
            }
            // LinearCombinedMaterial element
            else if (qName.equals("LinearCombinedMaterial"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endLinearCombinedMaterial();
                if (    handler != null) handler.endLinearCombinedMaterial();
            }
            // Scene element
            else if (qName.equals("Scene"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endScene();
                if (    handler != null) handler.endScene();
            }
            // Shape element
            else if (qName.equals("Shape"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endShape();
                if (    handler != null) handler.endShape();
            }
            // Rotate element
            else if (qName.equals("Rotate"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endRotate();
                if (    handler != null) handler.endRotate();
            }
            // Translate element
            else if (qName.equals("Translate"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endTranslate();
                if (    handler != null) handler.endTranslate();
            }
            // Scale element
            else if (qName.equals("Scale"))
            {
                // call handler
                if (echoHandler != null) echoHandler.endScale();
                if (    handler != null) handler.endScale();
            }
            // Unknown element
            else
            {
                throw new ParseException("Unknown element \"" + qName + "\".");
            }
        }
        // catch any exception and turn it into a SAXParseException
        catch (Exception exception)
        {
            throw new SAXParseException(null, locator, exception);
        }

    }

    /**
     * SAX2 event handler.
     * Receive an object for locating the origin of SAX document events.
     * You should not call this method, it is called by the SAX2 parser.
     */

    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }


    /**
     * SAX2 event handler.
     * Receive notification of a parser warning.
     * You should not call this method, it is called by the SAX2 parser.
     */

    public void warning(SAXParseException e)
    {
        // report the warning and continue
        System.err.println("SDLParser.warning() : WARNING : " + e);
    }


    /**
     * SAX2 event handler.
     * Receive notification of a recoverable parser error.
     * You should not call this method, it is called by the SAX2 parser.
     */

    public void error(SAXParseException e) throws SAXParseException
    {
        // abort the parse anyway
        throw e;
    }

    /**
     * SAX2 event handler.
     * Report a fatal XML parsing error.
     * You should not call this method, it is called by the SAX2 parser.
     */

    public void fatalError(SAXParseException e) throws SAXParseException
    {
        // abort the parse
        throw e;
    }
}
