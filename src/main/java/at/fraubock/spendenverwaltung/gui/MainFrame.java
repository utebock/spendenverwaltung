package at.fraubock.spendenverwaltung.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;


public class MainFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private static IPersonService personService;
	private static IAddressService addressService;
	private static IDonationService donationService;
	
	public MainFrame(){
		super("Ute Bock Spendenverwaltungssystem");
		this.addWindowListener(new WindowAdapter(){ 
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}});
	}
	
	public void setPersonService(IPersonService personService){
		MainFrame.personService = personService;
	}
	public void setAddressService(IAddressService addressService){
		MainFrame.addressService = addressService;
	}
	public void setDonationService(IDonationService donationService){
		MainFrame.donationService = donationService;
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
		
		MainFrame frame = new MainFrame();
		JPanel panel = new JPanel();
		frame.add(new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		frame.add(new Overview(personService, addressService, donationService));
		frame.setSize(800, 800);
		frame.setLocation(100,100);
		frame.setResizable(true);
		frame.setVisible(true);
		
	}

}
