package com.photo_editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import resources.ResourceLoader;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Stack;

import javax.swing.JLabel;

public class Editor_Gui {

	private JFrame frame;
	private ImageManager image_manager;
	private Photo photo;
	private JLabel imageLabel;
	private ResourceLoader load_resource;
	private ImageEditor editor;
	private Stack<Image> undoStack;
	private Stack<Image> redoStack;
	private PhotoDBconnector dbConnector;
	private JTextField textField;
	private JTextField textField_1;
	private boolean isNew;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Editor_Gui window = new Editor_Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Editor_Gui() {

		image_manager= new ImageManager();
		photo = new Photo();
		load_resource = new ResourceLoader();
		editor = new ImageEditor();
		undoStack = new Stack<Image>();
		redoStack = new Stack<Image>();
		dbConnector = new PhotoDBconnector();

		initialize();
		loadInitialImage();
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("PHOTO EDITOR");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground( Color.black);

		//image_manager.loadLabelImage(load_resource.loadImage("open.png"));
		//initialize();

		Icon open_icon = new ImageIcon(load_resource.loadImage("open.png"));

		JButton btnNewButton = new JButton(open_icon);
		btnNewButton.setBounds(10, 11, 54, 43);
		btnNewButton.setBorderPainted(false);
		btnNewButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {

				loadOriginalImage();

				Image img = photo.getImageIcon().getImage();

				Image dimg = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(),Image.SCALE_SMOOTH);
				ImageIcon imgIcon = new ImageIcon(dimg);

				imageLabel.setIcon(imgIcon);

				undoStack.push(dimg);
				redoStack.clear();

			}

			//gives user indication of what the button does 
			public void mouseEntered(MouseEvent arg0) {
				btnNewButton.setToolTipText("Select a Photo");
				btnNewButton.setBackground(Color.gray);
			}

