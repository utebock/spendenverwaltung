package at.fraubock.spendenverwaltung.gui.views;

import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;

public class ConfirmMailingsView extends InitializableView {

	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	private IMailingService mailingService;
	
	private JPanel contentPanel;

	public void ConfirmMailingsView(ViewActionFactory viewActionFactory,
			ComponentFactory componentFactory, IMailingService mailingService) {
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.mailingService = mailingService;
		
		setUpLayout();
	}
	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(800, 800);
		
		this.add(contentPanel);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	

}
