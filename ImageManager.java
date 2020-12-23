package com.photo_editor;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/*
 * this class loads and saves images from and to the hard drive.
 */

public class ImageManager {

	private JFrame frame;

	public ImageManager() {
		frame = new JFrame();
	}

	/**
	 *this method allows the user to get the image path 
	 *@returns a String 
	 */
	public String getImagePath() {

		File selectedFile = null;

		JFileChooser openFileChooser = new JFileChooser();
		openFileChooser.setDialogTitle("Load image file"); 
		openFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		openFileChooser.setAcceptAllFileFilterUsed(false);
		openFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "tiff","jpeg","bmp"));
		int result = openFileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			selectedFile = openFileChooser.getSelectedFile();
		}
		return selectedFile.getAbsolutePath();
	}

	/**
	 * allows the user to input a name to save the image on disk 
	 * @param Image
	 */
	public void saveImageAs(Image image) {

		JFileChooser saveFileChooser = new JFileChooser();
		saveFileChooser.setDialogTitle("Save image file");  
		saveFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

		int result = saveFileChooser.showSaveDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File saveFile = saveFileChooser.getSelectedFile();
			String name = saveFile.getName() + ".jpg";
			String extension = name.substring(1+name.lastIndexOf(".")).toLowerCase();
			try {
				ImageIO.write((RenderedImage) image, extension, saveFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * allows the user to save the current version of the image without changing name or directory
	 * @param Image
	 */
	public void saveImage(Image image) {
		
		//will cause buffer to overflow
		saveImageToArray(image);
		
		File saveFile = new File(System.getProperty("user.home"),"myPhoto.jpg");
		String name = saveFile.getName();
		String extension = name.substring(1+name.lastIndexOf(".")).toLowerCase();
		try {
			ImageIO.write((RenderedImage) image, extension, saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * create an array of images to test buffer overflow 
	 * @param image
	 * @return
	 */
	public Image[] saveImageToArray(Image image) {
		Image[] imageArray = new Image[10];
		
		for(int i=0; i<100; i++) {
			imageArray[i] = image;
		}
		return imageArray;
	}
	

	/**
	 * create an array of images to test  StackOverflow and for photo collection
	 * @param image
	 * @return
	 */
	public Image loadLabelImage(Image image) {
	Image[] imageArray = new Image[10];
	
	for(int i=0; i<imageArray.length; i++) {
		imageArray[i] = image;
	}
	
	loadLabelImage(image); //will cause recursion 
	return imageArray[0];
	
	}
	
}