			public void mouseExited(MouseEvent arg0) {
				btnNewButton.setBackground(Color.black);
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.setBackground(Color.black);

		imageLabel = new JLabel("");
		imageLabel.setBounds(74, 65, 444, 414);
		frame.getContentPane().add(imageLabel);

		Icon form_icon = new ImageIcon(load_resource.loadImage("transform.png"));

		JButton btnNewButton_5 = new JButton(form_icon);
		btnNewButton_5.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {

				if(photo.getImageIcon() == null) {

					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					Image brighterImage = editor.changeImageForm(photo.getImageIcon().getImage(), 1.2f);
					brighterImage = editor.createResizedImage(brighterImage,imageLabel.getWidth() , imageLabel.getHeight(), true);
					fillLabel(brighterImage);

					undoStack.push(brighterImage);
					redoStack.clear();
				}
			}

			public void mouseEntered(MouseEvent e) {
				btnNewButton_5.setToolTipText("transform photo");
				btnNewButton_5.setBackground(Color.gray);
			}

			public void mouseExited(MouseEvent e) {
				btnNewButton_5.setBackground(Color.black);
			}
		});
		btnNewButton_5.setBounds(367, 11, 54, 43);
		btnNewButton_5.setBorderPainted(false);
		frame.getContentPane().add(btnNewButton_5);
		btnNewButton_5.setBackground(Color.black);

		Icon save_icon = new ImageIcon(load_resource.loadImage("save.png"));

		JButton btnNewButton_1 = new JButton(save_icon);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if(photo.getImageIcon() == null) {

					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					//saves it in the home directory
					image_manager.saveImage(photo.getImageIcon().getImage());
				}
			}

			public void mouseEntered(MouseEvent e) {
				btnNewButton_1.setToolTipText("Save current Photo to Home");
				btnNewButton_1.setBackground(Color.gray);
			}

			public void mouseExited(MouseEvent e) {
				btnNewButton_1.setBackground(Color.black);
			}
		});
		btnNewButton_1.setBounds(74, 11, 54, 43);
		btnNewButton_1.setBorderPainted(false);
		frame.getContentPane().add(btnNewButton_1);
		btnNewButton_1.setBackground(Color.black);

		Icon compression_icon = new ImageIcon(load_resource.loadImage("compress.png"));

		JButton btnNewButton_6 = new JButton(compression_icon);
		btnNewButton_6.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(photo.getImageIcon() == null) {
					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					try {
						image_manager.saveImage(photo.getImageIcon().getImage());
						photo.setPath(System.getProperty("user.home") + File.separator + "myPhoto.jpg");
						editor.compressImageFile(photo.getPath());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					BufferedImage img;
					try {
						img = ImageIO.read(new File("compressed_image.jpg"));
						img = editor.createResizedImage(img,imageLabel.getWidth() , imageLabel.getHeight(), true);
						fillLabel(img);
						undoStack.push(img);
						redoStack.clear();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			public void mouseEntered(MouseEvent e) {
				btnNewButton_6.setToolTipText("compress photo");
				btnNewButton_6.setBackground(Color.gray);
			}

			public void mouseExited(MouseEvent e) {
				btnNewButton_6.setBackground(Color.black);
			}
		});
		btnNewButton_6.setBounds(441, 11, 54, 43);
		btnNewButton_6.setBorderPainted(false);
		frame.getContentPane().add(btnNewButton_6);
		btnNewButton_6.setBackground(Color.black);

		Icon saveAs_icon = new ImageIcon(load_resource.loadImage("save_as.png"));

		JButton btnNewButton_2 = new JButton(saveAs_icon);
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if(photo.getImageIcon() == null) {

					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					image_manager.saveImageAs(photo.getImageIcon().getImage());
				}
			}

			public void mouseEntered(MouseEvent e) {
				btnNewButton_2.setToolTipText("Save photo as..");
				btnNewButton_2.setBackground(Color.gray);
			}

			public void mouseExited(MouseEvent e) {
				btnNewButton_2.setBackground(Color.black);
			}
		});
		btnNewButton_2.setBounds(150, 11, 54, 43);
		btnNewButton_2.setBorderPainted(false);
		frame.getContentPane().add(btnNewButton_2);
		btnNewButton_2.setBackground(Color.black);

		Icon transparent_icon = new ImageIcon(load_resource.loadImage("transparent.png"));

		JButton btnNewButton_7 = new JButton(transparent_icon);
		btnNewButton_7.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if(photo.getImageIcon() == null) {

					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					BufferedImage transparentImage = editor.createTransparentImage((BufferedImage) photo.getImageIcon().getImage());
					transparentImage = editor.createResizedImage(transparentImage,imageLabel.getWidth() , imageLabel.getHeight(), true);
					fillLabel(transparentImage);
					undoStack.push(transparentImage);
					redoStack.clear();
				}
			}

			public void mouseEntered(MouseEvent e) {
				btnNewButton_7.setToolTipText("make photo transparent");
				btnNewButton_7.setBackground(Color.gray);
			}

			public void mouseExited(MouseEvent e) {
				btnNewButton_7.setBackground(Color.black);
			}
		});
		btnNewButton_7.setBounds(516, 11, 54, 43);
		btnNewButton_7.setBorderPainted(false);
		frame.getContentPane().add(btnNewButton_7);
		btnNewButton_7.setBackground(Color.black);

		Icon close_icon = new ImageIcon(load_resource.loadImage("delete.png"));

		JButton btnNewButton_3 = new JButton(close_icon);
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(photo.getImageIcon() == null) {

					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					//give user option to save before deleting
					int userOption = JOptionPane.showConfirmDialog(frame, "Do you want to save image before?");
					if(userOption == 0)
						image_manager.saveImage(photo.getImageIcon().getImage());
					else if(userOption == 1)
						removeLabelContent();
				}
			}

			public void mouseEntered(MouseEvent e) {
				btnNewButton_3.setToolTipText("delete photo");
				btnNewButton_3.setBackground(Color.gray);
			}

			public void mouseExited(MouseEvent e) {
				btnNewButton_3.setBackground(Color.black);
			}
		});
		btnNewButton_3.setBounds(220, 11, 54, 43);
		btnNewButton_3.setBorderPainted(false);
		frame.getContentPane().add(btnNewButton_3);
		btnNewButton_3.setBackground(Color.black);

		Icon rotate_icon = new ImageIcon(load_resource.loadImage("rotate.png"));

		JButton btnNewButton_8 = new JButton(rotate_icon);
		btnNewButton_8.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if(photo.getImageIcon() == null) {

					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					Image rotatedImage = editor.rotateImage((BufferedImage) (photo.getImageIcon()).getImage());
					rotatedImage= editor.createResizedImage(rotatedImage,imageLabel.getWidth() , imageLabel.getHeight(), true);
					fillLabel(rotatedImage);
					undoStack.push(rotatedImage);
					redoStack.clear();
				}
			}

			public void mouseEntered(MouseEvent e) {
				btnNewButton_8.setToolTipText("rotate photo");
				btnNewButton_8.setBackground(Color.gray);
			}

			public void mouseExited(MouseEvent e) {
				btnNewButton_8.setBackground(Color.black);
			}
		});
		btnNewButton_8.setBounds(594, 11, 54, 43);
		btnNewButton_8.setBorderPainted(false);
		frame.getContentPane().add(btnNewButton_8);
		btnNewButton_8.setBackground(Color.black);

		Icon comment_icon = new ImageIcon(load_resource.loadImage("comment.png"));

		JButton btnNewButton_4 = new JButton(comment_icon);
		btnNewButton_4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if(photo.getImageIcon() == null) {

					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					String text = JOptionPane.showInputDialog("Please enter the text you want to put on the image");
					if(text.isEmpty()) {
						JOptionPane.showMessageDialog(frame, "Please write a text in the space provided");
						text = JOptionPane.showInputDialog("Please enter the text you want to put on the image");
						fillLabel (photo.getImageIcon().getImage());
					}else {
						editor.writeTextOnImage((BufferedImage) photo.getImageIcon().getImage(), text);

						BufferedImage img = null;
						try {
							img = ImageIO.read(new File("image.jpg"));
						} catch (IOException e1) {
						}
						img = editor.createResizedImage(img, imageLabel.getWidth(), imageLabel.getHeight(), true);
						fillLabel(img);
						undoStack.push(img);
						redoStack.clear();
					}
				}
			}
			public void mouseEntered(MouseEvent e) {
				btnNewButton_4.setToolTipText("Write comment on photo");
				btnNewButton_4.setBackground(Color.gray);
			}

			public void mouseExited(MouseEvent e) {
				btnNewButton_4.setBackground(Color.black);
			}
		});
		btnNewButton_4.setBounds(290, 11, 54, 43);
		btnNewButton_4.setBorderPainted(false);
		frame.getContentPane().add(btnNewButton_4);
		btnNewButton_4.setBackground(Color.black);

		Icon resize_icon = new ImageIcon(load_resource.loadImage("resize.png"));

		JButton btnNewButton_9 = new JButton(resize_icon);
		btnNewButton_9.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if(photo.getImageIcon() == null) {

					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					int width = photo.getImageIcon().getIconWidth();
					String input = JOptionPane.showInputDialog("Please enter preferred width/height.\n Current Width: " + width);
					if(input.isEmpty()) {
						JOptionPane.showMessageDialog(frame, "please enter a preferred width first!");
						input = JOptionPane.showInputDialog("Please Enter a preferred width/height");
					}else {
						int width_height = Integer.parseInt(input);
						Image image = editor.createResizedImage(photo.getImageIcon().getImage(),width_height , width_height, true);
						fillLabel(image);
						undoStack.push(image);
						redoStack.clear();
					}
				}
			}

			public void mouseEntered(MouseEvent e) {
				btnNewButton_9.setToolTipText("resize photo");
				btnNewButton_9.setBackground(Color.gray);
			}

			public void mouseExited(MouseEvent e) {
				btnNewButton_9.setBackground(Color.black);
			}
		});
		btnNewButton_9.setBounds(670, 11, 54, 43);
		btnNewButton_9.setBorderPainted(false);
		frame.getContentPane().add(btnNewButton_9);
		btnNewButton_9.setBackground(Color.black);

		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(568, 177, 156, 14);
		lblNewLabel.setForeground(Color.white);
		lblNewLabel.setVisible(false);
		frame.getContentPane().add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(568, 202, 156, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setVisible(false);

		JLabel lblNewLabel_1 = new JLabel("Collection name");
		lblNewLabel_1.setForeground(Color.white);
		lblNewLabel_1.setBounds(568, 254, 156, 14);
		lblNewLabel_1.setVisible(false);
		frame.getContentPane().add(lblNewLabel_1);

		textField_1 = new JTextField();
		textField_1.setBounds(568, 279, 156, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		textField_1.setVisible(false);

		JButton btnNewButton_13 = new JButton("Add");
		btnNewButton_13.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(isNew) 	
					getNewCollectionInputs();
				else {
					getExistingCollectionInput();
				}
			}
		});
		btnNewButton_13.setBounds(568, 305, 156, 31);
		btnNewButton_13.setBackground(Color.GRAY);
		btnNewButton_13.setForeground(Color.white);
		btnNewButton_13.setVisible(false);
		frame.getContentPane().add(btnNewButton_13);

		JButton btnNewButton_11 = new JButton("New");
		btnNewButton_11.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				lblNewLabel.setVisible(true);
				lblNewLabel_1.setVisible(true);
				textField.setVisible(true);
				textField_1.setVisible(true);
				btnNewButton_13.setVisible(true);
				isNew = true;

			}
		});
		btnNewButton_11.setBounds(568, 117, 66, 23);
		btnNewButton_11.setBackground(Color.GRAY);
		btnNewButton_11.setForeground(Color.white);
		btnNewButton_11.setVisible(false);
		frame.getContentPane().add(btnNewButton_11);

		JButton btnNewButton_12 = new JButton("Existing");
		btnNewButton_12.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				lblNewLabel.setVisible(true);
				lblNewLabel_1.setVisible(true);
				textField.setVisible(true);
				textField_1.setVisible(true);
				btnNewButton_13.setVisible(true);
				isNew = false;
			}
		});
		btnNewButton_12.setBounds(644, 117, 80, 23);
		btnNewButton_12.setBackground(Color.GRAY);
		btnNewButton_12.setForeground(Color.white);
		btnNewButton_12.setVisible(false);
		frame.getContentPane().add(btnNewButton_12);

		JButton btnNewButton_10 = new JButton("Add to Collection");
		btnNewButton_10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(photo.getImageIcon() == null) {

					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					btnNewButton_11.setVisible(true);
					btnNewButton_12.setVisible(true);
				}
			}
		});
		btnNewButton_10.setBackground(Color.gray);
		btnNewButton_10.setForeground(Color.white);
		btnNewButton_10.setBounds(568, 75, 156, 31);
		frame.getContentPane().add(btnNewButton_10);


		frame.setPreferredSize(new Dimension(750, 550));
		frame.pack();
		frame.setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu menu = new JMenu("FILE");
		menuBar.add(menu);

		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				loadOriginalImage();

				Image img = photo.getImageIcon().getImage();

				Image dimg = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(),Image.SCALE_SMOOTH);
				ImageIcon imgIcon = new ImageIcon(dimg);

				imageLabel.setIcon(imgIcon);

				undoStack.push(dimg);
				redoStack.clear();
			}
		});
		menu.add(openMenuItem);

		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(photo.getImageIcon() == null) {

					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					//saves it in the home directory
					image_manager.saveImage(photo.getImageIcon().getImage());
				}
			}
		});
		menu.add(saveMenuItem);

		JMenuItem saveAsMenuItem = new JMenuItem("Save as..");
		saveAsMenuItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(photo.getImageIcon() == null) {

					JOptionPane.showMessageDialog(frame, "No image loaded!");
				}else {
					image_manager.saveImageAs(photo.getImageIcon().getImage());
				}
			}
		});
		menu.add(saveAsMenuItem);

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				System.exit(0);
			}
		});
		menu.add(exitMenuItem);

		JMenu editMenu = new JMenu("EDIT");
		menuBar.add(editMenu);

		JMenuItem undoMenuItem = new JMenuItem("Undo");
		undoMenuItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				if(undoStack.isEmpty())
					JOptionPane.showMessageDialog(frame, "Can't undo anymore");	
				else {
					Image image = undoStack.pop();
					redoStack.push(image);
					fillLabel(image);
				}
			}
		});
		editMenu.add(undoMenuItem);

		JMenuItem redoMenuItem = new JMenuItem("Redo");
		redoMenuItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(redoStack.isEmpty())
					JOptionPane.showMessageDialog(frame, "Can't redo anymore");
				else {
					Image image = redoStack.pop();
					undoStack.push(image);
					fillLabel(image);
				}
			}
		});
		editMenu.add(redoMenuItem);
	}

	/**
	 * method loads the original image user selects to the screen.
	 */
	public void loadOriginalImage() {
		BufferedImage image;
		String path = image_manager.getImagePath();
		try {
			image = ImageIO.read(new File(path));
			ImageIcon imgIcon = new ImageIcon(image);
			photo.setImageIcon(imgIcon);
			photo.setPath(path);

		} catch (IOException e) {
		}
	}

	/**
	 * loads bcu image when program starts 
	 */
	public void loadInitialImage() {

		Icon initialImage = new ImageIcon(load_resource.loadImage("bcu.png"));
		imageLabel.setIcon(initialImage);
	}

	/**
	 * deletes the content from label 
	 */
	public void removeLabelContent() {		
		imageLabel.setIcon(null);
		photo.setImageIcon(null);
	}

	/**
	 * creates an image icon and sets it the image label
	 * @param image
	 */
	public void fillLabel(Image image) {
		ImageIcon image_icon = new ImageIcon(image);
		photo.setImageIcon(image_icon);
		imageLabel.setIcon(photo.getImageIcon());
	}

	/**
	 * gets user details and creates a new image collection
	 */
	public void getNewCollectionInputs() {

		Date date = new Date(622790105000L); //make sure to change the format

		String username = textField.getText();
		String collection_name = textField_1.getText();
		String imageName = null;

		if ((dbConnector.checkUserName(username)) && (dbConnector.checkCollectionName(collection_name))) {

			imageName= JOptionPane.showInputDialog("Enter name to save image with");

			try {
				ImageIO.write((RenderedImage) photo.getImageIcon().getImage(), "jpg", new File(imageName));
			} catch (IOException e) {
				e.printStackTrace();
			}

			dbConnector.createCollection(imageName, collection_name, date);

		}else {
			dbConnector.registerUserName(username);
			dbConnector.registerCollectionName(collection_name, username);
			dbConnector.createCollection(imageName, collection_name, date);
		}

		File file = new File(imageName);
		file.delete();
		System.out.println("collection created successfully! ");

	}

	/**
	 * checks if collection already exists
	 */
	public void getExistingCollectionInput(){

		Date date = new Date(622790105000L); //make sure to change the format

		String username = textField.getText();
		String collection_name = textField_1.getText();
		String imageName = null;

		if ((dbConnector.checkUserName(username)) && (dbConnector.checkCollectionName(collection_name))) {

			imageName= JOptionPane.showInputDialog("Enter name to save image with");

			try {
				ImageIO.write((RenderedImage) photo.getImageIcon().getImage(), "jpg", new File(imageName));
			} catch (IOException e) {
				e.printStackTrace();
			}

			dbConnector.createCollection(imageName, collection_name, date);

			File file = new File(imageName);
			file.delete();

		}else {
			JOptionPane.showMessageDialog(frame, "Collection/User does not exist!");
		}

	}
}
