/**
 * Java tile grabbing class: Create ImageIcons from coordinates out of an image
 *
 * @author Protractor Ninja
 * @version 1.00 2011/2/21
 */

 import javax.swing.ImageIcon;
 import javax.imageio.ImageIO;
 import java.awt.image.BufferedImage;
 import java.awt.image.PixelGrabber;
 import java.awt.image.ImageObserver;
 import java.awt.Image;
 import java.io.File;
 import java.lang.Exception;
 import java.io.IOException;
 import java.net.URISyntaxException;
 import javax.swing.*;
 import java.awt.image.WritableRaster;

 import java.util.*;

public class TileGrabber {

	 private BufferedImage image;
	 private File imageFile;
	 private int tileWidth;
	 private int tileHeight;
	 private int[] pixels;

	 /*
	  * Creates a tile grabber object using a filepath and tile heights.
	  * Tiles should start at zero pixels.
	  */
	 public TileGrabber(String path, int tWidth, int tHeight) throws IOException, URISyntaxException {
	 	try {
			path = "/" + path.replaceAll("^/", "");
			image = ImageIO.read(this.getClass().getResourceAsStream("/" + path.replaceAll("^/", "")));
	 	} catch (Exception e) {
	 		throw new IOException(path + " does not exist.");
	 	}
	 	tileWidth = tWidth;
		tileHeight = tHeight;
	 }

	 /*
	  * Same as before, but uses an already-created BufferedImage file.
	  */
	 public TileGrabber(BufferedImage img, int tWidth, int tHeight) {
		image = img;
		tileWidth = tWidth;
		tileHeight = tHeight;
	 }

	 public void setImagePath(String path) throws IOException {
	 	try {
	 		path = "/" + path.replaceAll("^/", "");
	 	} catch (Exception e) {
	 		throw new IOException(path + " does not exist.");
	 	}
	 }

	 /*
	  * Creates an ImageIcon object from tile coordinates on an image.
	  */
	 public ImageIcon grabTile(int x, int y, int scale) {
	 	x = x*tileWidth;
		y = y*tileHeight;
		BufferedImage bi = image.getSubimage(x,y,tileWidth,tileHeight);
		Image i = bi.getScaledInstance(tileWidth*scale, tileHeight*scale, Image.SCALE_FAST);
		return new ImageIcon(i);
	 }

	 /*
	  * Creates an Image from an array of pixels and specified heights.
	  * Retrieved and modified from a stackoverflow.com question
	  */
	 public Image getImageFromArray(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0,0,width,height,pixels,0, width);
        return image;
     }

     /**
      * For testing purposes.
      */
	 public static void main(String[] args) {
	 	try {
		 	JFrame window = new JFrame("Hello, this is a test");
		 	window.setBounds(0,0,550,500);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 		TileGrabber grabber = new TileGrabber("terrain.png", 16, 16);
	 		JLabel label = new JLabel(grabber.grabTile(12,8,5));
		 	window.getContentPane().add(label);
		 	window.setVisible(true);
	 	} catch (Exception e) {
	 		TeaUtils.buildExceptionDialog(e);
	 	}
	 }


}