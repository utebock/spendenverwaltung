package at.fraubock.spendenverwaltung.gui.container;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

//import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
//import at.fraubock.spendenverwaltung.gui.views.CreatePersonView;

/**
 * 
 * @author Chris Steele
 * 
 * ViewDisplayer contains the main frame of the application and offers
 * functionality to switch the currently displayed view.
 */
public class ViewDisplayer {

	private JFrame frame;
	private JScrollPane scrollPane;
	
	public ViewDisplayer() {
		
		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		frame = new JFrame("Ute Bock Spendenverwaltung");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(scrollPane);
		frame.setSize(800, 800);
		frame.setLocation(100,100);
		frame.setResizable(true);
		frame.setVisible(true);
	}
	
	/**
	 * changes the content of the frame's JScrollPane and displays it
	 * @param newView
	 */
	public void changeView(Component newView) {
		scrollPane.getViewport().removeAll();
		scrollPane.repaint();
		scrollPane.getViewport().add(newView);
	}
	
//	/**
//	 * small test method to make sure view switching works
//	 * @throws InterruptedException
//	 */
//	public static void main(String[] args) throws InterruptedException {
//		CreatePersonView cpView = new CreatePersonView(null, null, null, null, new ComponentFactory());
//		ViewDisplayer viewDisplayer = new ViewDisplayer();
//		viewDisplayer.changeView(cpView);
//	}
}
