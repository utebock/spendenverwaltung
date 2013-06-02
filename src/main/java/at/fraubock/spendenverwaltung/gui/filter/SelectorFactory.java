package at.fraubock.spendenverwaltung.gui.filter;

import java.util.ArrayList;
import java.util.List;

import at.fraubock.spendenverwaltung.gui.filter.comparators.BooleanComparator;
import at.fraubock.spendenverwaltung.gui.filter.comparators.EnumComparator;
import at.fraubock.spendenverwaltung.gui.filter.comparators.StringComparator;
import at.fraubock.spendenverwaltung.util.FilterProperty;

public class SelectorFactory {

	public static List<PropertyComponent> propertySelectorForPerson() {
		List<PropertyComponent> selectors = new ArrayList<PropertyComponent>();
		selectors.add(new PropertyComponent("Vorname",
				FilterProperty.PERSON_GIVENNAME, new StringComparator()));
		selectors.add(new PropertyComponent("Nachname",
				FilterProperty.PERSON_SURNAME, new StringComparator()));
		selectors.add(new PropertyComponent("E-Mail",
				FilterProperty.PERSON_EMAIL, new StringComparator()));
		selectors.add(new PropertyComponent("empfängt von uns E-Mails",
				FilterProperty.PERSON_WANTS_EMAIL, new BooleanComparator()));
		selectors.add(new PropertyComponent("empfängt von uns Post",
				FilterProperty.PERSON_WANTS_MAIL, new BooleanComparator()));
		selectors.add(new PropertyComponent("Geschlecht",
				FilterProperty.PERSON_SEX, new EnumComparator()));
		selectors.add(new PropertyComponent("Titel",
				FilterProperty.PERSON_TITLE, new StringComparator()));
		selectors.add(new PropertyComponent("Telephonnummer",
				FilterProperty.PERSON_TELEPHONE, new EnumComparator()));
		selectors.add(new PropertyComponent("Firma",
				FilterProperty.PERSON_COMPANY, new EnumComparator()));
		selectors.add(new PropertyComponent("Notiz",
				FilterProperty.PERSON_NOTE, new EnumComparator()));
		return selectors;
	}
}
