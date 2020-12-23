package resources;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * this class will load the images from the resource.images folder
 */
public class ResourceLoader {
 
	/**
	 * loads resource images
	 * @param imageName
	 * @return
	 */
    public  Image loadImage(String imageName)
    {
    	BufferedImage image = null;
    	try {
    		image = ImageIO.read(this.getClass().getResourceAsStream("images/"+imageName));
    	} 
    	catch (IOException e) {
    	    e.printStackTrace();
    	}
    	return image;
    }
}