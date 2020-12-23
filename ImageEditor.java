package com.photo_editor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;


public class ImageEditor {

	/**
	 * rotates the selected image for the user
	 * @param image
	 * @return
	 */
	public Image rotateImage(BufferedImage image) {

		double rads = Math.toRadians(90);
		double sin = Math.abs(Math.sin(rads));
		double cos = Math.abs(Math.cos(rads));
		int width = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
		int height = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
		BufferedImage rotatedImage = new BufferedImage(width, height, image.getType());
		AffineTransform at = new AffineTransform();
		at.translate(width / 2, height / 2);
		at.rotate(rads,0, 0);
		at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
		AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		rotateOp.filter(image,rotatedImage);

		Image image2 = rotatedImage;

		return image2;
	}

	/**
	 * resizes an image
	 * @param originalImage
	 * @param scaledWidth
	 * @param scaledHeight
	 * @param preserveAlpha
	 * @return
	 */
	public BufferedImage createResizedImage(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha)
	{
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
		Graphics2D g = scaledBI.createGraphics();
		if (preserveAlpha) {
			g.setComposite(AlphaComposite.Src);
		}
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
		g.dispose();
		return scaledBI;
	}

	/**
	 *  makes an image transparent
	 * @param image
	 * @return
	 */
	public BufferedImage createTransparentImage(BufferedImage image) {

		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) newImage.getGraphics();
		g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f)); 
		g2d.drawImage(image, 0, 0, null);
		image = newImage;

		return image;
	}

	/**
	 * change the form of an image
	 * @param source
	 * @param brightnessPercentage
	 * @return
	 */
	public  Image changeImageForm( Image source, float brightnessPercentage ) {

		BufferedImage newImage = new BufferedImage(source.getWidth( null ), source.getHeight( null ), BufferedImage.TYPE_INT_ARGB );

		int[] pixel = { 0, 0, 0, 0 };
		float[] hsbvals = { 0, 0, 0 };

		newImage.getGraphics().drawImage( source, 0, 0, null );

		// Recalculate every pixel, changing the brightness
		for ( int i = 0; i < newImage.getHeight(); i++ ) {
			for ( int j = 0; j < newImage.getWidth(); j++ ) {

				// get the pixel data
				newImage.getRaster().getPixel( j, i, pixel );

				// converts its data to hsb to change brightness
				Color.RGBtoHSB( pixel[0], pixel[1], pixel[2], hsbvals );

				// create a new color with the changed brightness
				Color c = new Color( Color.HSBtoRGB( hsbvals[0], hsbvals[1], hsbvals[2] * brightnessPercentage ) );

				// set the new pixel
				newImage.getRaster().setPixel( j, i, new int[]{ c.getRed(), c.getGreen(), c.getBlue(), pixel[3] } );

			}

		}

		return newImage;

	}

	/**
	 *  allows user to write text on the image.
	 * @param bufferedImage
	 * @param text
	 */
	public void writeTextOnImage(BufferedImage bufferedImage, String text) {

		Graphics graphics = bufferedImage.getGraphics();
		graphics.setColor(Color.CYAN);
		graphics.fillRect(0, 0, 200, 50);
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("Arial Black", Font.BOLD, 20));
		graphics.drawString(text, 10, 25);

		try {
			ImageIO.write(bufferedImage, "jpg", new File( "image.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * compress the image file 
	 * @param sourceImage
	 * @throws IOException
	 */
	public void compressImageFile(String sourceImage) throws IOException {
		
		File input = new File(sourceImage);
		BufferedImage image = ImageIO.read(input);

		File compressedImageFile = new File("compressed_image.jpg");
		OutputStream os = new FileOutputStream(compressedImageFile);

		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter writer = (ImageWriter) writers.next();

		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		writer.setOutput(ios);

		ImageWriteParam param = writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(0.05f);  // Change the quality value you prefer
		writer.write(null, new IIOImage(image, null, null), param);

		os.close();
		ios.close();
		writer.dispose();
	}
}
