package at.fraubock.spendenverwaltung.gui.filter;

import java.util.List;

/**
 * used in {@link CreateFilter} for receiving the models for the current filter
 * type to be rendered.
 * 
 * @author philipp muhoray
 * 
 */
public interface IPropertySelectorProvider {

	/**
	 * @return list of {@link PropertyComponent} instances to be rendered in each
	 *         {@link PropertySelector}
	 */
	public List<PropertyComponent> receiveModels();

}
