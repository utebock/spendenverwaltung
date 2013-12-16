/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.filter.FilterBuilder;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.LogicalOperator;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractFilterBuilderTest {

	private static FilterBuilder builder;

	private Filter personFilter;
	private Filter donationFilter;
	private Filter mailingFilter;
	private Filter addressFilter;

	private PropertyCriterion personCompProp;
	private PropertyCriterion donationAmountProp;
	private PropertyCriterion donationDaysBackProp;
	private PropertyCriterion mailingNotNullProp;
	private PropertyCriterion addressIsMainProp;
	private PropertyCriterion personNameProp;

	public static FilterBuilder getBuilder() {
		return builder;
	}

	public static void setBuilder(FilterBuilder builder) {
		AbstractFilterBuilderTest.builder = builder;
	}

	/* testing filter statements */

	@Test(expected = IllegalArgumentException.class)
	public void createStmtWithNullParameter_ThrowsException() {
		builder.createStatement(null);
	}

	@Test
	public void createStmtWithEmptyFilter_ReturnsStatement() {
		String personStmt = builder.createStatement(personFilter);
		String donationStmt = builder.createStatement(donationFilter);
		String mailingStmt = builder.createStatement(mailingFilter);
		String addressStmt = builder.createStatement(addressFilter);

		assertEquals("select * from validated_persons as mount0", personStmt);
		assertEquals("select * from validated_donations as mount0",
				(donationStmt));
		assertEquals("select * from confirmed_mailings as mount0",
				(mailingStmt));
		assertEquals("select * from validated_addresses as mount0",
				(addressStmt));
	}

	/* testing property criterion statements */

	@Test(expected = IllegalArgumentException.class)
	public void propertyCriteriontWithNullValues_ThrowsException() {
		PropertyCriterion personCompProp = new PropertyCriterion();
		personFilter.setCriterion(personCompProp);
		builder.createStatement(personFilter);
	}

	@Test
	public void personCompanyLikeString_ReturnsStatement() {
		personFilter.setCriterion(personCompProp);
		String personStmt = builder.createStatement(personFilter);
		assertEquals(
				"select * from validated_persons as mount0 where company LIKE '%testcompany%'",
				(personStmt));
	}

	@Test
	public void donationAmountLessEqualsNumeric_ReturnsStatement() {
		donationFilter.setCriterion(donationAmountProp);
		String donationStmt = builder.createStatement(donationFilter);
		assertEquals(
				"select * from validated_donations as mount0 where amount <= 100.0",
				(donationStmt));
	}

	@Test
	public void donationDateGreaterDaysBack_ReturnsStatement() {
		donationFilter.setCriterion(donationDaysBackProp);
		String donationStmt = builder.createStatement(donationFilter);
		assertEquals(
				"select * from validated_donations as mount0 where donationdate < DATE_SUB(DATE(NOW()),INTERVAL 25 DAY)",
				(donationStmt));
	}

	@Test
	public void mailingnDateNotNull_ReturnsStatement() {
		mailingFilter.setCriterion(mailingNotNullProp);
		String donationStmt = builder.createStatement(mailingFilter);
		assertEquals(
				"select * from confirmed_mailings as mount0 where mailing_date IS NOT NULL ",
				(donationStmt));
	}

	@Test
	public void isMainAddressEqualsBool_ReturnsStatement() {
		addressFilter.setCriterion(addressIsMainProp);
		String addressStmt = builder.createStatement(addressFilter);
		assertEquals(
				"select * from validated_addresses as mount0 where ismain = true",
				(addressStmt));
	}

	/* testing connected criterion statements */

	@Test(expected = IllegalArgumentException.class)
	public void connectedCriterionWithNullValues_ThrowsException() {
		ConnectedCriterion con = new ConnectedCriterion();
		personFilter.setCriterion(con);
		builder.createStatement(personFilter);
	}

	@Test
	public void connectPropertyCritWithAnd_ReturnsStatement() {
		ConnectedCriterion con = new ConnectedCriterion();
		con.connect(personCompProp, LogicalOperator.AND, personCompProp);
		personFilter.setCriterion(con);

		String stmt = builder.createStatement(personFilter);

		assertEquals(
				"select * from validated_persons as mount0 where "
						+ "(company LIKE '%testcompany%' AND company LIKE '%testcompany%')",
				(stmt));
	}

	@Test
	public void connectConnectedCritWithOr_ReturnsStatement() {
		ConnectedCriterion con1 = new ConnectedCriterion();
		con1.connect(personCompProp, LogicalOperator.AND, personNameProp);

		ConnectedCriterion con2 = new ConnectedCriterion();
		con2.connect(personCompProp, LogicalOperator.AND, personNameProp);

		ConnectedCriterion con = new ConnectedCriterion();
		con.connect(con1, LogicalOperator.OR, con2);
		personFilter.setCriterion(con);

		String stmt = builder.createStatement(personFilter);

		assertEquals(
				"select * from validated_persons as mount0 where ((company LIKE '%testcompany%' AND givenname = 'testname') OR (company LIKE '%testcompany%' AND givenname = 'testname'))",
				(stmt));
	}

	@Test
	public void complex() {
		// person has donated 100euro for each donation or lives in 1070

		// filter address = 1070
		PropertyCriterion plz1070 = new PropertyCriterion();
		plz1070.compare(FilterProperty.ADDRESS_POSTCODE,
				RelationalOperator.EQUALS, "1070");
		Filter addressFilter = new Filter(FilterType.ADDRESS, plz1070);
		MountedFilterCriterion addressMount = new MountedFilterCriterion();
		addressMount.mountAndCompareCount(addressFilter,
				RelationalOperator.GREATER_EQ, 1);

		// filter (donation != 100) = 0 AND donations exist
		PropertyCriterion amount100 = new PropertyCriterion();
		amount100.compare(FilterProperty.DONATION_AMOUNT,
				RelationalOperator.UNEQUAL, 100D);
		Filter donationUnequals100Filter = new Filter(FilterType.DONATION,
				amount100);
		MountedFilterCriterion donationUneq100Mount = new MountedFilterCriterion();
		donationUneq100Mount.mountAndCompareCount(donationUnequals100Filter,
				RelationalOperator.EQUALS, 0);

		Filter emptyDonationFilter = new Filter(FilterType.DONATION);
		MountedFilterCriterion emptyDonMount = new MountedFilterCriterion();
		emptyDonMount.mountAndCompareCount(emptyDonationFilter,
				RelationalOperator.GREATER_EQ, 1);

		ConnectedCriterion andCrit = new ConnectedCriterion();
		andCrit.connect(donationUneq100Mount, LogicalOperator.AND,
				emptyDonMount);

		// connect with or
		ConnectedCriterion orNotCrit = new ConnectedCriterion();
		orNotCrit.connect(andCrit, LogicalOperator.OR, addressMount);

		Filter mainFilter = new Filter(FilterType.PERSON, orNotCrit);
		String stmt = builder.createStatement(mainFilter);
		String result = "select * from validated_persons as mount0 where "
				+ "(((select count(*) from validated_donations as mount1 where"
				+ " mount0.id=mount1.personid and amount <> 100.0) = 0 AND"
				+ " (select count(*) from validated_donations as mount1 where "
				+ "mount0.id=mount1.personid) >= 1) OR (select count(*) from"
				+ " validated_addresses as mount1 join livesat on mount1.id=aid " +
				"where pid=mount0.id and postcode = '1070') >= 1)";
		assertEquals(result, stmt);

	}

	@Before
	public void init() {
		personFilter = new Filter(FilterType.PERSON);
		donationFilter = new Filter(FilterType.DONATION);
		mailingFilter = new Filter(FilterType.MAILING);
		addressFilter = new Filter(FilterType.ADDRESS);

		personCompProp = new PropertyCriterion();
		personCompProp.compare(FilterProperty.PERSON_COMPANY,
				RelationalOperator.LIKE, "testcompany");

		personNameProp = new PropertyCriterion();
		personNameProp.compare(FilterProperty.PERSON_GIVENNAME,
				RelationalOperator.EQUALS, "testname");

		donationAmountProp = new PropertyCriterion();
		donationAmountProp.compare(FilterProperty.DONATION_AMOUNT,
				RelationalOperator.LESS_EQ, 100D);

		donationDaysBackProp = new PropertyCriterion();
		donationDaysBackProp.compareDaysBack(FilterProperty.DONATION_DATE,
				RelationalOperator.GREATER, 25);

		mailingNotNullProp = new PropertyCriterion();
		mailingNotNullProp.compareNotNull(FilterProperty.MAILING_DATE);

		addressIsMainProp = new PropertyCriterion();
		addressIsMainProp.compare(FilterProperty.ADDRESS_IS_MAIN, true);
	}

}
