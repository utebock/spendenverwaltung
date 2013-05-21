package gui;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ComponentBuilder {

	public JButton createImageButton(String path, ActionListener listener){
		java.net.URL url = getClass().getResource(path);
		JButton button = new JButton(new ImageIcon(url));
		button.addActionListener(listener);
		return button;
	}
	
	public JPanel createPanel(){
		return new JPanel();
	}
	
	public JLabel createLabel(String text){
		return new JLabel(text);
	}
	
	public JLabel createImageLabel(ImageIcon img){
		return new JLabel(img);
	}

	public ImageIcon createImageIcon(String path) {
		java.net.URL url = getClass().getResource(path);
		return new ImageIcon(url);
	}
}
