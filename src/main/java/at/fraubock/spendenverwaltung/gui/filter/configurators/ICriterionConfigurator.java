package at.fraubock.spendenverwaltung.gui.filter.configurators;

import javax.swing.JComponent;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.filter.CriterionSelector;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;

/**
 * interface for {@link CriterionSelector} that provides a {@link Criterion}'s
 * GUI components and a factory method. the GUI components depend on many things
 * regarding the type of the criterion. a PropertyCriterion that represents a
 * boolean will have other components than one representing a String or Date. a
 * MountedCriterion might have different components too. each implementing class
 * should provide a panel which holds all necessary GUI components for
 * configuring the type criterion and a name for this criterion which will be
 * shown in a drop down box.
 * 
 * @author philipp muhoray
 * 
 */
public interface ICriterionConfigurator {

	/**
	 * creates the criterion of this configurator based on the user's input in
	 * the panel
	 * 
	 * @return criterion
	 * @throws InvalidInputException
	 */
	public Criterion createCriterion() throws InvalidInputException;

	/**
	 * a panel that let's a user configure this criterion
	 * 
	 * @return
	 */
	public JComponent getConfigComponent();

	/**
	 * will be shown to the user
	 * 
	 * @return
	 */
	@Override
	public String toString();

}
