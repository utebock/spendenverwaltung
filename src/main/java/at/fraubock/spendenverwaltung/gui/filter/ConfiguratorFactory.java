package at.fraubock.spendenverwaltung.gui.filter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.gui.filter.configurators.mounted.AddressConfigurator;
import at.fraubock.spendenverwaltung.gui.filter.configurators.mounted.DonationToPersonFilterConfig;
import at.fraubock.spendenverwaltung.gui.filter.configurators.mounted.MailingToPersonFilterConfig;
import at.fraubock.spendenverwaltung.gui.filter.configurators.mounted.PersonToMailingFilterConfig;
import at.fraubock.spendenverwaltung.gui.filter.configurators.mounted.SimpleFilterMountConfig;
import at.fraubock.spendenverwaltung.gui.filter.configurators.property.BooleanComparator;
import at.fraubock.spendenverwaltung.gui.filter.configurators.property.DateComparator;
import at.fraubock.spendenverwaltung.gui.filter.configurators.property.DaysBackComparator;
import at.fraubock.spendenverwaltung.gui.filter.configurators.property.EnumComparator;
import at.fraubock.spendenverwaltung.gui.filter.configurators.property.NumberComparator;
import at.fraubock.spendenverwaltung.gui.filter.configurators.property.StringComparator;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing.MailingType;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.FilterType;

/**
 * creates a list of {@link ICriterionConfigurator}s for a given
 * {@link FilterType}
 * 
 * @author philipp muhoray
 * 
 */
public class ConfiguratorFactory {

	private FilterType type;
	@SuppressWarnings("unused")
	private Filter editFilter;
	private List<Filter> personFilters;
	private List<Filter> donationFilters;
	private List<Filter> mailingFilters;
	private List<Filter> addressFilters;

	public ConfiguratorFactory(FilterType type, IFilterService filterService) {
		this(type, null, filterService);
	}

	public ConfiguratorFactory(FilterType type, Filter editFilter,
			IFilterService filterService) {
		this.type = type;
		this.editFilter = editFilter;
		this.personFilters = new ArrayList<Filter>();
		try {
			personFilters = filterService.getAllByFilter(FilterType.PERSON).a;
			mailingFilters = filterService.getAllByFilter(FilterType.MAILING).a;
			donationFilters = filterService.getAllByFilter(FilterType.DONATION).a;
			addressFilters = filterService.getAllByFilter(FilterType.ADDRESS).a;
		} catch (ServiceException e) {
			// TODO err msg + log
		}

		if (editFilter != null) {
			if (personFilters.contains(editFilter)) {
				personFilters.remove(editFilter);
			} else if (donationFilters.contains(editFilter)) {
				donationFilters.remove(editFilter);
			} else if (donationFilters.contains(editFilter)) {
				donationFilters.remove(editFilter);
			} else if (addressFilters.contains(editFilter)) {
				addressFilters.remove(editFilter);
			}
		}
	}

	public List<ICriterionConfigurator> getConfigurators() {

		List<ICriterionConfigurator> configurators = new ArrayList<ICriterionConfigurator>();

		if (type == FilterType.PERSON) {

			LinkedHashMap<Object, String> sexMap = new LinkedHashMap<Object, String>();
			sexMap.put("m\u00E4nnlich", Person.Sex.MALE.getName());
			sexMap.put("weiblich", Person.Sex.FEMALE.getName());
			sexMap.put("Familie", Person.Sex.FAMILY.getName());
			sexMap.put("Unternehmen", Person.Sex.COMPANY.getName());

			configurators.add(new StringComparator(
					FilterProperty.PERSON_GIVENNAME, "Vorname"));
			configurators.add(new StringComparator(
					FilterProperty.PERSON_SURNAME, "Nachname"));
			configurators.add(new StringComparator(FilterProperty.PERSON_EMAIL,
					"E-Mail"));
			configurators.add(new BooleanComparator(
					FilterProperty.PERSON_WANTS_EMAIL,
					"empf\u00E4ngt von uns E-Mails"));
			configurators.add(new BooleanComparator(
					FilterProperty.PERSON_WANTS_MAIL,
					"empf\u00E4ngt von uns Post"));
			configurators.add(new EnumComparator(sexMap,
					FilterProperty.PERSON_SEX, "Geschlecht"));
			configurators.add(new StringComparator(FilterProperty.PERSON_TITLE,
					"Titel"));
			configurators.add(new StringComparator(
					FilterProperty.PERSON_TELEPHONE, "Telefonnummer"));
			configurators.add(new StringComparator(
					FilterProperty.PERSON_COMPANY, "Firma"));
			configurators.add(new StringComparator(FilterProperty.PERSON_NOTE,
					"Notiz"));

			configurators.add(new AddressConfigurator("Adresse"));

			if (!donationFilters.isEmpty()) {
				configurators.add(new DonationToPersonFilterConfig(
						"Spenden der Person", donationFilters));
			}

			if (!mailingFilters.isEmpty()) {
				configurators.add(new MailingToPersonFilterConfig(
						"Aussendungen der Person", mailingFilters));
			}

			if (!personFilters.isEmpty()) {
				configurators.add(new SimpleFilterMountConfig(
						"Weiteren Personenfilter hinzuf\u00FCgen",
						personFilters, this.type,
						"Die Person erf\u00FCllt diesen Filter:"));
			}

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
					FilterProperty.DONATION_DATE, "Liegt zur\u00FCck"));
			configurators.add(new StringComparator(
					FilterProperty.DONATION_DEDICATION, "Widmung"));
			configurators.add(new EnumComparator(donationTypes,
					FilterProperty.DONATION_TYPE, "Art der Spende"));
			configurators.add(new StringComparator(
					FilterProperty.DONATION_NOTE, "Notiz"));

			if (!personFilters.isEmpty()) {
				configurators.add(new SimpleFilterMountConfig("Spender",
						personFilters, FilterType.PERSON,
						"Der Spender erf\u00FCllt diesen Personenfilter:"));
			}

			if (!donationFilters.isEmpty()) {
				configurators.add(new SimpleFilterMountConfig(
						"Weiteren Spendenfilter hinzuf\u00FCgen",
						donationFilters, this.type,
						"Die Spende erf\u00FCllt diesen Filter:"));
			}

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
					FilterProperty.MAILING_DATE, "Liegt zur\u00FCck"));
			configurators.add(new EnumComparator(mediumMap,
					FilterProperty.MAILING_MEDIUM, "Medium der Aussendung"));
			configurators.add(new EnumComparator(typeMap,
					FilterProperty.MAILING_TYPE, "Art der Aussendung"));

			if (!personFilters.isEmpty()) {
				configurators.add(new PersonToMailingFilterConfig(
						"Empf\u00E4nger", personFilters));
			}

			if (!mailingFilters.isEmpty()) {
				configurators.add(new SimpleFilterMountConfig(
						"Weiteren Aussendungsfilter hinzuf\u00FCgen",
						mailingFilters, this.type,
						"Die Aussendung erf\u00FCllt diesen Filter:"));
			}

		}

		return configurators;
	}
}
