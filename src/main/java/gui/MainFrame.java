package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import service.IPersonService;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private static IPersonService personService;
	
	public MainFrame(){
		super();
		this.addWindowListener(new WindowAdapter(){ 
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}});
	}
	
	public void setIPersonService(IPersonService personService){
		MainFrame.personService = personService;
	}

	public static void openMainWindow(){
		setUpGUI();
		
		Runnable run = new Runnable(){
			public void run() {
				try{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		javax.swing.SwingUtilities.invokeLater(run);
	}

	
	private static void setUpGUI() {
		/**
		 * FIXME: Setting up connection here
		 */
		MainFrame frame = new MainFrame();
		JPanel panel = new JPanel();
		frame.add(new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		/**
		 * FIXME: Either add buttons here or call methods for doing so
		 */
		frame.setSize(800, 800);
		frame.setLocation(100,100);
		frame.setResizable(true);
		frame.setVisible(true);
		
	}

}
