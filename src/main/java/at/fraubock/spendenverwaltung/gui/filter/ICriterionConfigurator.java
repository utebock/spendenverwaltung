package at.fraubock.spendenverwaltung.gui.filter;

import javax.swing.JComponent;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.service.to.CriterionTO;

public interface ICriterionConfigurator {
	
	public CriterionTO createCriterion() throws InvalidInputException;
	public JComponent getConfigComponent();
	@Override
	public String toString();
	
}
