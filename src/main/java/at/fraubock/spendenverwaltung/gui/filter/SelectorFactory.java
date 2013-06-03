package at.fraubock.spendenverwaltung.gui.filter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import at.fraubock.spendenverwaltung.gui.filter.comparators.BooleanComparator;
import at.fraubock.spendenverwaltung.gui.filter.comparators.DateComparator;
import at.fraubock.spendenverwaltung.gui.filter.comparators.DaysBackComparator;
import at.fraubock.spendenverwaltung.gui.filter.comparators.EnumComparator;
import at.fraubock.spendenverwaltung.gui.filter.comparators.NumberComparator;
import at.fraubock.spendenverwaltung.gui.filter.comparators.StringComparator;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing.MailingType;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
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

		LinkedHashMap<Object, String> sexMap = new LinkedHashMap<Object, String>();
		sexMap.put("männlich", Person.Sex.MALE.getName());
		sexMap.put("weiblich", Person.Sex.FEMALE.getName());
		sexMap.put("Familie", Person.Sex.FAMILY.getName());
		sexMap.put("Unternehmen", Person.Sex.COMPANY.getName());
		selectors.add(new PropertyComponent("Geschlecht",
				FilterProperty.PERSON_SEX, new EnumComparator(sexMap)));

		selectors.add(new PropertyComponent("Titel",
				FilterProperty.PERSON_TITLE, new StringComparator()));

		selectors.add(new PropertyComponent("Telephonnummer",
				FilterProperty.PERSON_TELEPHONE, new StringComparator()));

		selectors.add(new PropertyComponent("Firma",
				FilterProperty.PERSON_COMPANY, new StringComparator()));

		selectors.add(new PropertyComponent("Notiz",
				FilterProperty.PERSON_NOTE, new StringComparator()));

		return selectors;
	}

	public static List<PropertyComponent> propertySelectorForDonation() {
		List<PropertyComponent> selectors = new ArrayList<PropertyComponent>();

		selectors.add(new PropertyComponent("Betrag",
				FilterProperty.DONATION_AMOUNT, new NumberComparator()));

		selectors.add(new PropertyComponent("Datum",
				FilterProperty.DONATION_DATE, new DateComparator()));

		selectors.add(new PropertyComponent("Liegt zurück",
				FilterProperty.DONATION_DATE, new DaysBackComparator()));

		selectors.add(new PropertyComponent("Widmung",
				FilterProperty.DONATION_DEDICATION, new StringComparator()));

		LinkedHashMap<Object, String> donationMap = new LinkedHashMap<Object, String>();
		for (DonationType donType : Donation.DonationType.values()) {
			donationMap.put(donType.getName(), donType.getName());
		}
		selectors.add(new PropertyComponent("Art der Spende",
				FilterProperty.DONATION_TYPE, new EnumComparator(donationMap)));

		selectors.add(new PropertyComponent("Notiz",
				FilterProperty.DONATION_NOTE, new StringComparator()));

		return selectors;
	}

	public static List<PropertyComponent> propertySelectorForMailing() {
		List<PropertyComponent> selectors = new ArrayList<PropertyComponent>();

		selectors.add(new PropertyComponent("Datum",
				FilterProperty.MAILING_DATE, new DateComparator()));

		selectors.add(new PropertyComponent("Liegt Tage zurück",
				FilterProperty.MAILING_DATE, new DaysBackComparator()));

		LinkedHashMap<Object, String> mediumMap = new LinkedHashMap<Object, String>();
		mediumMap.put("E-Mail", Mailing.Medium.EMAIL.getName());
		mediumMap.put("Post", Mailing.Medium.POSTAL.getName());
		selectors.add(new PropertyComponent("Medium der Aussendung",
				FilterProperty.MAILING_MEDIUM, new EnumComparator(mediumMap)));

		LinkedHashMap<Object, String> typeMap = new LinkedHashMap<Object, String>();
		for (MailingType mailType : Mailing.MailingType.values()) {
			typeMap.put(mailType.getName(), mailType.getName());
		}
		selectors.add(new PropertyComponent("Art der Aussendung",
				FilterProperty.MAILING_TYPE, new EnumComparator(typeMap)));

		return selectors;
	}
}
