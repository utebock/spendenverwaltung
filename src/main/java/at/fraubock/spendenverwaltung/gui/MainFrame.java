package at.fraubock.spendenverwaltung.gui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;


public class MainFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	
	public MainFrame(){
		super("Ute Bock Spendenverwaltungssystem");
		this.addWindowListener(new WindowHandler());
	}
	
	public void setPersonService(IPersonService personService){
		this.personService = personService;
	}
	public void setAddressService(IAddressService addressService){
		this.addressService = addressService;
	}
	public void setDonationService(IDonationService donationService){
		this.donationService = donationService;
	}
	
	public void openMainWindow(){
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

	
	private void setUpGUI() {
		
		MainFrame frame = new MainFrame();
		JPanel panel = new JPanel();
		frame.add(new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		frame.add(new Overview(personService, addressService, donationService));
		frame.setSize(800, 1000);
		frame.setLocation(100,100);
		frame.setResizable(true);
		frame.setVisible(true);
		
	}

}
