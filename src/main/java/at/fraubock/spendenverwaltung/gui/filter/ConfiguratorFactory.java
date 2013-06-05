package at.fraubock.spendenverwaltung.gui.filter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;

public class ConfiguratorFactory {

	private FilterType type;
	private List<Filter> personFilters;
	private List<Filter> donationFilters;
	private List<Filter> mailingFilters;
	private List<Filter> addressFilters;

	public ConfiguratorFactory(FilterType type) {
		this.type = type;
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"/spring.xml");
		IFilterService filterService = context.getBean("filterService",
				IFilterService.class);
		this.personFilters = new ArrayList<Filter>();
		try {
			personFilters = filterService.getAll(FilterType.PERSON);
			mailingFilters = filterService.getAll(FilterType.MAILING);
			donationFilters = filterService.getAll(FilterType.DONATION);
			addressFilters = filterService.getAll(FilterType.ADDRESS);
		} catch (ServiceException e) {
			// TODO err msg + log
		}
	}

	public List<ICriterionConfigurator> getConfigurators() {

		List<ICriterionConfigurator> configurators = new ArrayList<ICriterionConfigurator>();

		if (type == FilterType.PERSON) {

			LinkedHashMap<Object, String> sexMap = new LinkedHashMap<Object, String>();
			sexMap.put("männlich", Person.Sex.MALE.getName());
			sexMap.put("weiblich", Person.Sex.FEMALE.getName());
			sexMap.put("Familie", Person.Sex.FAMILY.getName());
			sexMap.put("Unternehmen", Person.Sex.COMPANY.getName());

			configurators.add(new PersonToDonationFilterConfig(
					"Spendenfilter hinzufügen", donationFilters));
			configurators.add(new StringComparator(
					FilterProperty.PERSON_GIVENNAME, "Vorname"));
			configurators.add(new StringComparator(
					FilterProperty.PERSON_SURNAME, "Nachname"));
			configurators.add(new StringComparator(FilterProperty.PERSON_EMAIL,
					"E-Mail"));
			configurators.add(new BooleanComparator(
					FilterProperty.PERSON_WANTS_EMAIL,
					"empfängt von uns E-Mails"));
			configurators.add(new BooleanComparator(
					FilterProperty.PERSON_WANTS_MAIL, "empfängt von uns Post"));
			configurators.add(new EnumComparator(sexMap,
					FilterProperty.PERSON_SEX, "Geschlecht"));
			configurators.add(new StringComparator(FilterProperty.PERSON_TITLE,
					"Titel"));
			configurators.add(new StringComparator(
					FilterProperty.PERSON_TELEPHONE, "Telephonnummer"));
			configurators.add(new StringComparator(
					FilterProperty.PERSON_COMPANY, "Firma"));
			configurators.add(new StringComparator(FilterProperty.PERSON_NOTE,
					"Notiz"));
			configurators.add(new MountedFilterSingleResultConfig(
					"Personenfilter hinzufügen", personFilters));
			configurators.add(new PersonToMailingFilterConfig(
					"Aussendungsfilter hinzufügen", mailingFilters));

		} else if (type == FilterType.DONATION) {

			LinkedHashMap<Object, String> donationTypes = new LinkedHashMap<Object, String>();
			for (DonationType donType : Donation.DonationType.values()) {
				donationTypes.put(donType.getName(), donType.getName());
			}

			configurators.add(new NumberComparator(
					FilterProperty.DONATION_AMOUNT, "Betrag"));
			configurators.add(new DateComparator(FilterProperty.DONATION_DATE,
					"Datum"));
			configurators.add(new DaysBackComparator(
					FilterProperty.DONATION_DATE, "Liegt zurück"));
			configurators.add(new StringComparator(
					FilterProperty.DONATION_DEDICATION, "Widmung"));
			configurators.add(new EnumComparator(donationTypes,
					FilterProperty.DONATION_TYPE, "Art der Spende"));
			configurators.add(new StringComparator(
					FilterProperty.DONATION_NOTE, "Notiz"));

		} else if (type == FilterType.MAILING) {

			LinkedHashMap<Object, String> mediumMap = new LinkedHashMap<Object, String>();
			mediumMap.put("E-Mail", Mailing.Medium.EMAIL.getName());
			mediumMap.put("Post", Mailing.Medium.POSTAL.getName());

			LinkedHashMap<Object, String> typeMap = new LinkedHashMap<Object, String>();
			for (MailingType mailType : Mailing.MailingType.values()) {
				typeMap.put(mailType.getName(), mailType.getName());
			}

			configurators.add(new DateComparator(FilterProperty.MAILING_DATE,
					"Datum"));
			configurators.add(new DaysBackComparator(
					FilterProperty.MAILING_DATE, "Liegt zurück"));
			configurators.add(new EnumComparator(mediumMap,
					FilterProperty.MAILING_MEDIUM, "Medium der Aussendung"));
			configurators.add(new EnumComparator(typeMap,
					FilterProperty.MAILING_TYPE, "Art der Aussendung"));

		}

		return configurators;
	}
}
