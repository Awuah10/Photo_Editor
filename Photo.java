package com.photo_editor;

import javax.swing.ImageIcon;

/*
 * this class holds the image object
 */
public class Photo {
	
	private ImageIcon image;
	private String path;

	public ImageIcon getImageIcon() {
		return image;
	}


	public void setImageIcon(ImageIcon image) {
		this.image = image;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}

}
