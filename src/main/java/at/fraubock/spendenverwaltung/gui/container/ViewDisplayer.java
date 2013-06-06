package at.fraubock.spendenverwaltung.gui.container;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.views.CreatePersonView;

public class ViewDisplayer {

	private JFrame frame;
	private JScrollPane scrollPane;
	
	public ViewDisplayer(Component initialDisplay) {
		
		scrollPane = new JScrollPane(initialDisplay);
		
		frame = new JFrame("Ute Bock Spendenverwaltung");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(scrollPane);
		frame.setSize(800, 800);
		frame.setLocation(100,100);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
	}
	
	public void changeView(Component newView) {
		scrollPane.removeAll();
		scrollPane.revalidate();
		scrollPane.repaint();
		scrollPane.add(newView);
	}
	
	public static void main(String[] args) throws InterruptedException {
		CreatePersonView cpView = new CreatePersonView(null, null, null, null, new ComponentFactory());
		ViewDisplayer viewDisplayer = new ViewDisplayer(cpView);
		Thread.sleep(1000);
		cpView.removeAll();
		cpView.revalidate();
		cpView.repaint();
		viewDisplayer.changeView(new JTextField(10));
	}
	
}
