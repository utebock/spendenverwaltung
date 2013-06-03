package at.fraubock.spendenverwaltung.gui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

/**
 * use this class for our input fields
 * 
 * @author philipp muhoray
 *
 */
public class CustomTextField extends JTextField {
	private static final long serialVersionUID = -1994613008380818598L;
	
	public CustomTextField() {}
	
	public CustomTextField(int length) {
		super(length);
	}
	
	public void invalidateInput() {

		this.requestFocus();
		this.setBackground(new Color(255,153,153));
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				setBackground(new Color(255,255,255));
			}
			public void keyPressed(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {}
		});
	}
	
}
