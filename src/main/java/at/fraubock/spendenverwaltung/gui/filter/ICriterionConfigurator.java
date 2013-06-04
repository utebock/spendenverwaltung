package at.fraubock.spendenverwaltung.gui.filter;

import javax.swing.JComponent;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;

public interface ICriterionConfigurator {
	
	public Criterion createCriterion() throws InvalidInputException;
	public JComponent getConfigComponent();
	@Override
	public String toString();
	
}
