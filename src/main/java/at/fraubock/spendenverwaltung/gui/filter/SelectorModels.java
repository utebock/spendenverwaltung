package at.fraubock.spendenverwaltung.gui.filter;

import java.util.ArrayList;
import java.util.List;

import at.fraubock.spendenverwaltung.util.FilterProperty;

public class SelectorModels {

	public static List<PropertyCriterionComponent> getPersonSelectorModels() {
		List<PropertyCriterionComponent> selectors = new ArrayList<PropertyCriterionComponent>();
		selectors.add(new PropertyCriterionComponent("Vorname",
				FilterProperty.PERSON_GIVENNAME, new StringComparator()));
		selectors.add(new PropertyCriterionComponent("Nachname",
				FilterProperty.PERSON_SURNAME, new StringComparator()));
		selectors.add(new PropertyCriterionComponent("E-Mail",
				FilterProperty.PERSON_EMAIL, new StringComparator()));
		selectors.add(new PropertyCriterionComponent("E-Mail Benachrichtigungen",
				FilterProperty.PERSON_WANTS_EMAIL, new BooleanComparator()));
		selectors.add(new PropertyCriterionComponent("Postalische Benachrichtigungen",
				FilterProperty.PERSON_WANTS_MAIL, new BooleanComparator()));
		return selectors;
	}
}
